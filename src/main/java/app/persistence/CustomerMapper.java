package app.persistence;
import app.entities.Customer;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper {

    public static Customer login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from \"customers\" where email = ? and password = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password);


                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("customer_id");
                    int balance = rs.getInt("balance");
                    return new Customer(id,email,password,balance);
                } else {
                    throw new DatabaseException("Fejl i login. Prøv igen");
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public static void createUser(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into customers (email, password) values (?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Fejl ved oprettelse af bruger");
                }

            }

        } catch (SQLException e) {
            String msg = "Fejl ved oprettelse af bruger";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg);
        }
    }



}
