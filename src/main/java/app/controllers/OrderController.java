package app.controllers;
import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;
import java.util.List;

public class OrderController {

    public static void showOrders(Context ctx, ConnectionPool connectionPool) {

        int customerId = ctx.sessionAttribute("customer_id");
        String userEmail = ctx.sessionAttribute("userEmail");
        try {
            List<Order> orders = OrderMapper.getOrdersByCustomerId(customerId, connectionPool);

            ctx.attribute("orders", orders);
            ctx.attribute("userEmail", userEmail);
            ctx.render("orders.html");


        } catch (DatabaseException e) {
            ctx.attribute("message", "Failed to load orders: " + e.getMessage());
            ctx.render("orders.html");
        }
    }

    public static void showAllOrdersWithDetails(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Order> allOrders = OrderMapper.getAllOrdersWithDetails(connectionPool);

            System.out.println("Retrieved Orders: " + allOrders);

            ctx.attribute("orders", allOrders);
            ctx.render("adminOrderList.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", "Failed to load all orders: " + e.getMessage());
            ctx.render("index.html");
        }
    }
}