package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository{
    private Connection connection;
    public BookRepositoryMySQL(Connection connection){
        this.connection=connection;
    }
    @Override
    public List<Book> findAll() {
        String sql="SELECT * FROM book;";
        List<Book> books=new ArrayList<>();
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return books;
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(resultSet.getDate("publishedDate").toLocalDate())
                .setStock(resultSet.getInt("stock"))
                .setPrice(resultSet.getDouble("price"))
                .build();
    }


    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id=" + id;
        Optional<Book> book = Optional.empty();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                book = Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public boolean save(Book book) {
        String newSql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setInt(4, book.getStock());
            preparedStatement.setDouble(5, book.getPrice());
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean delete(Book book) {
        String newSql="DELETE FROM book WHERE author=\'"+book.getAuthor()+"\' AND title=\'"+book.getTitle()+"\';";
        try{
            Statement statement=connection.createStatement();
            statement.executeUpdate(newSql);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void removeAll() {
        String newSql="DELETE FROM book WHERE id>=0;";
        try{
            Statement statement=connection.createStatement();
            statement.executeUpdate(newSql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean updateStock(Book book, int newStock) {
        if (book == null || book.getTitle() == null || book.getAuthor() == null) {
            System.err.println("Error: Book, title, or author cannot be null.");
            return false;
        }

        String sql = "UPDATE book SET stock = ? WHERE title = ? AND author = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, newStock);
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthor());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated == 1; // Return true if exactly one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Book> findByTitleAuthorPublishedDate(String title, String author, LocalDate publishedDate) {
        String sql = "SELECT * FROM book WHERE title = ? AND author = ? AND publishedDate = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setDate(3, java.sql.Date.valueOf(publishedDate));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
