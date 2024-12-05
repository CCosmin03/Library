package launcher;

import repository.order.OrderRepository;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepositoryMySQL;
import database.DatabaseConnectionFactory;

import java.sql.Connection;

public class UserOperationsComponentFactory {

    private static UserOperationsComponentFactory instance;
    private final RightsRolesRepository rightsRolesRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private static Boolean componentsForTests = false; // Default value to avoid null

    // Singleton method to get the instance
    public static UserOperationsComponentFactory getInstance(Boolean aComponentsForTests) {
        if (instance == null) {
            synchronized (UserOperationsComponentFactory.class) {
                if (instance == null) {
                    componentsForTests = aComponentsForTests != null ? aComponentsForTests : false;
                    instance = new UserOperationsComponentFactory(componentsForTests);
                }
            }
        }
        return instance;
    }
    private UserOperationsComponentFactory(Boolean componentsForTests) {

        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();

        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.orderRepository = new OrderRepositoryMySQL(connection);
    }

    public static Boolean getComponentsForTests() {
        return componentsForTests;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }
    public OrderRepository getOrderRepository() {
        return orderRepository;
    }
}
