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
                    return new Customer(id,email,password,balance,isAdmin);
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
                ps.setInt(3,500);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Error creating user");
                }

            }

        } catch (SQLException e) {
            String msg = "Error creating user";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "User already exists";
            }
            throw new DatabaseException(msg);
        }
    }



}
