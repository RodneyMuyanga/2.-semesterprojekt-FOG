package app.entities;

public class User {

    private String name;
    private String adress;
    private int phonenumber;
    private int zipcode;
    private String email;
    private String role;

    private String password;

    private static int usernumber;

    public User(int usernumber, String name, String adress, int phonenumber, int zipcode, String email, String role, int user_number, String password) {
        this.name = name;
        this.adress = adress;
        this.phonenumber = phonenumber;
        this.zipcode = zipcode;
        this.email = email;
        this.role = role;
        this.password = password;
        this.usernumber = usernumber;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public static int getUsernumber() { return usernumber;}
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

    public String getEmail() {
        return email;
    }


}
