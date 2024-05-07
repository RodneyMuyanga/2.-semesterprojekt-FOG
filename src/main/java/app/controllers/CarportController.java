package app.controllers;

import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/calculate", ctx -> {
            // Udfør beregningerne
            calculateCarport(ctx, connectionPool);

            ctx.redirect("/order.html");
        });

        app.get("/specielcarport.html", ctx -> ctx.render("specielcarport.html"));
        app.post("/contactinfo.html", ctx -> ctx.render("contactinfo.html"));
        app.post("/payment.html", ctx -> ctx.render("payment.html"));
        app.post("/order.html", ctx -> ctx.render("order.html"));
        app.post("/order", ctx -> createCarport(ctx, connectionPool));
    }

    public static void createCarport(Context ctx, ConnectionPool connectionPool) {

        // Gem de valgte dimensioner på sessionen
        String carportWidth = ctx.formParam("carportwidth");
        String carportLength = ctx.formParam("carportlength");
        ctx.sessionAttribute("selectedWidth", carportWidth);
        ctx.sessionAttribute("selectedLength", carportLength);
    }

    public static void calculateCarport(Context ctx, ConnectionPool connectionPool) {
        String carportlength = ctx.formParam("carportlength");
        String carportWidth = ctx.formParam("carportwidth");
        String carportLength = ctx.formParam("carportlength");

        int rafterWoodQuantity = calculateRafterWood(Integer.parseInt(carportlength));
        int postQuantity = calculatePost(Integer.parseInt(carportlength));
        int strapQuantity = calculateStrap(Integer.parseInt(carportlength));

        // Gem udregningsresultaterne på sessionen
        ctx.sessionAttribute("selectedLength", carportLength);
        ctx.sessionAttribute("selectedWidth", carportWidth);
        ctx.sessionAttribute("rafterWoodQuantity", rafterWoodQuantity);
        ctx.sessionAttribute("postQuantity", postQuantity);
        ctx.sessionAttribute("strapQuantity", strapQuantity);
        // Omdiriger til ordresiden
        ctx.redirect("/order.html");
    }

    private static int calculateRafterWood(int length) {
        return (int) Math.round(length / 55.0);
    }

    private static int calculatePost(int length) {
        return (int) Math.round((length - 200) / 310.0);
    }

    private static int calculateStrap(int length) {
        return (int) Math.ceil(length / 360);
    }
}