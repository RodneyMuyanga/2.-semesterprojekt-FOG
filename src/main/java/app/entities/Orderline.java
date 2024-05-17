package app.entities;

public class Orderline {

    private static int user_number;
    private static double price;
    private static String name;
    private static String description;
    private static int quantity;
    public Orderline(int user_number, double price, int quantity, String name, String description) {
        this.user_number = user_number;
        this.price = price;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    public static int getUser_number() {
        return user_number;
    }

    public static double getPrice() {
        return price;
    }

    public static String getName() {
        return name;
    }

    public static String getDescription() {
        return description;
    }

    public static int getQuantity() {
        return quantity;
    }

    public static void setQuantity(int quantity) {
        Orderline.quantity = quantity;
    }

    @Override
    public String toString() {
        return "name: " + name;
    }
}
