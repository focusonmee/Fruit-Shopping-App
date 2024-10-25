package model;

import java.util.List;
import java.util.Objects;

public class Order {
    private int orderId;
    private int userId;
    private float totalMoney;
    private String status;
    private String note;
    private String shippingAddress;  // Đã sửa lỗi chính tả
    private String shippingMethod;
    private String paymentMethod;
    private int isActive;
    private String orderDate;           // Ngày đặt hàng, lưu dưới dạng String
    private List<OrderDetail> orderDetails;  // Danh sách OrderDetail

    // Constructor không tham số
    public Order() {
    }

    // Constructor đầy đủ
    public Order(int orderId, int userId, float totalMoney, String status, String note,
                 String shippingAddress, String shippingMethod, String paymentMethod, int isActive,
                 String orderDate, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalMoney = totalMoney;
        this.status = status;
        this.note = note;
        this.shippingAddress = shippingAddress;
        this.shippingMethod = shippingMethod;
        this.paymentMethod = paymentMethod;
        this.isActive = isActive;
        this.orderDate = orderDate;    // Gán orderDate
        this.orderDetails = orderDetails;
    }

    // Getters và Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;  // Sửa lại kiểu dữ liệu để nhận String
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }


}
