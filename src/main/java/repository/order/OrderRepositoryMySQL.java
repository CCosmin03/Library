package repository.order;

import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class OrderRepositoryMySQL implements OrderRepository{
    private Connection connection;
    public OrderRepositoryMySQL(Connection connection){
        this.connection=connection;
    }
    @Override
    public boolean save(Order order) {
        System.out.println("Order bookId: " + order.getBookId());
        System.out.println("Order userId: " + order.getUserId());
        System.out.println("Order getQuantity: " + order.getQuantity());
        System.out.println("Order price: " + order.getTotalPrice());
        System.out.println("Order date: " + order.getOrderDate());
        String sql = "INSERT INTO `order` VALUES (null, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, order.getBookId());
            preparedStatement.setLong(2, order.getUserId());
            preparedStatement.setInt(3, order.getQuantity());
            preparedStatement.setDouble(4, order.getTotalPrice());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(order.getOrderDate()));

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
