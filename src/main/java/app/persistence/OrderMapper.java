package app.persistence;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
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

    public static int insertOrder(double width, double length, ConnectionPool connectionPool) {
        String sql = "INSERT INTO public.\"order\"(\n" +
                "\t user_number, price, approved, width, length)\n" +
                "\tVALUES (?, ?, ?, ?, ?);";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, User.getUsernumber());
            ps.setDouble(2, CarportMapper.getTotalPrice());
            ps.setBoolean(3, false);
            ps.setDouble(4, width);
            ps.setDouble(5, length);

            ps.executeUpdate();

            // Get the generated order number
            ResultSet rs = ps.getGeneratedKeys();
            int orderNumber = 0;
            if (rs.next()) {
                orderNumber = rs.getInt(1);
            }
            OrderlineMapper.insertOrderline(orderNumber, width, length, connectionPool);

            return orderNumber;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
