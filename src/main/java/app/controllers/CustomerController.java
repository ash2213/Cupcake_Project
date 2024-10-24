package app.controllers;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import io.javalin.http.Context;


public class CustomerController {

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        try {
            Customer customer = CustomerMapper.login(email, password, connectionPool);

            ctx.sessionAttribute("currentUser", customer);

            ctx.sessionAttribute("userEmail", customer.getEmail());

            if (customer.isAdmin()) {
                ctx.redirect("adminOrderList.html");
            }
            else {

                ctx.sessionAttribute("currentUser", customer);
                ctx.sessionAttribute("customer_id", customer.getCustomer_id());
                ctx.redirect("/shopping");
            }

        } catch (DatabaseException e) {

            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }


    }

    public static void createCustomer(Context ctx, ConnectionPool connectionPool) {

        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        if (password1.equals(password2)) {


            try {
                CustomerMapper.createUser(email, password1, connectionPool);
                ctx.attribute("message", "You have successfully created a new customer");
                ctx.render("index.html");
            } catch (DatabaseException e) {

                ctx.attribute("message", e.getMessage());
                ctx.render("register.html");

            }
        } else {
            ctx.attribute("message", "Passwords do not match");
            ctx.render("register.html");
        }


    }


}
