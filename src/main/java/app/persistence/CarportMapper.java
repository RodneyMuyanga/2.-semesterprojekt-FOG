package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CarportMapper {
    private static double totalPrice;

    public static double getTotalPrice() {
        return totalPrice;
    }

    public static double calculateFinalPrice(int rafterWoodQuantity, int postQuantity, int strapQuantity, ConnectionPool connectionPool) {
        totalPrice = 0;

        String sql = "SELECT name, price FROM public.material_list WHERE name IN (?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            // Set parameters for the SQL query
            ps.setString(1, "Rem");
            ps.setString(2, "Stolpe");
            ps.setString(3, "Spær");

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // Iterate through the results and calculate the total price
            while (rs.next()) {
                double price = rs.getDouble("price");
                // Multiply price by corresponding quantity and add to total price
                if (rs.getString("name").equals("Spær")) {
                    totalPrice += price * rafterWoodQuantity;
                } else if (rs.getString("name").equals("Stolpe")) {
                    totalPrice += price * postQuantity;
                } else if (rs.getString("name").equals("Rem")) {
                    totalPrice += price * strapQuantity;
                }
            }
                // Add profit margin of 40%
                 totalPrice *= 1.4;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalPrice;
    }
    public static void insertOrderline(Map<String, String> materialList, int orderId, ConnectionPool connectionPool) {
        String sql = "INSERT INTO orderline (orderlinieid, quantity, product_name, product_description) VALUES (?, ?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            for (Map.Entry<String, String> entry : materialList.entrySet()) {
                String materialName = entry.getKey();
                String[] materialInfo = entry.getValue().split("\\|");
                String description = materialInfo[0];
                int quantity = Integer.parseInt(materialInfo[1]);

                // Set parameters for the insert statement
                ps.setInt(1, orderId);
                ps.setString(2, materialName);
                ps.setString(3, description);
                ps.setInt(4, quantity);

                // Execute the insert statement
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            try {
                throw new DatabaseException("Fejl", e.getMessage());
            } catch (DatabaseException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // Method to get quantity based on material name
    private static String getQuantity(String materialName, int rafterWoodQuantity, int postQuantity, int strapQuantity) {
        // Determine quantity based on material name
        switch (materialName) {
            case "Spær":
                return String.valueOf(rafterWoodQuantity);
            case "Stolpe":
                return String.valueOf(postQuantity);
            case "Rem":
                return String.valueOf(strapQuantity);
            default:
                return "Unknown";
        }
    }


    public static Map<String, String> materialList(int rafterWoodQuantity, int postQuantity, int strapQuantity, ConnectionPool connectionPool) {
        Map<String, String> materialListMap = new LinkedHashMap<>();

        String sql = "SELECT name, description FROM public.material_list\n" +
                "ORDER BY name ASC";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                String materialInfo = description + "|" + getQuantity(name, rafterWoodQuantity, postQuantity, strapQuantity);
                materialListMap.put(name, materialInfo);
            }
        } catch (SQLException e) {
            try {
                throw new DatabaseException("Fejl", e.getMessage());
            } catch (DatabaseException ex) {
                throw new RuntimeException(ex);
            }
        }
        return materialListMap;
    }
}
