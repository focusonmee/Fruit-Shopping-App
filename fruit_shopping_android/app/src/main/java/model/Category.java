package model;

public class Category {
    private int id;
    private String name;

    // Constructor
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
}
