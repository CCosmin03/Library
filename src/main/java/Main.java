import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;

import java.sql.Connection;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args){
        //System.out.println("Hello world!");

        BookRepository bookRepository=new BookRepositoryCacheDecorator(
            new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection()),
            new Cache<>()
        );
        BookService bookService=new BookServiceImpl(bookRepository);
        Connection connection=DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        RightsRolesRepository rightsRolesRepository=new RightsRolesRepositoryMySQL(connection);
        UserRepository userRepository=new UserRepositoryMySQL(connection, rightsRolesRepository);
        AuthenticationService authenticationService=new AuthenticationServiceImpl(userRepository,rightsRolesRepository);
        authenticationService.register("Cosmin","@Coso2404");
        System.out.println(authenticationService.login("Cosmin","@Coso2404"));
//        Book book=new BookBuilder()
//                .setTitle("Ion")
//                .setAuthor("Liviu Rebreanu")
//                .setPublishedDate(LocalDate.of(1910,10,20))
//                .build();
////        System.out.println(book);
////
////        BookRepository bookRepository=new BookRepositoryMock();
////        bookRepository.save(book);
////        bookRepository.save(new BookBuilder()
////                .setTitle("Moara cu Noroc")
////                .setAuthor("Ioan Slavici")
////                .setPublishedDate(LocalDate.of(1950,2,10))
////                .build());
////        System.out.println(bookRepository.findAll());
////        bookRepository.removeAll();
////        System.out.println(bookRepository.findAll());
//        //DatabaseConnectionFactory.getConnectionWrapper(false);
//        Connection connection=DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
//        BookRepository bookRepository=new BookRepositoryMySQL(connection);
//        BookService bookService=new BookServiceImpl(bookRepository);
//        bookService.save(book);
//        System.out.println(bookService.findAll());
//        Book bookMoaraCuNoroc=new BookBuilder()
//                .setTitle("Moara cu Noroc")
//                .setAuthor("Ioan Slavici")
//                .setPublishedDate(LocalDate.of(1950,2,10))
//                .build();
//        bookService.save(bookMoaraCuNoroc);
//        System.out.println(bookService.findAll());
//        bookService.delete(bookMoaraCuNoroc);
//        bookService.delete(book);
//        System.out.println(bookRepository.findAll());
    }
}
