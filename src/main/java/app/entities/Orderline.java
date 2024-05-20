package app.entities;

import java.util.Objects;

public class Orderline {

    private int user_number;
    private double price;
    private String name;
    private String description;
    private int quantity;

    public Orderline(int user_number, double price, int quantity, String name, String description) {
        this.user_number = user_number;
        this.price = price;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orderline orderline = (Orderline) o;
        return user_number == orderline.user_number && Double.compare(orderline.price, price) == 0 && quantity == orderline.quantity && Objects.equals(name, orderline.name) && Objects.equals(description, orderline.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_number, price, quantity, name, description);
    }

    public int getUser_number() {
        return user_number;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "name: " + name;
    }
}
