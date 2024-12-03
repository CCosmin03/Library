package mapper;

import model.Book;
import model.builder.BookBuilder;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {
    public static Book convertBookDTOToBook(BookDTO bookDTO) {
        return new BookBuilder()
                .setAuthor(bookDTO.getAuthor())
                .setTitle(bookDTO.getTitle())
                .setStock(bookDTO.getStock())
                .setPrice(bookDTO.getPrice())
                .setPublishedDate(bookDTO.getPublishedDate())
                .build();
    }

    public static BookDTO convertBookToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setStock(book.getStock());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setPublishedDate(book.getPublishedDate());
        return bookDTO;
    }
    public static List<BookDTO> convertBookListToBookDTOList(List<Book> books){
        return books.parallelStream().map(BookMapper::convertBookToBookDTO).collect(Collectors.toList());
    }
    public static List<Book> convertBookDTOListToBookList(List<BookDTO> bookDTOS){
        return bookDTOS.parallelStream().map(BookMapper::convertBookDTOToBook).collect(Collectors.toList());
    }
}
