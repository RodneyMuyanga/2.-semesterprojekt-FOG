package app.controllers;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.SQLException;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/payment.html", ctx -> createuser(ctx, connectionPool));
        app.get("/payment.html", ctx -> ctx.render("payment.html"));
    }

        private static void createuser(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        // Retrieve form parameters
        String fullname = ctx.formParam("fullname");
        String adress = ctx.formParam("adress");
        String phonenumberStr = ctx.formParam("phonenumber");
        String zipcodeStr = ctx.formParam("zipcode");
        String emailCheck = ctx.formParam("email");

            // Store form parameters in model attributes
            ctx.attribute("fullname", fullname);
            ctx.attribute("adress", adress);
            ctx.attribute("phonenumber", phonenumberStr);
            ctx.attribute("zipcode", zipcodeStr);
            ctx.attribute("email", emailCheck);

            String email;
            if (!(emailCheck.contains("@") && (emailCheck.contains(".com") || emailCheck.contains(".dk")))) {
                ctx.sessionAttribute("message", "Din mail er ikke korrekt");
                ctx.render("contactinfo.html");
                return;
            } else {
                email = emailCheck;
            }

        // Check if the input is empty or invalid
        if (phonenumberStr.isEmpty()) {
            ctx.sessionAttribute("message", "Du mangler at skrive dit mobilnummer");
            ctx.render("contactinfo.html");
            return;
        }

        if (phonenumberStr.length() != 8) {
            ctx.sessionAttribute("message", "Dit mobilnummer er ikke 8 cifre. Prøv igen.");
            ctx.render("contactinfo.html");
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
            ctx.render("contactinfo.html");
            return;
        }

               if (zipcodeStr.length() != 4) {
                ctx.sessionAttribute("message", "Postnummeret er ugyldigt. Prøv igen.");
                ctx.render("contactinfo.html");
                return;
            }
        int zipcode;
        try {
            // Attempt to parse the string to an integer
            zipcode = Integer.parseInt(zipcodeStr);
        } catch (NumberFormatException e) {
            ctx.render("contactinfo.html");
            ctx.sessionAttribute("message", "Postnummeret må kun bestå af tal");
            return; // Return early to prevent further processing
        }

        try {
            // Call UserMapper with all validated parameters
            UserMapper.createuser(fullname, adress, phonenumber, zipcode, email, connectionPool);
            ctx.redirect("/payment.html");
        } catch (DatabaseException | SQLException e) {
            ctx.attribute("message", "Der skete en fejl, prøv igen");
            ctx.render("contactinfo.html");
        }
    }
    
    


    }

