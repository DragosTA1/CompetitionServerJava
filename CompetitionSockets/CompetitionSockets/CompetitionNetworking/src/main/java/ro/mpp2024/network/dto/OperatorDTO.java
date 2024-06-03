package ro.mpp2024.network.dto;

import java.io.Serializable;

public class OperatorDTO implements Serializable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    private String username;
    private String password;
    private String email;
    private String city;

    public OperatorDTO() {
    }

    public OperatorDTO(int id, String username, String email, String password, String city) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.city = city;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
