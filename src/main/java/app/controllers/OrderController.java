package app.controllers;
import app.entities.Order;
import app.entities.Orderline;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.OrderlineMapper;
import app.services.CarportSvg;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Locale;




public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
    app.get("/admin.html", ctx -> showAllOrders(ctx, connectionPool));
        app.post("/approve", ctx -> approveOrder(ctx, connectionPool));
        app.post("/order", ctx -> showMaterialList(ctx, connectionPool));
    }


    public static void showMaterialList(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int order_number = OrderMapper.getOrderNumber();
        List<Orderline> orderlines = OrderlineMapper.getOrderLinesByOrderNumber(order_number, connectionPool);
        ctx.attribute("orderlines", orderlines); // Pass order lines to the context
        ctx.attribute("ordernumber", order_number); // Pass order number to the context
        ctx.render("order.html");
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

    public static void showAllOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try
        {
            List<Order> orders = OrderMapper.getAllOrders(connectionPool);
            ctx.sessionAttribute("orders", orders);
            ctx.render("admin.html");
        } catch (DatabaseException e)
            {
                new DatabaseException("Fejl", e.getMessage());
            }
    }


    public static void approveOrder(Context ctx, ConnectionPool connectionPool) {
        try {
            int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));

            // Update the database to change the "accept" status of the order to true
            OrderMapper.approveOrder(connectionPool, orderNumber);

            // Redirect back to the admin page after approving the order
            ctx.redirect("/admin.html");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error approving order");
        }
    }

    public static void insertOrderline(Context ctx, ConnectionPool connectionPool){
        double width = CarportController.getWidth();
        double length = CarportController.getLength();

        OrderMapper.insertOrder(width, length, connectionPool);

    }

}
