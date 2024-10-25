package app;

import app.config.ThymeleafConfig;
import app.controllers.CustomerController;
import app.controllers.OrderController;
import app.persistence.ConnectionPool;
import app.controllers.CartController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
            config.staticFiles.add("/templates");
        }).start(7070);

        // Routing

        app.get("/", ctx -> ctx.render("index.html"));
        app.post("/login", ctx -> CustomerController.login(ctx, connectionPool));
        app.get("/register",ctx -> ctx.render("register.html"));
        app.post("/register",ctx -> CustomerController.createCustomer(ctx,connectionPool));
        app.get("/shopping", ctx -> CartController.showItemSelection(ctx, connectionPool));
        app.post("/shopping", ctx -> CartController.addItemToCart(ctx, connectionPool));
        app.get("/cart", ctx -> CartController.showCart(ctx, connectionPool));
        app.get("/adminOrderList", ctx -> ctx.render("adminOrderList.html"));
        app.get("/checkout", ctx -> CartController.showCheckoutPage(ctx, connectionPool));
        app.post("/checkout", ctx -> CartController.processCheckout(ctx, connectionPool));
        app.post("/logout", ctx -> CustomerController.logout(ctx, connectionPool));
        app.post("/removeOrder", ctx -> CartController.removeOrderLine(ctx, connectionPool));
        app.get("/orders", ctx -> OrderController.showOrders(ctx, connectionPool));


    }

}