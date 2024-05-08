package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {


    public static void createuser(String name, String adress, int phonenumber, int zipcode, String email, ConnectionPool connectionPool) throws DatabaseException, SQLException {
        String selectSql = "SELECT user_number FROM public.\"user\" WHERE user_name = ? AND user_email = ?";
        String updateSql = "UPDATE public.\"user\" SET user_adress = ?, user_phonenumber = ?, user_zipcode = ? WHERE user_number = ?";


        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement selectPs = connection.prepareStatement(selectSql);
                PreparedStatement updatePs = connection.prepareStatement(updateSql)
        ) {
            // Check if the user already exists
            selectPs.setString(1, name);
            selectPs.setString(2, email);
            ResultSet resultSet = selectPs.executeQuery();

            if (resultSet.next()) {
                // User already exists, update their information
                int userNumber = resultSet.getInt("user_number");
                updatePs.setString(1, adress);
                updatePs.setInt(2, phonenumber);
                updatePs.setInt(3, zipcode);
                updatePs.setInt(4, userNumber);

                int rowsAffectedUpdate = updatePs.executeUpdate();
                if (rowsAffectedUpdate != 1) {
                    throw new DatabaseException("Fejl under opdatering af kontaktoplysninger");
                } else {
                    String sql = "INSERT INTO public.\"user\"(\n" +
                            "user_name, user_adress, user_phonenumber, user_zipcode, user_email, role)\n" +
                            "VALUES (?, ?, ?, ?, ?, ?);";

                    try (
                            PreparedStatement ps = connection.prepareStatement(sql)
                    ) {

                        ps.setString(1, name);
                        ps.setString(2, adress);
                        ps.setInt(3, phonenumber);
                        ps.setInt(4, zipcode);
                        ps.setString(5, email);
                        ps.setString(6, "user");


                        int rowsAffected = ps.executeUpdate();
                        if (rowsAffected != 1) {
                            throw new DatabaseException("Fejl da kontaktoplysninger skulle gemmes");
                        }
                    } catch (SQLException e) {
                        String msg = "Der er sket en fejl. Prøv igen";
                        throw new DatabaseException(msg, e.getMessage());
                    }
                }
            }
        }
    }
}