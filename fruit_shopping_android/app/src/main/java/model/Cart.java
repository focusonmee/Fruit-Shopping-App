package model;

import java.util.ArrayList;
import java.util.List;
import dtos.ProductDTO;

public class Cart {
    private static List<CartItem> items = new ArrayList<>();

    public static void addItem(ProductDTO product, int quantity) {
        items.add(new CartItem(product, quantity));
    }

    // Phương thức xóa sản phẩm khỏi giỏ hàng
    public static void removeItem(int productId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getId() == productId) {
                items.remove(i); // Xóa sản phẩm khỏi giỏ hàng
                break;
            }
        }
    }

    public static List<CartItem> getItems() {
        return items;
    }

    public static void clearCart() {
        items.clear();
    }
    public static void clearItems() {
        items.clear(); // Xóa tất cả sản phẩm trong danh sách
    }
}
