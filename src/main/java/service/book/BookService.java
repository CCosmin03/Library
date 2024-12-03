package service.book;
import model.Book;

import java.time.LocalDate;
import java.util.*;
public interface BookService {
    List<Book> findAll();
    Book findById(Long id);
    boolean save(Book book);
    boolean delete(Book book);
    int getAgeOfBook(Long id);
    boolean updateStock(Book book, int newStock);
    Optional<Book> findByTitleAuthorPublishedDate(String title, String author, LocalDate publishedDate);


}
