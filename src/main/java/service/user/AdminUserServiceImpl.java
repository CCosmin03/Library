package service.user;

import model.Role;
import model.User;
import model.validator.Notification;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.util.Collections;
import java.util.List;

public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AdminUserServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> add(String username, String password, Role role) {
        Notification<Boolean> addUserNotification = new Notification<>();

        if (username == null || username.trim().isEmpty()) {
            addUserNotification.addError("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            addUserNotification.addError("Password cannot be empty.");
        }
        if (role == null) {
            addUserNotification.addError("Role cannot be empty.");
        }

        if (addUserNotification.hasErrors()) {
            return addUserNotification;
        }

        // Save user to database
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Add password hashing if needed
        user.setRoles(Collections.singletonList(role));

        boolean userSaved = userRepository.save(user);
        if (!userSaved) {
            addUserNotification.addError("Could not save user.");
            return addUserNotification;
        }

        // Assign roles to the user
        rightsRolesRepository.addRolesToUser(user, user.getRoles());
        addUserNotification.setResult(true);

        return addUserNotification;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}

