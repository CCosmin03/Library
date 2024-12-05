package controller;

import database.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.AdminComponentFactory;
import launcher.EmployeeComponentFactory;
import launcher.LoginComponentFactory;
import launcher.UserOperationsComponentFactory;
import model.Role;
import model.User;
import model.validator.Notification;
import service.user.AuthenticationService;
import view.LoginView;

import java.util.List;
import java.util.stream.Collectors;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()) {
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                User loggedUser = loginNotification.getResult();
                loginView.setActionTargetText("LogIn Successful!");

                UserOperationsComponentFactory instance= UserOperationsComponentFactory.getInstance(UserOperationsComponentFactory.getComponentsForTests());

                List<String> roles = loggedUser.getRoles().stream()
                        .map(Role::getRole)
                        .peek(role -> System.out.println("Debug: Retrieved role - " + role))
                        .collect(Collectors.toList());

                System.out.println("Debug: All roles for user: " + loggedUser.getUsername()+ "-"+roles);


                if (roles.contains(Constants.Roles.ADMINISTRATOR)) {
                    System.out.println("Debug: Redirecting to Admin Dashboard");
                    AdminComponentFactory.getInstance(
                            LoginComponentFactory.getStage(),
                            instance.getUserRepository(),
                            instance.getRightsRolesRepository(),
                            loggedUser
                    );
                } else {
                    System.out.println("Debug: Redirecting to Employee Dashboard");
                    EmployeeComponentFactory.getInstance(
                            LoginComponentFactory.getComponentsForTests(),
                            LoginComponentFactory.getStage(),
                            loggedUser
                    );
                }
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}
