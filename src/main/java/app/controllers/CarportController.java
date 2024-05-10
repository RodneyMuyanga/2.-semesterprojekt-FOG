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
        app.post("/contactinfo.html", ctx -> ctx.render("contactinfo.html"));
        app.post("/order.html", ctx -> ctx.render("order.html"));
        app.post("/order", ctx -> createCarport(ctx, connectionPool));
        app.post("/payment", ctx -> calculatePrice(connectionPool, ctx));
    }

    private static void createCarport(Context ctx, ConnectionPool connectionPool) {
        //hent form parameter
        String carportwidth = ctx.formParam("carportwidth");
        String carportlength = ctx.formParam("carportlength");

            carportCalculater(carportlength, carportwidth, connectionPool);

        // Generer SVG med stolper og spær
        String svgWithRaftersAndPosts = generateRaftersAndPostsSVG(Double.parseDouble(carportlength), Double.parseDouble(carportwidth));

        // Tilføj SVG med stolper og spær til kontekst
        ctx.attribute("svgWithRaftersAndPosts", svgWithRaftersAndPosts);

        // Rendere din side med SVG
        ctx.render("payment.html");
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

    private static String generateRaftersAndPostsSVG(double carportLength, double carportWidth) {
        StringBuilder svgBuilder = new StringBuilder();

        // Constant values
        double rafterSpacing = 0.55; // Spacing between rafters (e.g., in meters)
        double maxPostSpacing = 2.10; // Maximum spacing between posts (e.g., in meters)
        double eavesToPost = 0.50; // Distance from eaves to the first post on both ends (e.g., in meters)

        // Calculate the number of rafters based on the carport length and desired rafter spacing
        int numberOfRafters = (int) Math.ceil(carportLength / rafterSpacing);

        // Calculate the spacing between posts based on the carport length
        double actualPostSpacing = carportLength / (numberOfRafters - 1);

        // Adjust the post spacing if it exceeds the maximum allowed spacing
        if (actualPostSpacing > maxPostSpacing) {
            actualPostSpacing = maxPostSpacing;
        }

        // Calculate the actual width between posts based on the actual number of posts
        double actualPostWidth = (numberOfRafters - 1) * actualPostSpacing;

        // Calculate the distance from the eaves to the first post on both ends
        double distanceToPost = (carportLength - actualPostWidth) / 2;

        // Draw the carport with posts and rafters
        svgBuilder.append("<svg viewBox=\"0 0 ").append(carportLength + 100).append(" 800\" preserveAspectRatio=\"xMinYMin\">");
        svgBuilder.append("<defs>...</defs>"); // Define markers, etc. as before
        svgBuilder.append("<line x1=\"50\" y1=\"730\" x2=\"50\" y2=\"0\" style=\"stroke:#000; marker-start:url(#a); marker-end:url(#b)\"/>");
        svgBuilder.append("<line x1=\"50\" y1=\"740\" x2=\"").append(carportLength + 50).append("\" y2=\"740\" style=\"stroke:#000; marker-start:url(#a); marker-end:url(#b)\"/>");
        svgBuilder.append("<svg x=\"50\" y=\"20\" width=\"").append(carportLength).append("\" height=\"700\" viewBox=\"0 0 ").append(carportLength).append(" 300\">");
        svgBuilder.append("<rect x=\"0\" y=\"0\" width=\"").append(carportLength).append("\" height=\"300\" fill=\"none\" stroke=\"#000\" stroke-width=\"1\"/>");

        // Draw posts
        double xPost = distanceToPost + 5; // Start position for the first post
        while (xPost < carportLength - distanceToPost) {
            svgBuilder.append("<rect x=\"").append(xPost).append("\" y=\"25\" width=\"15\" height=\"15\" fill=\"#fff\" stroke=\"#000\" stroke-width=\"1\"/>");
            xPost += actualPostSpacing;
        }

        // Draw rafters
        double xRafter = 25; // Start position for the first rafter
        for (int i = 0; i < numberOfRafters; i++) {
            svgBuilder.append("<line x1=\"").append(xRafter).append("\" y1=\"30\" x2=\"").append(xRafter).append("\" y2=\"285\" stroke=\"#000\" stroke-width=\"2\" stroke-dasharray=\"5,5\"/>");
            xRafter += rafterSpacing;
        }

        // Display carport width and length
        svgBuilder.append("<text x=\"").append(carportLength / 2).append("\" y=\"320\" style=\"text-anchor: middle;\">Width: ").append(carportWidth).append(" m</text>");
        svgBuilder.append("<text x=\"").append(carportLength / 2).append("\" y=\"340\" style=\"text-anchor: middle;\">Length: ").append(carportLength).append(" m</text>");

        // End SVG
        svgBuilder.append("</svg>");
        svgBuilder.append("</svg>");

        return svgBuilder.toString();
    }



    public static void calculatePrice(ConnectionPool connectionPool, Context ctx) {

        double totalPrice = CarportMapper.calculateFinalPrice(rafterWoodQuantity, postQuantity, strapQuantity, connectionPool);
        System.out.println("Total Price: " + totalPrice);
        ctx.sessionAttribute("totalPrice", totalPrice);
        ctx.render("payment.html", Map.of("totalPrice", totalPrice));

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