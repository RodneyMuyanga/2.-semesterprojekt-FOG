package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/contactinfo.html",ctx -> createuser(ctx, connectionPool));
    }

    private static void createuser(Context ctx, ConnectionPool connectionPool)
    {
        //hent form parameter
        String fullname = ctx.formParam("fullname");
        String adress = ctx.formParam("adress");
        Integer phonenumber = Integer.valueOf(ctx.formParam("phonenumber"));
        Integer zipcode = Integer.valueOf(ctx.formParam("zipcode"));
        String email = ctx.formParam("email");

            try {
                UserMapper.createuser(fullname, adress, phonenumber, zipcode, email, connectionPool);

            } catch (DatabaseException e) {
                ctx.attribute("message", "Der skete en fejl, prøv igen");
                ctx.render("createuser.html");
            }

    }

}
