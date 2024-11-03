package dtos;

import java.io.Serializable;

public class ProductDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price; // Đảm bảo kiểu dữ liệu là double
    private String image;
    private String banner;
    private int categoryId;
    private String categoryName; // Thêm thuộc tính categoryName

    // Constructor không tham số
    public ProductDTO() {}

    // Constructor với tất cả các tham số
    public ProductDTO(int id, String name, String description, double price, String image, String banner, int categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.banner = banner; // Khởi tạo banner
        this.categoryId = categoryId;
        this.categoryName = categoryName; // Khởi tạo categoryName
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBanner() {
        return banner; // Getter cho banner
    }

    public void setBanner(String banner) {
        this.banner = banner; // Setter cho banner
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName; // Getter cho categoryName
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName; // Setter cho categoryName
    }
}
