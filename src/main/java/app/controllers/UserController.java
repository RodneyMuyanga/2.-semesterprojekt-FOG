package app.controllers;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.SQLException;

import static app.controllers.CarportController.createCarport;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/index.html", ctx -> createuser(ctx, connectionPool));
        app.get("/login.html", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> login(ctx, connectionPool));
        app.get("/mypage.html", ctx -> ctx.render("mypage.html"));
        app.get("/index.html", ctx -> ctx.render("index.html"));
        app.get("/createuser.html", ctx -> ctx.render("createuser.html"));
       // app.post("/createuser.html", ctx -> createuser(ctx, connectionPool));
    }


        private static void createuser(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        // Retrieve form parameters
        String fullname = ctx.formParam("fullname");
        String adress = ctx.formParam("adress");
        String phonenumberStr = ctx.formParam("phonenumber");
        String zipcodeStr = ctx.formParam("zipcode");
        String emailCheck = ctx.formParam("email");
        String password = ctx.formParam("password");

            // Store form parameters in model attributes
            ctx.attribute("fullname", fullname);
            ctx.attribute("adress", adress);
            ctx.attribute("phonenumber", phonenumberStr);
            ctx.attribute("zipcode", zipcodeStr);
            ctx.attribute("email", emailCheck);
            ctx.attribute("password", password);

            String email;
            if (!(emailCheck.contains("@") && (emailCheck.contains(".com") || emailCheck.contains(".dk")))) {
                ctx.sessionAttribute("message", "Din mail er ikke korrekt");
                ctx.render("createuser.html");
                return;
            } else {
                email = emailCheck;
            }

        // Check if the input is empty or invalid
        if (phonenumberStr.isEmpty()) {
            ctx.sessionAttribute("message", "Du mangler at skrive dit mobilnummer");
            ctx.render("createuser.html");
            return;
        }

        if (phonenumberStr.length() != 8) {
            ctx.sessionAttribute("message", "Dit mobilnummer er ikke 8 cifre. Prøv igen.");
            ctx.render("createuser.html");
            return;
        }

        // Trim any leading or trailing whitespaces
        phonenumberStr = phonenumberStr.trim();
        int phonenumber;
        try {
            // Attempt to parse the string to an integer
            phonenumber = Integer.parseInt(phonenumberStr);
        } catch (NumberFormatException e) {
            ctx.sessionAttribute("message", "Dit mobilnummer må kun bestå af tal");
            ctx.render("createuser.html");
            return;
        }

               if (zipcodeStr.length() != 4) {
                ctx.sessionAttribute("message", "Postnummeret er ugyldigt. Prøv igen.");
                ctx.render("createuser.html");
                return;
            }
        int zipcode;
        try {
            // Attempt to parse the string to an integer
            zipcode = Integer.parseInt(zipcodeStr);
        } catch (NumberFormatException e) {
            ctx.render("createuser.html");
            ctx.sessionAttribute("message", "Postnummeret må kun bestå af tal");
            return; // Return early to prevent further processing
        }
        try {
            // Call UserMapper with all validated parameters
            UserMapper.createuser(fullname, adress, phonenumber, zipcode, email, password, connectionPool);
            login(ctx, connectionPool);
            ctx.redirect("/index.html");
        } catch (DatabaseException | SQLException e) {
            ctx.sessionAttribute("message", "Din email findes allerede, prøv igen eller log ind");
            ctx.render("createuser.html");
        }
    }


    public static void login(Context ctx, ConnectionPool connectionPool)  {

        //Get form parametre
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(email, password, connectionPool);
            if (user != null) {
            ctx.sessionAttribute("currentUser", user);
                ctx.sessionAttribute("message", null);
            ctx.redirect("/");
            } else {
                ctx.sessionAttribute("message", "Forkert brugernavn eller adgangskode. Prøv igen.");
                ctx.render("login.html");
            }
        } catch (DatabaseException e) {
            ctx.sessionAttribute("message", e.getMessage());
            ctx.render("index.html");
        }

    }

    }

