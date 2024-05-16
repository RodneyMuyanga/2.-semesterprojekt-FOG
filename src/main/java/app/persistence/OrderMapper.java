package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT order_number, user_number, price, width, length FROM public.order WHERE approved = false";


        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int order_number = rs.getInt("order_number");
                int user_number = rs.getInt("order_number");
                double price = rs.getDouble("price");
                double width = rs.getDouble("width");
                double length = rs.getDouble("length");

                Order order = new Order(order_number, user_number, price, width, length);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i OrderMapper", e.getMessage());
        }
        return orders;
    }

    public static void approveOrder(ConnectionPool connectionPool, int orderNumber) {
        String sql = "UPDATE public.\"order\" SET approved = true WHERE order_number = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderNumber);
            int rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
