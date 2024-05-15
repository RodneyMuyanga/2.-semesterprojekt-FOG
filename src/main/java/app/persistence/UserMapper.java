package app.persistence;
  
import app.entities.User;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserMapper {


    public static void createuser(String name, String adress, int phonenumber, int zipcode, String email, String password, ConnectionPool connectionPool) throws DatabaseException, SQLException {

                    String sql = "INSERT INTO public.\"user\"(\n" +
                            "user_name, user_adress, user_phonenumber, user_zipcode, user_email, role, password)\n" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);";

                    try (
                            Connection connection = connectionPool.getConnection();
                            PreparedStatement ps = connection.prepareStatement(sql)
                    ) {

                        ps.setString(1, name);
                        ps.setString(2, adress);
                        ps.setInt(3, phonenumber);
                        ps.setInt(4, zipcode);
                        ps.setString(5, email);
                        ps.setString(6, "user");
                        ps.setString(7, password);
                        login(name, password, connectionPool);


                        int rowsAffected = ps.executeUpdate();
                        if (rowsAffected != 1) {
                            throw new DatabaseException("Fejl da kontaktoplysninger skulle gemmes");
                        }
                    } catch (SQLException e) {
                        String msg = "Der er sket en fejl i UserMapper, createuser()";
                        throw new DatabaseException(msg, e.getMessage());
                    }
                }

        public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException
        {
            {
                String sql = "select * from public.\"user\" where user_email=? and password=?";

                try (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql)
                )
                {
                    ps.setString(1, email);
                    ps.setString(2, password);

                    ResultSet rs = ps.executeQuery();
                    if (rs.next())
                    {
                        int user_id = rs.getInt("user_id");
                        String user_name = rs.getString("user_name");
                        String user_adress = rs.getString("user_adress");
                        int user_phonenumber = rs.getInt("user_phonenumber");
                        int user_zipcode = rs.getInt("user_zipcode");
                        String role = rs.getString("role");
                        int user_number = rs.getInt("user_number");
                        return new User(user_id, user_name, user_adress, user_phonenumber, user_zipcode, email, role, user_number, password);
                    } else
                    {
                        return null; // Return null if user not found
                    }
                }
                catch (SQLException e)
                {
                    String msg = "Der er sket en fejl. Prøv igen";
                    if (e.getMessage().startsWith("ERROR: duplicate key value "))
                    {
                        msg = "Email findes allerede. Vælg et andet eller login";
                    }
                    throw new DatabaseException(msg, e.getMessage());
                }
            }
        }

    }

