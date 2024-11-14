package repository;

import model.Book;

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
}
