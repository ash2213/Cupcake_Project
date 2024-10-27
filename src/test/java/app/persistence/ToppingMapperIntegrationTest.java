package app.persistence;

import app.entities.Topping;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ToppingMapperIntegrationTest {

    private ConnectionPool connectionPool;
    private int testToppingId = 999;
    private String testFlavor = "Chocolate";
    private double testPrice = 0.5;

    @BeforeAll
    public void setUp() throws SQLException {
        connectionPool = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=public", "cupcake");
    }

    @BeforeEach
    public void insertTestData() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            String insertSql = "INSERT INTO topping (topping_id, topping_flavor, price) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setInt(1, testToppingId);
                ps.setString(2, testFlavor);
                ps.setDouble(3, testPrice);
                ps.executeUpdate();
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            String deleteSql = "DELETE FROM topping WHERE topping_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setInt(1, testToppingId);
                ps.executeUpdate();
            }
        }
    }

    @Test
    public void testGetAllToppings() throws DatabaseException {
        List<Topping> toppings = ToppingMapper.getAllToppings(connectionPool);
        assertFalse(toppings.isEmpty(), "Toppings list should not be empty");

        Topping topping = toppings.stream().filter(t -> t.getToppingId() == testToppingId).findFirst().orElse(null);
        assertNotNull(topping, "Inserted test topping should be found in the list");
        assertEquals(testFlavor, topping.getToppingFlavor(), "Flavor should match the inserted topping's flavor");
        assertEquals(testPrice, topping.getPrice(), "Price should match the inserted topping's price");
    }

    @Test
    public void testGetToppingByIdSuccess() throws DatabaseException {
        Topping topping = ToppingMapper.getToppingById(testToppingId, connectionPool);
        assertNotNull(topping, "Topping should not be null when retrieving by valid ID");
        assertEquals(testFlavor, topping.getToppingFlavor(), "Flavor should match the retrieved topping's flavor");
        assertEquals(testPrice, topping.getPrice(), "Price should match the retrieved topping's price");
    }

    @Test
    public void testGetToppingByIdNonExistent() throws DatabaseException {
        int nonExistentId = 9999; // Assuming this ID does not exist in the database
        Topping topping = ToppingMapper.getToppingById(nonExistentId, connectionPool);
        assertNull(topping, "Topping should be null when retrieving with non-existent ID");
    }
}
