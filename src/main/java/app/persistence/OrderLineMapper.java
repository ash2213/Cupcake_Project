package app.persistence;

import app.entities.Base;
import app.entities.OrderLine;
import app.entities.Topping;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderLineMapper {

    public static void createOrderLine(OrderLine orderLine, int customer_id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO order_line (base_id, topping_id, quantity, price, customer_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, orderLine.getBase().getBaseId());
                ps.setInt(2, orderLine.getTopping().getToppingId());
                ps.setInt(3, orderLine.getQuantity());
                ps.setDouble(4, orderLine.getPrice());
                ps.setInt(5,orderLine.getCustomer_id());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Failed to create order line");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database connection failed");
        }
    }


    public static List<OrderLine> getOrderLine(int customer_id, ConnectionPool connectionPool) throws DatabaseException {

        List<OrderLine> orderLines= new ArrayList<>();

        String sql = "SELECT * FROM order_line WHERE customer_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, customer_id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int order_line_id = rs.getInt("order_line_id");
                    int base_id = rs.getInt("base_id");
                    int topping_id = rs.getInt("topping_id");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");


                    Base base = BaseMapper.getBaseById(base_id, connectionPool);
                    Topping topping = ToppingMapper.getToppingById(topping_id, connectionPool);

                    OrderLine orderLine = new OrderLine(order_line_id, base, topping, quantity, price, customer_id);
                    orderLines.add(orderLine);


                }
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to retrieve order lines for customer with ID");
        }
        return orderLines;
    }

    public static void deleteOrderLine(int order_line_id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM order_line WHERE order_line_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, order_line_id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DatabaseException("Order line with ID " + order_line_id + " not found.");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not delete order line with ID " + order_line_id);
        }
    }

}
