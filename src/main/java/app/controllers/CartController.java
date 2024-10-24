package app.controllers;

import app.entities.Base;
import app.entities.Order;
import app.entities.OrderLine;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import app.persistence.BaseMapper;
import app.persistence.OrderLineMapper;
import app.persistence.ToppingMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

import app.persistence.ConnectionPool;

public class CartController {
    public static void addItemToCart(Context ctx, ConnectionPool connectionPool) {

        try {
            int baseId = Integer.parseInt(ctx.formParam("base_id"));
            int toppingId = Integer.parseInt(ctx.formParam("topping_id"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));

            Base base = BaseMapper.getBaseById(baseId, connectionPool);
            Topping topping = ToppingMapper.getToppingById(toppingId, connectionPool);

            double price = (base.getPrice() + topping.getPrice()) * quantity;

            OrderLine orderLine = new OrderLine(base, topping, quantity, price);

            OrderLineMapper.createOrderLine(orderLine, connectionPool);

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

            ctx.attribute("bases", bases);
            ctx.attribute("toppings", toppings);

            ctx.render("shopping.html");

        } catch (DatabaseException e) {

            ctx.attribute("message", "Failed to load item available flavors");
            ctx.render("shopping.html");
        }
    }

    public static void showCart(Context ctx, ConnectionPool connectionPool) {

        List<OrderLine> cart = ctx.sessionAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        double totalPrice = 0;
        for (OrderLine orderLine : cart) {
            totalPrice += orderLine.getPrice();
        }

        ctx.sessionAttribute("cart", cart);
        ctx.attribute("cart", cart);
        ctx.attribute("totalPrice", totalPrice);

        ctx.render("cart.html");
    }
}
