package app.controllers;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Map;

public class CarportController {
    private static int rafterWoodQuantity;
    private static int postQuantity;
    private static int strapQuantity;

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/specielcarport.html", ctx -> ctx.render("specielcarport.html"));
        app.post("/order.html", ctx -> ctx.render("order.html"));
        app.post("/contactinfo", ctx -> createCarport(ctx, connectionPool));
    }

    private static void createCarport(Context ctx, ConnectionPool connectionPool) {
        //hent form parameter
        String carportwidth = ctx.formParam("carportwidth");
        String carportlength = ctx.formParam("carportlength");

        // Set parameters as session attributes
        ctx.sessionAttribute("carportwidth", carportwidth);
        ctx.sessionAttribute("carportlength", carportlength);

        carportCalculater(carportlength, carportwidth, connectionPool, ctx);
    }

        public static void carportCalculater(String carportlength, String carportwidth, ConnectionPool connectionPool, Context ctx) {
        // Convert string values to double
        double width = Double.parseDouble(carportwidth);
        double length = Double.parseDouble(carportlength);

        rafterWoodQuantity = (int) Math.round(length / 55.0);
        postQuantity = Math.max((int) Math.round(((length - 100) / 210.0)*2), 4); // Ensuring minimum of 4 posts
        strapQuantity = (int) Math.ceil((length / 360)*2);

        calculatePrice(connectionPool, ctx);
            ctx.render("contactinfo.html");
        //CarportMapper.insertOrderline(rafterWoodQuantity, postQuantity, strapQuantity, connectionPool);
    }

    public static void calculatePrice(ConnectionPool connectionPool, Context ctx) {

        double totalPrice = CarportMapper.calculateFinalPrice(CarportController.getRafterWoodQuantity(), CarportController.getPostQuantity(), CarportController.getStrapQuantity(), connectionPool);
        totalPrice = Math.round(totalPrice * 100.0) / 100.0;
        ctx.sessionAttribute("totalPrice", totalPrice);
        ctx.render("/payment.html");
    }


    public static int getRafterWoodQuantity() {
        return rafterWoodQuantity;
    }

    public static int getPostQuantity() {
        return postQuantity;
    }

    public static int getStrapQuantity() {
        return strapQuantity;
    }
}