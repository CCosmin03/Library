package mapper;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import repository.security.RightsRolesRepository;
import view.model.UserDTO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User convertUserDTOToUser(UserDTO userDTO, RightsRolesRepository rightsRolesRepository) {

        List<Role> roles = Arrays.stream(userDTO.getRoles().split(","))
                .map(String::trim)
                .map(rightsRolesRepository::findRoleByTitle)
                .collect(Collectors.toList());

        return new UserBuilder()
                .setUsername(userDTO.getUsername())
                .setPassword(userDTO.getPassword())
                .setRoles(roles)
                .build();
    }

    public static UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toList());
        userDTO.setRoles(roleNames);

        return userDTO;
    }

    public static List<UserDTO> convertUserListToUserDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    public static List<User> convertUserDTOListToUserList(List<UserDTO> userDTOs, RightsRolesRepository rightsRolesRepository) {
        return userDTOs.stream()
                .map(userDTO -> convertUserDTOToUser(userDTO, rightsRolesRepository))
                .collect(Collectors.toList());
    }
}
