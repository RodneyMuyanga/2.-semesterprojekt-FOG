package app.controllers;

import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/specielcarport.html", ctx -> ctx.render("specielcarport.html"));
        app.post("/contactinfo.html", ctx -> ctx.render("contactinfo.html"));
        app.post("/order", ctx -> ctx.render("order.html"));
    }

    private static void createCarport(Context ctx, ConnectionPool connectionPool) {
        //hent form parameter
        String carportwidth = ctx.formParam("carportwidth");
        String carportlength = ctx.formParam("carportlength");

        try {
            carportCalculater (carportlength, carportwidth, connectionPool);

        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt, prøv igen.");
            ctx.render("specielcarport.html");
        }
    }
    public void carportCalculater(String carportlength, String carportwidth, ConnectionPool connectionPool) {
        // Convert string values to double
        double width = Double.parseDouble(carportwidth);
        double length = Double.parseDouble(carportlength);

        int rafterWoodQuantity = (int) Math.round(length / 55.0);
        int postQuantity = (int) Math.round((length - 200) / 310.0);
        int strapQuantity = (int) Math.ceil(length / 360);
    }

}
