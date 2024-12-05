package launcher;

import controller.AdminController;
import javafx.stage.Stage;
import mapper.UserMapper;
import model.User;
import repository.order.OrderRepository;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import service.user.AdminUserService;
import service.user.AdminUserServiceImpl;
import view.AdminView;
import view.model.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class AdminComponentFactory {
    private final AdminView adminView;
    private final AdminController adminController;
    private final AdminUserService adminUserService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private static volatile AdminComponentFactory instance;

    public static AdminComponentFactory getInstance(Stage stage,
                                                    UserRepository userRepository,
                                                    RightsRolesRepository rightsRolesRepository,
                                                    User loggedUser) {
        if (instance == null) {
            synchronized (AdminComponentFactory.class) {
                if (instance == null) {
                    instance = new AdminComponentFactory(stage, userRepository, rightsRolesRepository, loggedUser);
                }
            }
        }
        return instance;
    }

    private AdminComponentFactory(Stage stage,
                                  UserRepository userRepository,
                                  RightsRolesRepository rightsRolesRepository,
                                  User loggedUser) {

        this.adminUserService=new AdminUserServiceImpl(userRepository, rightsRolesRepository);
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;

        List<UserDTO> users = UserMapper.convertUserListToUserDTOList(adminUserService.findAll());

        this.adminView = new AdminView(stage, users);
        this.adminController = new AdminController(adminView, adminUserService, rightsRolesRepository, userRepository, loggedUser);
    }

    public AdminView getAdminView() {
        return adminView;
    }

    public AdminController getAdminController() {
        return adminController;
    }
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }
}
