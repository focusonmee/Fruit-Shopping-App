package model;

public class Category {
    private int id;
    private String name;
    private int isActive;

    public void setId(int id) {
        this.id = id;
    }

    public Category() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    // Constructor
    public Category(int id, String name, int isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getIsActive() {
        return isActive;
    }
    @Override
    public String toString() {
        return name; // Để hiển thị tên danh mục trong Spinner
    }
}
