package repository.book;
import model.Book;

import java.time.LocalDate;
import java.util.*;
public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    boolean save (Book book);
    boolean delete (Book book);
    void removeAll();
    boolean updateStock(Book book, int newStock);
    Optional<Book> findByTitleAuthorPublishedDate(String title, String author, LocalDate publishedDate);

}
