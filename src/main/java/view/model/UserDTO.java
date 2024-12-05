package view.model;

import javafx.beans.property.*;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    private StringProperty username;
    private StringProperty roles;
    private StringProperty password;

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        if (password == null) {
            password = new SimpleStringProperty(this, "password");
        }
        return password;
    }

    public void setPassword(String password) {
        passwordProperty().set(password);
    }

    public String getUsername() {
        return usernameProperty().get();
    }

    public void setUsername(String username) {
        usernameProperty().set(username);
    }

    public StringProperty usernameProperty() {
        if (username == null) {
            username = new SimpleStringProperty(this, "username");
        }
        return username;
    }
    public StringProperty rolesProperty() {
        if (roles==null){
            roles=new SimpleStringProperty(this,"roles");
        }
        return roles;
    }
    public String getRoles() {
        return rolesProperty().get();
    }

    public void setRoles(List<String> rolesList) {
        String rolesString = String.join(", ", rolesList);
        rolesProperty().set(rolesString);
    }


}
