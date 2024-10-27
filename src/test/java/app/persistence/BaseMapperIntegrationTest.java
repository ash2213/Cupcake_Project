package app.persistence;

import app.entities.Base;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseMapperIntegrationTest {

    private ConnectionPool connectionPool;
    private int testBaseId = 999;
    private String testFlavor = "Test Flavor";
    private double testPrice = 4.99;

    @BeforeAll
    public void setUp() throws SQLException {
        connectionPool = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=public", "cupcake");
    }

    @BeforeEach
    public void insertTestData() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            String insertSql = "INSERT INTO base (base_id, base_flavor, price) VALUES (?, ?, ?) " +
                    "ON CONFLICT (base_id) DO NOTHING";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setInt(1, testBaseId);
                ps.setString(2, testFlavor);
                ps.setDouble(3, testPrice);
                ps.executeUpdate();
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {

            String deleteOrderLineSql = "DELETE FROM order_line WHERE base_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteOrderLineSql)) {
                ps.setInt(1, testBaseId);
                ps.executeUpdate();
            }

            String deleteBaseSql = "DELETE FROM base WHERE base_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteBaseSql)) {
                ps.setInt(1, testBaseId);
                ps.executeUpdate();
            }
        }
    }

    @Test
    public void testGetAllBases() throws DatabaseException {
        List<Base> bases = BaseMapper.getAllBases(connectionPool);
        assertFalse(bases.isEmpty(), "Base list should not be empty");

        Base base = bases.stream().filter(b -> b.getBaseId() == testBaseId).findFirst().orElse(null);
        assertNotNull(base, "Inserted test base should be found in the list");
        assertEquals(testFlavor, base.getBaseFlavor(), "Flavor should match the inserted test base's flavor");
        assertEquals(testPrice, base.getPrice(), "Price should match the inserted test base's price");
    }

    @Test
    public void testGetBaseByIdSuccess() throws DatabaseException {
        Base base = BaseMapper.getBaseById(testBaseId, connectionPool);
        assertNotNull(base, "Base should not be null when retrieving by valid ID");
        assertEquals(testFlavor, base.getBaseFlavor(), "Flavor should match the retrieved base's flavor");
        assertEquals(testPrice, base.getPrice(), "Price should match the retrieved base's price");
    }

    @Test
    public void testGetBaseByIdNonExistent() throws DatabaseException {
        int nonExistentId = 9999;
        Base base = BaseMapper.getBaseById(nonExistentId, connectionPool);
        assertNull(base, "Base should be null when retrieving with non-existent ID");
    }
}
