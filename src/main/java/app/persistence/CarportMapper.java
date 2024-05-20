package app.persistence;

import app.entities.Orderline;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CarportMapper {
    private static double totalPrice;
    private static int counter = 0;
    private static Map<Integer, Orderline> orderlines;
    private static double rafterPrice;
    private static double postsPrice;
    private static double strapsPrice;
    private static String name;

    public static String getName() {
        return name;
    }

    public static double getRafterPrice() {
        return rafterPrice;
    }

    public static double getPostsPrice() {
        return postsPrice;
    }

    public static double getStrapsPrice() {
        return strapsPrice;
    }

    public static double getTotalPrice() {
        return totalPrice;
    }

    public static Map<Integer, Orderline> getOrderlines() {
        return orderlines;
    }

    public static double calculateFinalPrice(double width, double length, int rafterWoodQuantity, int postQuantity, int strapQuantity, ConnectionPool connectionPool) {
        totalPrice = 0;
        int count = 0;
        orderlines = new LinkedHashMap<>(); // Initialize orderlines map

        String sql = "SELECT name, price, description FROM public.material_list WHERE name IN (?, ?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Rem");
            ps.setString(2, "Stolpe");
            ps.setString(3, "Spær");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double price = rs.getDouble("price");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    int quantity = getQuantity(name, rafterWoodQuantity, postQuantity, strapQuantity);
                    double linePrice = 0;

                    if (name.equals("Spær")) {
                        linePrice = price * (rafterWoodQuantity * (width / 100));
                    } else if (name.equals("Stolpe")) {
                        linePrice = price * (postQuantity * 2.20);
                    } else if (name.equals("Rem")) {
                        linePrice = price * (strapQuantity * 3.60);
                    }

                    count++;

                    orderlines.put(count, new Orderline(User.getUsernumber(), linePrice, quantity, name, description));
                    totalPrice += linePrice;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Add profit margin of 40%
        totalPrice *= 1.4;

        return totalPrice;
    }

    // Method to get quantity based on material name
    private static int getQuantity(String materialName, int rafterWoodQuantity, int postQuantity, int strapQuantity) {
        // Determine quantity based on material name
        switch (materialName) {
            case "Spær":
                return rafterWoodQuantity;
            case "Stolpe":
                return postQuantity;
            case "Rem":
                return strapQuantity;
            default:
                return 0;
        }
    }
}
