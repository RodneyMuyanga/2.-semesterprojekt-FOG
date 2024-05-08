package app.entities;

public class User {

    private String name;
    private String adress;
    private int phonenumber;
    private int zipcode;
    private String email;

    public User(String name, String adress, int phonenumber, int zipcode, String email) {
        this.name = name;
        this.adress = adress;
        this.phonenumber = phonenumber;
        this.zipcode = zipcode;
        this.email = email;
    }

    public User() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
