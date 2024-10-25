package model;

public class Account {
    private String id, name, phone, address, email, password;

    public Account(String address, String email, String id, String name, String password, String phone) {
        this.address = address;
        this.email = email;
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
