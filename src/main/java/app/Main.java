package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.CarportController;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fog";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // GET-ruter
        app.get("/", ctx -> ctx.render("index.html"));
        app.get("/specielcarport.html", ctx -> ctx.render("specielcarport.html"));
        app.get("/order.html", ctx -> ctx.render("order.html"));

        // POST-rute til beregning af carporten
        app.post("/calculate", ctx -> {
            // Udfør beregningerne
            CarportController.calculateCarport(ctx, connectionPool);

            // Omdiriger til "order" siden
            ctx.redirect("/order.html");
        });

        // Tilføj ruter fra CarportController
        CarportController.addRoutes(app, connectionPool);
    }
}
