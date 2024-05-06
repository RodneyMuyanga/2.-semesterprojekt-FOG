package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserMapper {


    public static void createuser(String name, String adress, int phonenumber, int zipcode, String email, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "INSERT INTO public.\"user\"(\n" +
                "user_name, user_adress, user_phonenumber, user_zipcode, user_email, role)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, name);
            ps.setInt(2, phonenumber);
            ps.setInt(3, zipcode);
            ps.setString(4, email);
            ps.setString(5, "user");


            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        }
        catch (SQLException e)
        {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
    }
}
