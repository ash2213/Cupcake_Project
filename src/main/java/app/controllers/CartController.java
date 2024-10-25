package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.*;
import io.javalin.http.Context;
import app.persistence.OrderLineMapper;

import java.util.ArrayList;
import java.util.List;

public class CartController {
    public static void addItemToCart(Context ctx, ConnectionPool connectionPool) {

        try {
            int customerId = ctx.sessionAttribute("customer_id");

            int baseId = Integer.parseInt(ctx.formParam("base_id"));
            int toppingId = Integer.parseInt(ctx.formParam("topping_id"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));

            Base base = BaseMapper.getBaseById(baseId, connectionPool);
            Topping topping = ToppingMapper.getToppingById(toppingId, connectionPool);

            double price = (base.getPrice() + topping.getPrice()) * quantity;

            OrderLine orderLine = new OrderLine(base, topping, quantity, price, customerId);

            OrderLineMapper.createOrderLine(orderLine, customerId, connectionPool);

            List<OrderLine> cart = ctx.sessionAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }

            cart.add(orderLine);
            ctx.sessionAttribute("cart", cart);

            ctx.redirect("/cart");

        } catch (DatabaseException e) {
            ctx.attribute("message", "Failed to add item to cart.");
            ctx.render("/shopping");
        }
    }

    public static void showItemSelection(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Base> bases = BaseMapper.getAllBases(connectionPool);
            List<Topping> toppings = ToppingMapper.getAllToppings(connectionPool);


            String userEmail = ctx.sessionAttribute("userEmail");

            ctx.attribute("bases", bases);
            ctx.attribute("toppings", toppings);
            ctx.attribute("userEmail", userEmail);

            ctx.render("shopping.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", "Failed to load item available flavors");
            ctx.render("shopping.html");
        }
    }

    public static void showCart(Context ctx, ConnectionPool connectionPool) {

        int customer_id = ctx.sessionAttribute("customer_id");

        List<OrderLine> cart = null;
        try {

            if (cart == null) {
                cart = new ArrayList<>();
            }

            cart = OrderLineMapper.getOrderLine(customer_id, connectionPool);

            double totalPrice = 0;
            for (OrderLine orderLine : cart) {
                totalPrice += orderLine.getPrice();
            }

            ctx.attribute("cart", cart);
            ctx.attribute("totalPrice", totalPrice);

        } catch (DatabaseException e) {
            ctx.attribute("message", "Failed to load cart");
        }

        ctx.render("cart.html");

    }

    public static void showCheckoutPage(Context ctx, ConnectionPool connectionPool) {

        int customer_id = ctx.sessionAttribute("customer_id");

        try {

            List<OrderLine> cart = OrderLineMapper.getOrderLine(customer_id, connectionPool);

            if (cart == null || cart.isEmpty()) {
                ctx.attribute("message", "Your cart is empty. Please add items before checking out.");
                ctx.render("shopping.html");
                return;
            }

            double totalPrice = 0;
            for (OrderLine orderLine : cart) {
                totalPrice += orderLine.getPrice();
            }


            ctx.attribute("cart", cart);
            ctx.attribute("totalPrice", totalPrice);
            ctx.render("checkout.html");

        } catch (Exception e) {
            ctx.attribute("message", "Failed to load the checkout page.");
            ctx.render("cart.html");
        }
    }

    public static void removeOrderLine(Context ctx, ConnectionPool connectionPool) {

        try {

            int orderLineId = Integer.parseInt(ctx.formParam("order_line_id"));

            OrderLineMapper.deleteOrderLine(orderLineId, connectionPool);

            ctx.redirect("/cart");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Failed to remove item from cart");
            ctx.render("cart.html");
        }
    }

    public static void processCheckout(Context ctx, ConnectionPool connectionPool) {
        int customerId = ctx.sessionAttribute("customer_id");

        try {
            List<OrderLine> cartItems = OrderLineMapper.getOrderLine(customerId, connectionPool);
            if (cartItems == null || cartItems.isEmpty()) {
                ctx.attribute("message", "Your cart is empty.");
                ctx.render("cart.html");
                return;
            }

            double totalPrice = 0;
            for (OrderLine item : cartItems) {
                totalPrice += item.getPrice();
            }

            int orderId = OrderMapper.createOrder(customerId, totalPrice, connectionPool);

            OrderLineMapper.updateCartWithOrderId(customerId, orderId, connectionPool);

            ctx.attribute("message", "Order placed successfully!");
            ctx.redirect("/orders");

        } catch (DatabaseException e) {
            ctx.attribute("message", "Failed to complete checkout: " + e.getMessage());
            ctx.render("checkout.html");
        }
    }
}

