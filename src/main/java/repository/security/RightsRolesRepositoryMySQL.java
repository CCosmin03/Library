package repository.security;


import model.Right;
import model.Role;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.*;

public class RightsRolesRepositoryMySQL implements RightsRolesRepository  {
    public RightsRolesRepositoryMySQL(Connection connection){this.connection=connection;}
    private final Connection connection;
    @Override
    public void addRole(String role) {
         try {
             PreparedStatement insertStatement = connection.prepareStatement("INSERT IGNORE INTO "+ROLE+" values (null,?)");
             insertStatement.setString(1,role);
             insertStatement.executeUpdate();
         }catch (SQLException e){
             e.printStackTrace();
         }
    }

    @Override
    public void addRight(String right) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT IGNORE INTO`"+RIGHT+"` values(null,?)");
            insertStatement.setString(1,right);
            insertStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public Role findRoleByTitle(String role) {
        Statement statement;
        try {
            statement=connection.createStatement();
            String fetchRoleSql="SELECT * FROM "+ROLE+" WHERE `role`=\'"+role+"\'";
            ResultSet roleResultSet= statement.executeQuery(fetchRoleSql);
            roleResultSet.next();
            Long roleId=roleResultSet.getLong("id");
            String roleTitle=roleResultSet.getString("role");
            return new Role(roleId, roleTitle, null);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Role findRoleById(Long roleId) {
        String query = "SELECT * FROM " +ROLE + " WHERE id = ?";
        System.out.println("Debug: Executing SQL to fetch role by ID: " + query + " with roleId = " + roleId);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, roleId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role(
                            rs.getLong("id"),
                            rs.getString("role"),
                            null
                    );
                    System.out.println("Debug: Successfully fetched role - ID: " + role.getId() + ", Role: " + role.getRole());
                    return role;
                } else {
                    System.out.println("Debug: No role found with ID " + roleId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching role with ID " + roleId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Right findRightByTitle(String right) {
        Statement statement;
        try{
            statement=connection.createStatement();
            String feetchRoleSql="SELECT * FROM `"+RIGHT+"` WHERE `right`=\'"+right+"\'";
            ResultSet rightResultSet=statement.executeQuery(feetchRoleSql);
            rightResultSet.next();
            Long rightId=rightResultSet.getLong("id");
            String rightTitle=rightResultSet.getString("right");
            return new Right(rightId, rightTitle);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        String sql = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Role role : roles) {
                System.out.println("Debug: Assigning Role ID = " + role.getId() + " to User ID = " + user.getId());
                statement.setLong(1, user.getId());
                statement.setLong(2, role.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            System.out.println("Debug: Roles successfully assigned to user with ID = " + user.getId());
        } catch (SQLException e) {
            System.err.println("Error while assigning roles to user ID " + user.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public List<Role> findRolesForUser(Long userId) {
        try {
            List<Role> roles = new ArrayList<>();
            Statement statement = connection.createStatement();

            // Log the query to fetch roles
            String fetchRoleSql = "SELECT * FROM " + USER_ROLE + " WHERE user_id = " + userId;
            System.out.println("Debug: Executing SQL to fetch roles: " + fetchRoleSql);

            ResultSet userRoleResultSet = statement.executeQuery(fetchRoleSql);

            // Iterate through the result set
            while (userRoleResultSet.next()) {
                long roleId = userRoleResultSet.getLong("role_id");
                System.out.println("Debug: Found role ID: " + roleId);

                Role role = findRoleById(roleId); // Fetch the role by ID
                if (role != null) {
                    System.out.println("Debug: Role details fetched - ID: " + role.getId() + ", Role: " + role.getRole());
                    roles.add(role);
                } else {
                    System.out.println("Debug: Role with ID " + roleId + " not found.");
                }
            }

            System.out.println("Debug: Total roles fetched for user ID " + userId + ": " + roles.size());
            return roles;
        } catch (SQLException e) {
            System.err.println("Error while fetching roles for user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Return null in case of an error
    }


    @Override
    public void addRoleRight(Long roleId, Long rightId) {
        try {
            PreparedStatement insertStatement=connection.prepareStatement("INSERT INTO "+ROLE_RIGHT+" VALUES (null, ?, ?)");
            insertStatement.setLong(1,roleId);
            insertStatement.setLong(2,rightId);
            insertStatement.executeUpdate();
        }catch (SQLException e){

        }

    }
}
