package app.entities;

public class Carport {
    double carportWidth;
    double carportLength;
    int post;
    int strap;
    int rafterWood;

    public Carport(double carportWidth, double carportLength, int post, int strap, int rafterWood) {
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
        this.post = post;
        this.strap = strap;
        this.rafterWood = rafterWood;
    }
    public double getCarportWidth() {
        return carportWidth;
    }
    public void setCarportWidth(double carportWidth) {
        this.carportWidth = carportWidth;
    }
    public double getCarportLength() {
        return carportLength;
    }
    public void setCarportLength(double carportLength) {
        this.carportLength = carportLength;
    }
    public int getPost() {
        return post;
    }
    public void setPost(int post) {
        this.post = post;
    }
    public int getStrap() {
        return strap;
    }
    public void setStrap(int strap) {
        this.strap = strap;
    }
    public int getRafterWood() {
        return rafterWood;
    }
    public void setRafterWood(int rafterWood) {
        this.rafterWood = rafterWood;
    }
}
