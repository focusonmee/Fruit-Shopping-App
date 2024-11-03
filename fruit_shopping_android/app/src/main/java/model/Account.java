package model;

public class Account {
    private int id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String password;
    private int roleId; // Đảm bảo roleId là int
    private int isActive; // Sửa đổi tên biến để tuân theo quy tắc camelCase

    // Constructor
    public Account(int id, String name, String phone, String address, String email, String password, int roleId, int isActive) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
        this.roleId = roleId; // Thêm roleId
        this.isActive = isActive; // Thêm isActive
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getIsActive() {
        return isActive; // Getter cho isActive
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive; // Setter cho isActive
    }
}
