package app.controllers;

import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.services.CarportSvg;
import app.services.Svg;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Locale;
import java.util.Map;



public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/payment/calculate", ctx -> calculatePrice(connectionPool, ctx));
        app.post("/payment/showorder", ctx -> showOrder(ctx));
    }

    public static void calculatePrice(ConnectionPool connectionPool, Context ctx) {

        double totalPrice = CarportMapper.calculateFinalPrice(CarportController.getRafterWoodQuantity(), CarportController.getPostQuantity(), CarportController.getStrapQuantity(), connectionPool);
        System.out.println("Total Price: " + totalPrice);
        ctx.attribute("totalPrice", totalPrice);
        ctx.render("payment.html");

    }

    public static void showOrder(Context ctx) {

        Locale.setDefault(new Locale("US"));
        CarportSvg svg = new CarportSvg(600, 780);

        ctx.attribute("svg", svg.toString());
        ctx.render("payment.html");
    }
}
