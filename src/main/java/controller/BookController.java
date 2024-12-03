package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.Book;
import model.Order;
import model.User;
import service.book.BookService;
import service.order.OrderService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    private final OrderService orderService;
    private final User loggedUser;
    public BookController(BookView bookView, BookService bookService, OrderService orderService, User user){
        this.bookView=bookView;
        this.bookService=bookService;
        this.orderService=orderService;
        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSaleButtonListener(new SellButtonListener());
        this.loggedUser=user;
    }
    private class SaveButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            String publishedDate = bookView.getPublishedDate();
            String stock = bookView.getStock();
            String price = bookView.getPrice();

            if (title.isEmpty() || author.isEmpty() || publishedDate.isEmpty() || stock.isEmpty() || price.isEmpty()) {
                bookView.addDisplayAlertMessage("Save error",
                        "Missing fields",
                        "All fields (Title, Author, Published Date, Stock, Price) must be filled.");
                return;
            }

            try {
                LocalDate.parse(publishedDate);
            } catch (Exception e) {
                bookView.addDisplayAlertMessage("Save error",
                        "Invalid Date Format",
                        "Published Date must be in YYYY-MM-DD format.");
                return;
            }

            try {
                int stockValue = Integer.parseInt(stock);
                double priceValue = Double.parseDouble(price);

                BookDTO bookDTO = new BookDTOBuilder()
                        .setTitle(title)
                        .setAuthor(author)
                        .setPublishedDate(LocalDate.parse(publishedDate))
                        .setStock(stockValue)
                        .setPrice(priceValue)
                        .build();

                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));
                if (savedBook) {
                    bookView.addDisplayAlertMessage("Save successful",
                            "Book Added",
                            "Book was successfully added.");
                    bookView.addBookToObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Save error",
                            "Problem adding book",
                            "There was a problem adding the book to the database. Please try again.");
                }
            } catch (NumberFormatException e) {
                bookView.addDisplayAlertMessage("Save error",
                        "Invalid Stock or Price",
                        "Stock must be an integer, and Price must be a valid number.");
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            BookDTO bookDTO=(BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO!=null){
                boolean deletionSuccessful=bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));
                if (deletionSuccessful){
                    bookView.addDisplayAlertMessage("Delete successful","Book deleted","Book was successfully deleted.");
                    bookView.removeBookFromObservableList(bookDTO);
                }else{
                    bookView.addDisplayAlertMessage("Delete error","Problem at deleting book","There was a problem with the database. Please try again.");
                }
            }else{
                bookView.addDisplayAlertMessage("Delete error","Problem at deleting book","You must select a book before pressing the delete button");
            }
        }
    }
    private class SellButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null) {
                if (bookDTO.getStock() == 0) {
                    bookView.addDisplayAlertMessage("Not in stock","Problem at selling book","The chosen book is currently not in stock!");
                } else {
                    boolean sellSuccessful = bookService.updateStock(BookMapper.convertBookDTOToBook(bookDTO), bookDTO.getStock() - 1);

                    if (sellSuccessful) {
                        BookDTO bookInObservableList = bookView.getBookFromObservableList(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublishedDate());
                        bookInObservableList.setStock(bookInObservableList.getStock() - 1);
                        bookView.addDisplayAlertMessage("Successfully sold book","Book sold","Book was successfully sold from the library!");

                        Optional<Book> updatedBook = bookService.findByTitleAuthorPublishedDate(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublishedDate());
                        updatedBook.ifPresent(book -> orderService.save(new Order(null, loggedUser.getId(), book.getId(), LocalDateTime.now(), book.getPrice(), book.getStock())));
                    } else {
                        bookView.addDisplayAlertMessage("Selling error", "Problem at selling book", "There was a problem selling the selected book from the library!");
                    }
                }
            } else {
                bookView.addDisplayAlertMessage("Selling error", "Problem at selling book","You must select a book before pressing the Sell button!");
            }
        }
    }

}
