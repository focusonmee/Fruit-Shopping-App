package com.example.fruitshopping.UserActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fruitshopping.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.ProductAdapterConfirm;
import helper.OrderDBHelper;
import helper.OrderDetailDBHelper;
import model.Cart;
import model.CartItem;
import model.Order;

public class ConfirmProductActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, addressTextView, totalPriceTextView;
    private ListView productListView;
    private Button confirmButton;
    private ProductAdapterConfirm productAdapterConfirm;
    private float totalPrice;
    private Button backButton;
    private OrderDBHelper orderDBHelper;
    private OrderDetailDBHelper orderDetailDBHelper; // Thêm OrderDetailDBHelper
    private Spinner spinnerPaymentMethod;
    private Spinner spinnerShippingMethod;
    private EditText etNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_product);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Thiết lập tiêu đề cho Toolbar
        getSupportActionBar().setTitle("FruitShopping"); // Thiết lập tiêu đề cho toolbar

        // Thiết lập sự kiện click cho tiêu đề của Toolbar
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về HomeActivity
                Intent intent = new Intent(ConfirmProductActivity.this, ProductActivity.class);
                startActivity(intent);
                finish(); // Kết thúc ProfileActivity
            }
        });
        // Ánh xạ các view từ layout
        nameTextView = findViewById(R.id.name);
        phoneTextView = findViewById(R.id.phone);
        addressTextView = findViewById(R.id.address);
        totalPriceTextView = findViewById(R.id.totalPrice);
        productListView = findViewById(R.id.productListView);
        confirmButton = findViewById(R.id.confirmButton);
        backButton = findViewById(R.id.back);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);
        spinnerShippingMethod = findViewById(R.id.spinnerShippingMethod);
        etNote = findViewById(R.id.note);

        // Tạo DBHelper
        orderDBHelper = new OrderDBHelper(this);
        orderDetailDBHelper = new OrderDetailDBHelper(this); // Khởi tạo OrderDetailDBHelper

        // Lấy thông tin người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", -1);
        String userName = sharedPreferences.getString("name", "N/A");
        String phone = sharedPreferences.getString("phone", "N/A");
        String address = sharedPreferences.getString("address", "N/A");

        // Cập nhật giao diện
        nameTextView.setText("Tên người mua: " + userName);
        phoneTextView.setText("Số điện thoại: " + phone);
        addressTextView.setText("Địa chỉ: " + address);

        // Lấy danh sách sản phẩm từ giỏ hàng
        List<CartItem> cartItems = Cart.getItems();

        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng rỗng! Vui lòng thêm sản phẩm trước khi đặt hàng.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo adapter cho danh sách sản phẩm
        productAdapterConfirm = new ProductAdapterConfirm(this, cartItems);
        productListView.setAdapter(productAdapterConfirm);

        // Tính tổng tiền
        totalPrice = calculateTotalPrice(cartItems);
        totalPriceTextView.setText("Tổng tiền: "+formatCurrency(totalPrice)); // Đã sử dụng hàm formatCurrency

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartItems == null || cartItems.isEmpty()) {
                    Toast.makeText(view.getContext(), "Giỏ hàng rỗng! Không thể mua hàng.", Toast.LENGTH_SHORT).show();
                    return; // Dừng lại nếu giỏ hàng rỗng
                }

                String selectedPaymentMethod = spinnerPaymentMethod.getSelectedItem().toString();
                String selectedShippingMethod = spinnerShippingMethod.getSelectedItem().toString();
                String note = etNote.getText().toString().trim();

                // Tạo đối tượng Order
                Order order = new Order();
                order.setUserId(userId);
                order.setTotalMoney(totalPrice);
                order.setStatus("Đang xử lý");
                order.setNote(note);
                order.setShippingAddress(address);
                order.setShippingMethod(selectedShippingMethod);
                order.setPaymentMethod(selectedPaymentMethod);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = sdf.format(new Date());
                order.setOrderDate(currentDateTime);
                order.setIsActive(1);

                // Thêm đơn hàng vào DB và lấy orderId
                long newOrderId = orderDBHelper.addOrder(order.getUserId(), order.getTotalMoney(),
                        order.getStatus(), order.getNote(), order.getShippingAddress(),
                        order.getShippingMethod(), order.getPaymentMethod(), order.getIsActive(),
                        order.getOrderDate());

                if (newOrderId != -1) {
                    // Thêm các chi tiết đơn hàng
                    for (CartItem cartItem : cartItems) {
                        boolean isOrderDetailAdded = orderDetailDBHelper.addOrderDetail((int) newOrderId,
                                cartItem.getProduct().getId(), cartItem.getQuantity());

                        if (!isOrderDetailAdded) {
                            Toast.makeText(view.getContext(), "Lỗi khi thêm chi tiết đơn hàng.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    // Thông báo mua hàng thành công
                    Toast.makeText(view.getContext(), "Mua hàng thành công!", Toast.LENGTH_SHORT).show();
                    cartItems.clear();
                    Intent intent = new Intent(ConfirmProductActivity.this, ProductActivity.class); // Đổi tên lớp nếu cần
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(view.getContext(), "Lỗi khi thêm đơn hàng.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmProductActivity.this, ProductActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Tính tổng tiền từ các sản phẩm trong giỏ hàng
    private float calculateTotalPrice(List<CartItem> cartItems) {
        float total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    // Định dạng tiền tệ
    public static String formatCurrency(double amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return numberFormat.format(amount);
    }

    // Cập nhật tổng giá
    public void updateTotalPrice() {
        List<CartItem> cartItems = Cart.getItems();
        totalPrice = calculateTotalPrice(cartItems);
        totalPriceTextView.setText(formatCurrency(totalPrice)); // Đã sử dụng hàm formatCurrency
    }
    // Tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer, menu); // Thay thế bằng tên file menu của bạn
        return true;
    }

    // Xử lý sự kiện chọn menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Kiểm tra các mục menu
        if (id == R.id.menu_item1) {
            // Chuyển đến ProfileActivity
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item2) {
            // Nếu đang ở ConfirmProductActivity, không làm gì
            if (!(this instanceof ConfirmProductActivity)) {
                // Chuyển đến ConfirmProductActivity nếu không phải là trang hiện tại
                Intent intent = new Intent(this, ConfirmProductActivity.class);
                startActivity(intent);
            } else {
                // Nếu đang ở ConfirmProductActivity, có thể chỉ cần thông báo hoặc không làm gì
                Toast.makeText(this, "Bạn đang ở giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.menu_item3) {
            showPopupMenu(findViewById(R.id.menu_item3)); // Gọi hàm hiển thị PopupMenu
            return true;
        } else if (id == android.R.id.home) { // Xử lý sự kiện nút quay lại
            // Quay về Activity trước đó
            finish(); // Thay vì khởi tạo lại Activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // Phương thức hiển thị PopupMenu
    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor); // Truyền view gốc cho PopupMenu
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu()); // Inflate menu

        // Thiết lập sự kiện cho các mục trong PopupMenu
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.popup_item1) {
                // Chuyển đến OrderHistoryActivity
                Intent intent = new Intent(ConfirmProductActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;
            } else if (menuItem.getItemId() == R.id.popup_item2) {
                // Xóa phiên đăng nhập và trở về màn hình đăng nhập
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Xóa dữ liệu đăng nhập
                editor.apply(); // Áp dụng thay đổi

                Cart.clearItems(); // Xóa giỏ hàng

                Intent intent = new Intent(ConfirmProductActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Kết thúc Activity hiện tại
                return true;
            }
            return false;
        });

        // Hiển thị PopupMenu
        popupMenu.show();
    }
}
