package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/specielcarport.html", ctx -> ctx.render("specielcarport.html"));
        app.post("/payment.html", ctx -> ctx.render("payment.html"));
        app.post("/order.html", ctx -> ctx.render("order.html"));
    }




}
