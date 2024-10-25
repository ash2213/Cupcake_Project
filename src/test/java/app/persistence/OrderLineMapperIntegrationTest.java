package app.persistence;

import app.entities.Base;
import app.entities.OrderLine;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderLineMapperIntegrationTest {

    private ConnectionPool connectionPool;
    private Base testBase;
    private Topping testTopping;
    private OrderLine testOrderLine;
    private int testCustomerId = 5;

    @BeforeAll
    public void setUp() throws SQLException {
        connectionPool = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=public", "cupcake");

        testBase = new Base(1, "Test Base", 5.0);
        testTopping = new Topping(1, "Test Topping", 1.0);
        testOrderLine = new OrderLine(testBase, testTopping, 2, 11.0, testCustomerId);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            String deleteSql = "DELETE FROM order_line WHERE customer_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setInt(1, testCustomerId);
                ps.executeUpdate();
            }
        }
    }

    @Test
    public void testCreateOrderLineSuccess() throws DatabaseException {
        OrderLineMapper.createOrderLine(testOrderLine, testCustomerId, connectionPool);

        List<OrderLine> orderLines = OrderLineMapper.getOrderLine(testCustomerId, connectionPool);
        assertFalse(orderLines.isEmpty(), "Order lines should not be empty after creation");
        assertEquals(1, orderLines.size(), "There should be one order line");
        OrderLine createdOrderLine = orderLines.get(0);
        assertEquals(testBase.getBaseId(), createdOrderLine.getBase().getBaseId());
        assertEquals(testTopping.getToppingId(), createdOrderLine.getTopping().getToppingId());
        assertEquals(testOrderLine.getQuantity(), createdOrderLine.getQuantity());
        assertEquals(testOrderLine.getPrice(), createdOrderLine.getPrice());
    }

    @Test
    public void testGetOrderLineEmpty() throws DatabaseException {
        List<OrderLine> orderLines = OrderLineMapper.getOrderLine(testCustomerId, connectionPool);
        assertTrue(orderLines.isEmpty(), "Order lines should be empty for a customer with no orders");
    }
}
