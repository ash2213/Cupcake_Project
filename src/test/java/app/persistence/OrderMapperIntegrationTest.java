package app.persistence;

import app.entities.Order;
import app.entities.OrderLine;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderMapperIntegrationTest {

    private ConnectionPool connectionPool;
    private int testCustomerId = 1;
    private double testTotalPrice = 200.0;
    private int orderId;

    @BeforeEach
    public void setUp() throws SQLException {
        connectionPool = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=public", "cupcake");

        try (Connection connection = connectionPool.getConnection()) {
            String insertCustomerSql = "INSERT INTO customers (customer_id, email, password) VALUES (?, 'test@example.com', '1234') ON CONFLICT DO NOTHING";
            try (PreparedStatement ps = connection.prepareStatement(insertCustomerSql)) {
                ps.setInt(1, testCustomerId);
                ps.executeUpdate();
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            String deleteOrderSql = "DELETE FROM orders WHERE customer_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteOrderSql)) {
                ps.setInt(1, testCustomerId);
                ps.executeUpdate();
            }

            String deleteCustomerSql = "DELETE FROM customers WHERE customer_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteCustomerSql)) {
                ps.setInt(1, testCustomerId);
                ps.executeUpdate();
            }
        }
    }


    @Test
    public void testCreateOrderSuccess() throws DatabaseException {
        orderId = OrderMapper.createOrder(testCustomerId, testTotalPrice, connectionPool);
        assertTrue(orderId > 0, "Order ID should be greater than 0 after order creation");

        List<Order> orders = OrderMapper.getOrdersByCustomerId(testCustomerId, connectionPool);
        assertFalse(orders.isEmpty(), "Order list should not be empty for the test customer");
        assertEquals(orderId, orders.get(0).getOrderId(), "Order ID should match the created order ID");
    }

    @Test
    public void testGetOrdersByCustomerId() throws DatabaseException {
        orderId = OrderMapper.createOrder(testCustomerId, testTotalPrice, connectionPool);

        List<Order> orders = OrderMapper.getOrdersByCustomerId(testCustomerId, connectionPool);
        assertFalse(orders.isEmpty(), "Orders list should not be empty for the test customer");

        Order retrievedOrder = orders.get(0);
        assertEquals(orderId, retrievedOrder.getOrderId(), "Order ID should match the created order ID");
        assertEquals(testTotalPrice, retrievedOrder.getTotalPrice(), "Total price should match the test total price");
        assertEquals("Completed", retrievedOrder.getStatus(), "Order status should be 'Completed'");

        for (OrderLine orderLine : retrievedOrder.getOrderLines()) {
            assertNotNull(orderLine, "Order lines should not contain null elements");
        }
    }

    @Test
    public void testCreateOrderFailure() {
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            OrderMapper.createOrder(-1, testTotalPrice, connectionPool);
        });
        assertTrue(exception.getMessage().contains("Failed to create order"), "Exception message should indicate failure to create order");
    }
}
