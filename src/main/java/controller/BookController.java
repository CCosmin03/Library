package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import service.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    public BookController(BookView bookView, BookService bookService){
        this.bookView=bookView;
        this.bookService=bookService;
        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
    }
    private class SaveButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            String title=bookView.getTitle();
            String author=bookView.getAuthor();
            if (title.isEmpty() || author.isEmpty()){
                bookView.addDisplayAlertMessage("Save error","Problem at author or title fields","Cannot have an empty title or author field");
            }else {
                BookDTO bookDTO=new BookDTOBuilder().setTitle(title).setAuthor(author).build();
                boolean savedBook=bookService.save(BookMapper.convertBookDTOToBook(bookDTO));
                if (savedBook){
                    bookView.addDisplayAlertMessage("Save successful","Book Added","Book was successfully added");
                    bookView.addBookToObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Save error","Problem at adding book","There was a problem with adding the book to the database. Please try again.");

                }
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
}
