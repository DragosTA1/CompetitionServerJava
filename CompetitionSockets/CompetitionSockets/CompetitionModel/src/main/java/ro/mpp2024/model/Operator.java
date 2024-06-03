package ro.mpp2024.model;

public class Operator implements Identifiable<Integer> {
    private String username;
    private String password;
    private String email;
    private String city;
    private int id;

    public Operator(String username, String email, String password, String city) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.city = city;
    }

    public Operator() {
        this.username = "";
        this.password = "";
    }

    public Operator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Operator{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", id=" + id +
                '}';
    }
}
