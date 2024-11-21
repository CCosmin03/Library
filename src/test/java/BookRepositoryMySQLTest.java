import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.book.BookRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMySQLTest {

    private static BookRepositoryMySQL bookRepository;

    @BeforeAll
    public static void setup() {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        bookRepository = new BookRepositoryMySQL(connection);
        bookRepository.removeAll();
    }

    @Test
    public void testSave() {
        Book book = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(LocalDate.of(1910, 10, 20))
                .build();

        assertTrue(bookRepository.save(book), "Expected book to be saved successfully");
    }

    @Test
    public void testFindAll() {
        bookRepository.removeAll();
        bookRepository.save(new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(LocalDate.of(1910, 10, 20))
                .build());

        List<Book> books = bookRepository.findAll();
        assertEquals(1, books.size(), "Expected one book in the database");
    }

    @Test
    public void testFindById() {
        bookRepository.removeAll();
        Book book = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(LocalDate.of(1910, 10, 20))
                .build();

        bookRepository.save(book);

        List<Book> books = bookRepository.findAll();
        assertEquals(1, books.size(), "Expected one book in the database");
        Long savedBookId = books.get(0).getId();

        Optional<Book> retrievedBook = bookRepository.findById(savedBookId);
        assertTrue(retrievedBook.isPresent(), "Expected book to be found by ID");
    }

    @Test
    public void testDelete() {
        bookRepository.removeAll();
        Book book = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(LocalDate.of(1910, 10, 20))
                .build();

        bookRepository.save(book);
        assertTrue(bookRepository.delete(book), "Expected book to be deleted successfully");
    }

    @Test
    public void testRemoveAll() {
        bookRepository.save(new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(LocalDate.of(1910, 10, 20))
                .build());

        bookRepository.removeAll();
        List<Book> books = bookRepository.findAll();
        assertEquals(0, books.size(), "Expected no books in the database after removeAll");
    }
}
