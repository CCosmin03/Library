package view.model.builder;

import view.model.BookDTO;

import java.time.LocalDate;

public class BookDTOBuilder {
    private BookDTO bookDTO;

    public BookDTOBuilder() {
        bookDTO = new BookDTO();
    }

    public BookDTOBuilder setAuthor(String author) {
        bookDTO.setAuthor(author);
        return this;
    }

    public BookDTOBuilder setTitle(String title) {
        bookDTO.setTitle(title);
        return this;
    }

    public BookDTOBuilder setPublishedDate(LocalDate publishedDate) {
        bookDTO.setPublishedDate(publishedDate);
        return this;
    }

    public BookDTOBuilder setStock(int stock) {
        bookDTO.setStock(stock);
        return this;
    }

    public BookDTOBuilder setPrice(double price) {
        bookDTO.setPrice(price);
        return this;
    }

    public BookDTO build() {
        return bookDTO;
    }
}
