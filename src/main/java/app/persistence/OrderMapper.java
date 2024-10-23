package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper {

    public static void createOrder(Order order, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (total_price, status, order_date, customer_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, order.getTotalPrice());
                ps.setString(2, order.getStatus());
                ps.setTimestamp(3, order.getOrderDate());
                ps.setInt(4, order.getCustomerId());

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected == 1) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        order.setOrderId(rs.getInt(1));
                    }
                } else {
                    throw new DatabaseException("Failed to create order");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database connection failed");
        }
    }
}