package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/specielcarport.html", ctx -> ctx.render("specielcarport.html"));
        app.post("/contactinfo.html", ctx -> ctx.render("contactinfo.html"));
        app.post("/order.html", ctx -> ctx.render("order.html"));
    }




}
