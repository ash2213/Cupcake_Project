package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerMapperIntegrationTest {

    private ConnectionPool connectionPool;
    private String testEmail = "test@example.com";
    private String testPassword = "password123";
    private int balance= 500;

    @BeforeAll
    public void setUp() throws SQLException {
        connectionPool = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=public", "cupcake");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            String deleteSql = "DELETE FROM customers WHERE email = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setString(1, testEmail);
                ps.executeUpdate();
            }
        }
    }

    @Test
    public void testCreateUserSuccess() throws DatabaseException {
        CustomerMapper.createUser(testEmail, testPassword, connectionPool);

        Customer customer = CustomerMapper.login(testEmail, testPassword, connectionPool);
        assertNotNull(customer, "Customer should not be null after successful login");
        assertEquals(testEmail, customer.getEmail(), "Email should match the created user's email");
    }

    @Test
    public void testCreateUserDuplicate() {
        assertDoesNotThrow(() -> CustomerMapper.createUser(testEmail, testPassword, connectionPool));

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            CustomerMapper.createUser(testEmail, testPassword, connectionPool);
        });

        assertTrue(exception.getMessage().contains("E-mail already exists"), "Exception message should indicate duplicate user");
    }

    @Test
    public void testLoginSuccess() throws DatabaseException {
        CustomerMapper.createUser(testEmail, testPassword, connectionPool);
        Customer customer = CustomerMapper.login(testEmail, testPassword, connectionPool);
        assertNotNull(customer, "Customer should not be null after successful login");
        assertEquals(testEmail, customer.getEmail());
        assertEquals(testPassword, customer.getPassword());
    }

    @Test
    public void testLoginFailure() {
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            CustomerMapper.login("wrong@example.com", "wrongPassword", connectionPool);
        });

        assertEquals("Error in login. Try again", exception.getMessage());
    }
}
