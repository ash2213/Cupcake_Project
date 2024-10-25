package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper {

    public static Customer login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from customers where email = ? and password = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    boolean isAdmin = rs.getBoolean("is_admin");
                    int id = rs.getInt("customer_id");
                    int balance = rs.getInt("balance");
                    return new Customer(id, email, password, balance, isAdmin);
                } else {
                    throw new DatabaseException("Error in login. Try again");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createUser(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into customers (email, password, balance) values (?, ?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password);
                ps.setInt(3, 500);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Error creating user");
                }
            }
        } catch (SQLException e) {
            String msg = "Error creating user";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "E-mail already exists";
            }
            throw new DatabaseException(msg);
        }

    }

    public static double getBalance(int customer_id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT balance FROM customers WHERE customer_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, customer_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                throw new DatabaseException("Customer not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve customer balance: " + e.getMessage());
        }
    }

    public static void updateCustomerBalance(int customer_id, double balance, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE customers SET balance = ? WHERE customer_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, balance);
            ps.setInt(2, customer_id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Failed to update customer balance.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update customer balance: " + e.getMessage());
        }
    }

}

