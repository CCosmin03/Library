package view;

import javafx.beans.Observable;
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
import view.model.BookDTO;

import java.time.LocalDate;
import java.util.List;

public class BookView {
    private TableView bookTableView;
    private ObservableList<BookDTO> booksObservableList;
    private TextField authorTextField;
    private TextField titleTextField;
    private TextField publishedDateTextField;
    private TextField stockTextField;
    private TextField priceTextField;
    private Label authorLabel;
    private Label titleLabel;
    private Label publishedDateLabel;
    private Label stockLabel;
    private Label priceLabel;
    private Button saveButton;
    private Button deleteButton;
    private Button sellButton;

    public BookView(Stage primaryStage, List<BookDTO> books) {
        primaryStage.setTitle("Library");
        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(books);
        initTableView(gridPane);

        initSaveOptions(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane) {
        bookTableView = new TableView<BookDTO>();
        bookTableView.setPlaceholder(new Label("No books to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<BookDTO, String> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<BookDTO, String> publishedDateColumn = new TableColumn<>("Published Date");
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        bookTableView.getColumns().addAll(titleColumn, authorColumn, priceColumn, stockColumn, publishedDateColumn);
        bookTableView.setItems(booksObservableList);
        gridPane.add(bookTableView, 0, 0, 6, 1);
    }

    private void initSaveOptions(GridPane gridPane) {
        titleLabel = new Label("Title");
        gridPane.add(titleLabel, 1, 1);
        titleTextField = new TextField();
        gridPane.add(titleTextField, 2, 1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel, 3, 1);
        authorTextField = new TextField();
        gridPane.add(authorTextField, 4, 1);

        publishedDateLabel = new Label("Published Date");
        gridPane.add(publishedDateLabel, 1, 2);
        publishedDateTextField = new TextField();
        publishedDateTextField.setPromptText("YYYY-MM-DD");
        gridPane.add(publishedDateTextField, 2, 2);

        stockLabel = new Label("Stock");
        gridPane.add(stockLabel, 3, 2);
        stockTextField = new TextField();
        gridPane.add(stockTextField, 4, 2);

        priceLabel = new Label("Price");
        gridPane.add(priceLabel, 1, 3);
        priceTextField = new TextField();
        gridPane.add(priceTextField, 2, 3);

        saveButton = new Button("Save");
        gridPane.add(saveButton, 3, 3);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 4, 3);

        sellButton = new Button("Sell");
        gridPane.add(sellButton, 5, 3);
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener) {
        saveButton.setOnAction(saveButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addSaleButtonListener(EventHandler<ActionEvent> sellButtonListener) {
        sellButton.setOnAction(sellButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public String getTitle() {
        return titleTextField.getText();
    }

    public String getAuthor() {
        return authorTextField.getText();
    }

    public String getPublishedDate() {
        return publishedDateTextField.getText();
    }

    public String getStock() {
        return stockTextField.getText();
    }

    public String getPrice() {
        return priceTextField.getText();
    }

    public void addBookToObservableList(BookDTO bookDTO) {
        this.booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO) {
        this.booksObservableList.remove(bookDTO);
    }

    public TableView getBookTableView() {
        return bookTableView;
    }

    public BookDTO getBookFromObservableList(String title, String author, LocalDate publishedDate) {
        for (BookDTO book : booksObservableList) {
            if (book.getTitle().equals(title) &&
                    book.getAuthor().equals(author) &&
                    book.getPublishedDate().equals(publishedDate)) {
                return book; // Return the matching BookDTO
            }
        }
        return null; // Return null if no matching book is found
    }
}
