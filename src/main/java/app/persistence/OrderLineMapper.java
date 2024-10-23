package app.persistence;

import app.entities.OrderLine;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderLineMapper {

    public static void createOrderLine(OrderLine orderLine, int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO order_line (order_id, base_id, topping_id, quantity, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, orderId);
                ps.setInt(2, orderLine.getBase().getBaseId());
                ps.setInt(3, orderLine.getTopping().getToppingId());
                ps.setInt(4, orderLine.getQuantity());
                ps.setDouble(5, orderLine.getPrice());

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
