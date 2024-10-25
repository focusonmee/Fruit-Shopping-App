package model;

import dtos.ProductDTO;

public class CartItem { // Đảm bảo lớp này được khai báo là public
    private ProductDTO product; // Sản phẩm trong giỏ hàng
    private int quantity; // Số lượng của sản phẩm

    public CartItem(ProductDTO product, int quantity) {
        this.product = product; // Khởi tạo sản phẩm
        this.quantity = quantity; // Khởi tạo số lượng
    }

    public ProductDTO getProduct() {
        return product; // Trả về sản phẩm
    }

    public int getQuantity() {
        return quantity; // Trả về số lượng
    }
}
