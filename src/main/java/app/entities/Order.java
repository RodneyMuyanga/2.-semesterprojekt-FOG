package app.entities;

public class Order {

    private int orderNumber;
    private int userNumber;
    private double price;
    private double width;
    private double length;

    private boolean approved;

    public Order(int orderNumber, int userNumber, double price, double width, double length, boolean approved) {
        this.orderNumber = orderNumber;
        this.userNumber = userNumber;
        this.price = price;
        this.width = width;
        this.length = length;
        this.approved = approved;
    }

    public boolean getApproved() {
        return approved;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public double getPrice() {
        return price;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }
}
