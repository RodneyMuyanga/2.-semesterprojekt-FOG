package app.controllers;
import app.entities.Carport;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

import static app.controllers.OrderController.insertOrderline;

public class CarportController {
    private static int rafterWoodQuantity;
    private static int postQuantity;
    private static int strapQuantity;

    private static double width;
    private static double length;

    private static double totalPrice;

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/specielcarport.html", ctx -> ctx.render("specielcarport.html"));
        app.post("/orderconfirmation.html", ctx -> {
            if (userIsLoggedIn(ctx)) {
                ctx.render("orderconfirmation.html");
            } else {
                ctx.sessionAttribute("message", "Du skal være logget ind for at bestille");
                ctx.redirect("/login.html");
            }
            insertOrderline(ctx, connectionPool);
        });
        app.post("/payment.html", ctx -> createCarport(ctx, connectionPool));
    }


      public static void createCarport(Context ctx, ConnectionPool connectionPool) {
        //hent form parameter
        String carportwidth = ctx.formParam("carportwidth");
        String carportlength = ctx.formParam("carportlength");

        // Set parameters as session attributes
        ctx.sessionAttribute("carportwidth", carportwidth);
        ctx.sessionAttribute("carportlength", carportlength);

        width = Double.parseDouble(carportwidth);
        length= Double.parseDouble(carportlength);

            carportCalculater(carportlength, carportwidth, connectionPool, ctx);
            new Carport(width, length, getPostQuantity(), getStrapQuantity(), rafterWoodQuantity);
            OrderController.showOrder(ctx);
    }

        public static void carportCalculater(String carportlength, String carportwidth, ConnectionPool connectionPool, Context ctx) {

    // Convert string values to double
        double width = Double.parseDouble(carportwidth);
        double length = Double.parseDouble(carportlength);

        rafterWoodQuantity = (int) Math.round(length / 55.0);
        postQuantity = Math.max((int) Math.round(((length - 100) / 210.0))*2, 4); // Ensuring minimum of 4 posts
        strapQuantity = (int) Math.ceil((length / 360)*2);

        calculatePrice(width, length, connectionPool);
            ctx.sessionAttribute("totalPrice", totalPrice);
            ctx.render("/payment.html");
            ctx.render("createuser.html");

    }

    public static void calculatePrice(double width, double height, ConnectionPool connectionPool) {

        totalPrice = CarportMapper.calculateFinalPrice(width, height, CarportController.getRafterWoodQuantity(), CarportController.getPostQuantity(), CarportController.getStrapQuantity(), connectionPool);
        totalPrice = Math.round(totalPrice * 100.0) / 100.0;
    }
    private static boolean userIsLoggedIn(Context ctx) {
        return ctx.sessionAttribute("currentUser") instanceof User;
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

    public static double getWidth() {return width;}
    public static double getLength(){return length;}

}