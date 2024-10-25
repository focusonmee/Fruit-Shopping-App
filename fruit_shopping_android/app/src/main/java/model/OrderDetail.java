package model;

public class OrderDetail {
    private int orderDetailId; // ID duy nhất của chi tiết đơn hàng
    private int orderId;       // ID của đơn hàng
    private int productId;     // ID của sản phẩm
    private int quantity;      // Số lượng sản phẩm
    private String productName; // Tên sản phẩm
    private double productPrice; // Giá sản phẩm

    // Constructor
    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int quantity, int productId, int orderId) {
        this.orderDetailId = orderDetailId;
        this.quantity = quantity;
        this.productId = productId;
        this.orderId = orderId;
    }

    // Getter và Setter
    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}
