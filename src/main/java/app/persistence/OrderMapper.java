package app.persistence;

import app.entities.Base;
import app.entities.Order;
import app.entities.OrderLine;
import app.entities.Topping;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static int createOrder(int customerId, double totalPrice, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (customer_id, total_price, status, order_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP) RETURNING order_id";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, customerId);
                ps.setDouble(2, totalPrice);
                ps.setString(3, "Completed");

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new DatabaseException("Failed to create order.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database connection failed: " + e.getMessage());
        }
    }

    public static List<Order> getOrdersByCustomerId(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                double totalPrice = rs.getDouble("total_price");
                String status = rs.getString("status");
                Timestamp orderDate = rs.getTimestamp("order_date");

                Order order = new Order(orderId, customerId, totalPrice, status, orderDate);

                List<OrderLine> orderLines = OrderLineMapper.getOrderLinesByOrderId(orderId, connectionPool);
                order.setOrderLines(orderLines);

                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve orders: " + e.getMessage());
        }
        return orders;
    }
}