package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.model.UserDTO;

import java.util.List;

public class AdminView {
    private TableView<UserDTO> userTableView;
    private ObservableList<UserDTO> usersObservableList;
    private TextField usernameTextField;
    private TextField passwordTextField;
    private Label usernameLabel;
    private Label passwordLabel;
    private Button addButton;

    public AdminView(Stage primaryStage, List<UserDTO> users) {
        primaryStage.setTitle("Admin Dashboard");
        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);

        usersObservableList = FXCollections.observableArrayList(users);
        initTableView(gridPane);

        initAddUserOptions(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane) {
        userTableView = new TableView<>();
        userTableView.setPlaceholder(new Label("No users to display"));

        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserDTO, String> rolesColumn = new TableColumn<>("Roles");
        rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        userTableView.getColumns().addAll(usernameColumn, rolesColumn);
        userTableView.setItems(usersObservableList);
        gridPane.add(userTableView, 0, 0, 3, 1);
    }

    private void initAddUserOptions(GridPane gridPane) {
        usernameLabel = new Label("Username");
        gridPane.add(usernameLabel, 0, 1);
        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 1, 1);

        passwordLabel = new Label("Password");
        gridPane.add(passwordLabel, 0, 2);
        passwordTextField = new TextField();
        gridPane.add(passwordTextField, 1, 2);

        addButton = new Button("Add User");
        gridPane.add(addButton, 1, 4);
    }

    public void addAddButtonListener(EventHandler<ActionEvent> addButtonListener) {
        addButton.setOnAction(addButtonListener);
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return passwordTextField.getText();
    }

    public void addUserToObservableList(UserDTO userDTO) {
        usersObservableList.add(userDTO);
    }

    public void addDisplayAlertMessage(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
