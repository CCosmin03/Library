package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import view.BookView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class EmployeeComponentFactory {
    private final BookView bookView;
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static EmployeeComponentFactory instance;
    public static EmployeeComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage){
        if (instance==null){
            synchronized (EmployeeComponentFactory.class){
                if (instance==null){
                    instance=new EmployeeComponentFactory(componentsForTest,primaryStage);
                }
            }
        }
        return instance;
    }
    public EmployeeComponentFactory(Boolean componentsForTest, Stage primaryStage){
        Connection connection=DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository=new BookRepositoryMySQL(connection);
        this.bookService=new BookServiceImpl(bookRepository);
        List<BookDTO> books= BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.bookView=new BookView(primaryStage, books);
        this.bookController=new BookController(bookView,bookService);
    }

    public BookView getBookView() {
        return bookView;
    }

    public BookController getBookController() {
        return bookController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }
}

