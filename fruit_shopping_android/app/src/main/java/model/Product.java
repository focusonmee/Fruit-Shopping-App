package model;

public class Product {
    private int id;
    private String name;
    private String description;
    private int categoryId;
    private double price;
    private String banner;
    private String image;

    public Product() {
    }

    // Constructor
    public Product(int id, String name, String description, int categoryId, double price, String banner, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.banner = banner;
        this.image = image;
    }

    // Getter and Setter methods
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
