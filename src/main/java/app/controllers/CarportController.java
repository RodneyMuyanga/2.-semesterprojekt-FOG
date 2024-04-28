package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/rooftype.html", ctx -> ctx.render("rooftype.html"));
        app.get("/specielcarportwithflatroof.html", ctx -> ctx.render("specielcarportwithflatroof.html"));
    }



}
