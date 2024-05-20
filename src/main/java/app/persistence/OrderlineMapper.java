package app.persistence;

import app.controllers.CarportController;
import app.entities.Orderline;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static app.persistence.CarportMapper.getOrderlines;

public class OrderlineMapper {


    public static void insertOrderline(int ordernumber, double width, double length, ConnectionPool connectionPool) {

        String sql = "INSERT INTO public.orderline(\n" +
                " user_number, orderline_price, quantity, product_name, product_description, order_number)\n" +
                "\tVALUES (?, ?, ?, ?, ?, ?);";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Loop through each order line
            for (Map.Entry<Integer, Orderline> entry : getOrderlines().entrySet()) {
                Orderline orderline = entry.getValue();
                // Set parameters for the prepared statement
                ps.setInt(1, User.getUsernumber());
                ps.setDouble(2, orderline.getPrice());
                ps.setInt(3, orderline.getQuantity());
                ps.setString(4, orderline.getName());
                ps.setString(5, orderline.getDescription());
                ps.setInt(6, ordernumber);
                // Add the parameters to the batch (for execution later)
                ps.addBatch();
            }
            // Execute the batch of insert statements
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Orderline> getOrderLinesByOrderNumber(int orderNumber, ConnectionPool connectionPool) throws DatabaseException {
        List<Orderline> orderLines = new ArrayList<>();

        String sql = "SELECT user_number, orderline_price, quantity, product_name, product_description, orderline_number, order_number FROM public.orderline WHERE order_number = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderNumber);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int user_number = rs.getInt("user_number");
                double orderline_price = rs.getDouble("orderline_price");
                int order_number = rs.getInt("order_number");
                String product_name = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                String product_description = rs.getString("product_description");
                int orderlineNumber = rs.getInt("orderline_number");

                Orderline orderLine = new Orderline(user_number, orderline_price, quantity, product_name, product_description, order_number);
                orderLines.add(orderLine);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in OrderLineMapper: " + e.getMessage());
        }
        return orderLines;
    }
}

