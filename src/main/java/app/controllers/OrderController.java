package app.controllers;
import app.persistence.ConnectionPool;
import app.services.CarportSvg;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Locale;




public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
    }


    public static void showOrder(Context ctx) {
        String carportWidth = ctx.sessionAttribute("carportwidth");
        String carportLength = ctx.sessionAttribute("carportlength");

        int width = Integer.parseInt(carportWidth);
        int length = Integer.parseInt(carportLength);

        Locale.setDefault(new Locale("US"));
        CarportSvg svg = new CarportSvg(length, width);

        ctx.sessionAttribute("svg", svg.toString());
        ctx.render("payment.html");
    }
}
