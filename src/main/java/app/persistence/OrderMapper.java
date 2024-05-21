package app.persistence;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    private static int order_number;
    private static double width;
    private static double length;

    public static int getOrderNumber(){
        return order_number;
    }

    public static double getWidth() {
        return width;
    }

    public static double getLength() {
        return length;
    }

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT order_number, user_number, price, width, length FROM public.order WHERE approved = false";


        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                order_number = rs.getInt("order_number");
                int user_number = rs.getInt("order_number");
                double price = rs.getDouble("price");
                width = rs.getDouble("width");
                length = rs.getDouble("length");

                double totalPrice = Math.round(price * 100.0) / 100.0;

                Order order = new Order(order_number, user_number, totalPrice, width, length, false);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i OrderMapper", e.getMessage());
        }
        return orders;
    }
    public static List<Order> getOrdersByUser(int userNumber, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT order_number, user_number, price, width, length, approved FROM public.order WHERE user_number = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                order_number = rs.getInt("order_number");
                int userNumberFromDb = rs.getInt("user_number");
                double price = rs.getDouble("price");
                width = rs.getDouble("width");
                length = rs.getDouble("length");
                boolean approved = rs.getBoolean("approved");

                double priceTwoDecimals = Math.round(price * 100.0) / 100.0;
                Order order = new Order(order_number, userNumberFromDb, priceTwoDecimals, width, length, approved);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in OrderMapper: " + e.getMessage());
        }
        return orders;
    }


    public static void approveOrder(ConnectionPool connectionPool, int orderNumber) throws DatabaseException {
        String sql = "UPDATE public.\"order\" SET approved = true WHERE order_number = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderNumber);
            int rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error in OrderMapper: " + e.getMessage());
        }
    }

    public static int insertOrder(double width, double length, ConnectionPool connectionPool) {
        String sql = "INSERT INTO public.\"order\"(\n" +
                "\t user_number, price, approved, width, length)\n" +
                "\tVALUES (?, ?, ?, ?, ?);";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            connection.setAutoCommit(false);  // Start transaction

            int userNumber = User.getUsernumber();
            double price = CarportMapper.getTotalPrice();
            boolean approved = false;

            ps.setInt(1, userNumber);
            ps.setDouble(2, price);
            ps.setBoolean(3, approved);
            ps.setDouble(4, width);
            ps.setDouble(5, length);

            ps.executeUpdate();

            // Get the generated order number
            ResultSet rs = ps.getGeneratedKeys();
            int orderNumber = 0;
            if (rs.next()) {
                orderNumber = rs.getInt(2);
            }

            OrderlineMapper.insertOrderline(orderNumber, width, length, connectionPool);

            connection.commit();  // Commit transaction
            connection.setAutoCommit(true);  // Reset auto-commit to true

            return orderNumber;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Order getOrderByOrdernumber(int order_number, ConnectionPool connectionPool) throws DatabaseException {

        Order order = null;

        String sql = "SELECT order_number, user_number, price, width, length, approved FROM public.order WHERE order_number= ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,order_number);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                order_number = rs.getInt("order_number");
                int userNumberFromDb = rs.getInt("user_number");
                double price = rs.getDouble("price");
                width = rs.getDouble("width");
                length = rs.getDouble("length");
                boolean approved = rs.getBoolean("approved");

                double priceTwoDecimals = Math.round(price * 100.0) / 100.0;
                order = new Order(order_number, userNumberFromDb, priceTwoDecimals, width, length, approved);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in OrderMapper: " + e.getMessage());
        }
        return order;
    }




}
