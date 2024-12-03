package repository.book;

import model.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{
    private final List<Book> books;

    public BookRepositoryMock() {
        books=new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream().
                filter(it->it.getId().equals(id))
                .findFirst();
        //faster when the list has a larger amount of elements
        //for less elements a for loop can be used;
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean delete(Book book) {return books.remove(book);}

    @Override
    public void removeAll() {books.clear();}

    @Override
    public boolean updateStock(Book book, int newStock) {
        for (Book existingBook : books) {
            if (existingBook.getTitle().equalsIgnoreCase(book.getTitle()) &&
                    existingBook.getAuthor().equalsIgnoreCase(book.getAuthor()) &&
                    existingBook.getPublishedDate().isEqual(book.getPublishedDate())) {
                existingBook.setStock(newStock); // Update the stock
                return true; // Return true if the book is found and stock is updated
            }
        }
        return false; // Return false if no matching book is found
    }


    @Override
    public Optional<Book> findByTitleAuthorPublishedDate(String title, String author, LocalDate publishedDate) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title) &&
                        book.getAuthor().equalsIgnoreCase(author) &&
                        book.getPublishedDate().isEqual(publishedDate))
                .findFirst();
    }
}
