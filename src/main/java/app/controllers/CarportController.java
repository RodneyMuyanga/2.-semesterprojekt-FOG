package app.controllers;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CarportController {
    private static int rafterWoodQuantity;
    private static int postQuantity;
    private static int strapQuantity;

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {

        app.post("/contactinfo.html", ctx -> {
            String carportWidth = ctx.formParam("carportwidth");
            String carportLength = ctx.formParam("carportlength");
            ctx.sessionAttribute("selectedWidth", carportWidth);
            ctx.sessionAttribute("selectedLength", carportLength);
            ctx.redirect("/contactinfo.html");
        });
        app.get("/specielcarport.html", ctx -> ctx.render("specielcarport.html"));
        app.get("/contactinfo.html", ctx -> ctx.render("contactinfo.html"));
        app.post("/order.html", ctx -> ctx.render("order.html"));
        app.post("/order", ctx -> createCarport(ctx, connectionPool));
    }



    private static void createCarport(Context ctx, ConnectionPool connectionPool) {
        //hent form parameter
        String carportwidth = ctx.formParam("carportwidth");
        String carportlength = ctx.formParam("carportlength");

        carportCalculater(carportlength, carportwidth, connectionPool);
    }

    public static void carportCalculater(String carportlength, String carportwidth, ConnectionPool connectionPool) {
        // Convert string values to double
        double width = Double.parseDouble(carportwidth);
        double length = Double.parseDouble(carportlength);

        rafterWoodQuantity = (int) Math.round(length / 55.0);
        postQuantity = Math.max((int) Math.round(((length - 100) / 210.0)*2), 4); // Ensuring minimum of 4 posts
        strapQuantity = (int) Math.ceil((length / 360)*2);
        //CarportMapper.insertOrderline(rafterWoodQuantity, postQuantity, strapQuantity, connectionPool);
    }

    public int getRafterWoodQuantity() {
        return rafterWoodQuantity;
    }

    public int getPostQuantity() {
        return postQuantity;
    }

    public int getStrapQuantity() {
        return strapQuantity;
    }
}