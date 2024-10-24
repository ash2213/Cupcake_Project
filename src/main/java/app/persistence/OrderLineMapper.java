package app.persistence;

import app.entities.OrderLine;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderLineMapper {

    public static void createOrderLine(OrderLine orderLine, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO order_line (base_id, topping_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, orderLine.getBase().getBaseId());
                ps.setInt(2, orderLine.getTopping().getToppingId());
                ps.setInt(3, orderLine.getQuantity());
                ps.setDouble(4, orderLine.getPrice());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Failed to create order line");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database connection failed");
        }
    }
}
