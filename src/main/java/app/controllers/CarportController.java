package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/rooftype", ctx -> rooftype(ctx));
    }

    private static void rooftype(Context ctx) {
        String selectedRoof = ctx.formParam("roofs");
        if (selectedRoof != null) {
            if (selectedRoof.equals("flatroof")) {
                ctx.redirect("/specielcarportwithflatroof.html");
            } else if (selectedRoof.equals("highroof")) {
                ctx.redirect("/specielcarportwithhighroof.html");
            }
        } else {
            // Handle case where no roof type is selected
            ctx.result("Der er ikke valgt et tag");
        }
    }

}
