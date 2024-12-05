package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Role;
import model.User;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import service.user.AdminUserService;
import view.AdminView;
import view.model.builder.UserDTOBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AdminController {
    private final AdminView adminView;
    private final AdminUserService adminUserService;
    private final RightsRolesRepository rightsRolesRepository;
    private final UserRepository userRepository;
    private final User loggedUser;

    public AdminController(AdminView adminView, AdminUserService adminUserService,
                           RightsRolesRepository rightsRolesRepository, UserRepository userRepository,
                           User loggedUser) {
        this.adminView = adminView;
        this.adminUserService = adminUserService;
        this.rightsRolesRepository = rightsRolesRepository;
        this.userRepository = userRepository;
        this.loggedUser = loggedUser;

        this.adminView.addAddButtonListener(new AddUserButtonListener());
    }

    private class AddUserButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                adminView.addDisplayAlertMessage("Add User Error",
                        "Missing Fields",
                        "Both Username and Password are required.");
                return;
            }

            try {

                Role employeeRole = rightsRolesRepository.findRoleByTitle("employee");
                if (employeeRole == null) {
                    adminView.addDisplayAlertMessage("Add User Error",
                            "Role Not Found",
                            "The default role 'employee' does not exist in the system.");
                    return;
                }
                System.out.println("Debug: Found Employee Role - ID: " + employeeRole.getId());

                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(hashPassword(password));
                newUser.setRoles(List.of(employeeRole));

                boolean saveResult = userRepository.save(newUser);
                if (!saveResult) {
                    adminView.addDisplayAlertMessage("Add User Error",
                            "Database Error",
                            "An error occurred while saving the user.");
                    return;
                }

                System.out.println("Debug: New User Saved - ID: " + newUser.getId());

                adminView.addUserToObservableList(
                        new UserDTOBuilder()
                                .setUsername(username)
                                .setRoles(List.of("employee"))
                                .build()
                );

                adminView.addDisplayAlertMessage("Success",
                        "User Added",
                        "User was successfully added by " + loggedUser.getUsername() + "!");
            } catch (Exception e) {
                adminView.addDisplayAlertMessage("Add User Error",
                        "Unexpected Error",
                        "An error occurred while adding the user: " + e.getMessage());
            }
        }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
    }
}
