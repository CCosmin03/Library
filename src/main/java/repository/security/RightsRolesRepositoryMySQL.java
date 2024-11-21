package repository.security;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import database.Constants;
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
        Statement statement;
        try{
            statement = connection.createStatement();
            String fetchRoleSql="SELECT * FROM "+ROLE+" WHERE 'id'=\'"+roleId+"\'";
            ResultSet roleResultSet=statement.executeQuery(fetchRoleSql);
            roleResultSet.next();
            String roleTitle=roleResultSet.getString("role");
            return new Role(roleId, roleTitle, null);
        } catch (SQLException e){
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
        try {
            for (Role role : roles){
                PreparedStatement insertUserRoleStatement=connection.prepareStatement("INSERT INTO 'user_role' values (null, ?, ?)");
                insertUserRoleStatement.setLong(1,user.getId());
                insertUserRoleStatement.setLong(2,role.getId());
                insertUserRoleStatement.executeUpdate();
            }
        }catch (SQLException e){

        }

    }

    @Override
    public List<Role> findRolesForUser(Long userId) {
        try {
            List<Role> roles=new ArrayList<>();
            Statement statement=connection.createStatement();
            String fetchRoleSql="SELECT * FROM "+USER_ROLE+" WHERE 'user_id'=\'"+userId+"\'";
            ResultSet userRoleResultSet= statement.executeQuery(fetchRoleSql);
            while (userRoleResultSet.next()){
                long roleId=userRoleResultSet.getLong("role_id");
                roles.add(findRoleById(roleId));
            }
            return roles;
        }catch (SQLException e){

        }
        return null;
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
