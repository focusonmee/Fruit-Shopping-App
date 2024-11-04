package com.example.fruitshopping.UserActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fruitshopping.R;
import java.util.List;

import adapter.OrderAdapter;
import helper.OrderDBHelper;
import model.Cart;
import model.Order;

public class OrderHistoryActivity extends AppCompatActivity {

    private ListView orderListView;
    private OrderAdapter orderAdapter;
    private OrderDBHelper orderDBHelper;
    private TextView userNameTextView; // Khai báo TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history); // Thiết lập layout trước khi gọi findViewById

        // Khởi tạo Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FruitShopping"); // Thiết lập tiêu đề cho toolbar

        // Thiết lập sự kiện click cho tiêu đề của Toolbar
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về HomeActivity
                Intent intent = new Intent(OrderHistoryActivity.this, ProductActivity.class);
                startActivity(intent);
                finish(); // Kết thúc ProfileActivity
            }
        });
        // Kiểm tra xem ActionBar có khác null không
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút quay lại
        }

        // Khởi tạo TextView
        userNameTextView = findViewById(R.id.userNameTextView); // Tìm kiếm TextView
        // Khởi tạo ListView
        orderListView = findViewById(R.id.orderListView);

        // Khởi tạo OrderDBHelper và lấy danh sách đơn hàng
        orderDBHelper = new OrderDBHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);

        int userId = sharedPreferences.getInt("id", 0); // Lấy userId từ SharedPreferences
        String userName = sharedPreferences.getString("name", "name"); // Lấy tên người dùng

        // Gọi phương thức để lấy danh sách đơn hàng của người dùng
        List<Order> orders = orderDBHelper.getOrdersByUserId(userId);

        // Hiển thị tên người dùng
        userNameTextView.setText("Đây là những order của: " + userName);

        // Khởi tạo OrderAdapter với danh sách orders
        orderAdapter = new OrderAdapter(OrderHistoryActivity.this, R.layout.order_item, orders);

        // Thiết lập adapter cho ListView
        orderListView.setAdapter(orderAdapter);
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
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menu_item2) {
            Intent intent = new Intent(this, ConfirmProductActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item3) {
            showPopupMenu(findViewById(R.id.menu_item3)); // Gọi hàm hiển thị PopupMenu
            return true;
        } else if (id == android.R.id.home) { // Xử lý sự kiện nút quay lại
            finish(); // Quay về Activity trước đó
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
                if (!(this instanceof OrderHistoryActivity)) {
                    // Chuyển đến ConfirmProductActivity nếu không phải là trang hiện tại
                    Intent intent = new Intent(this, OrderHistoryActivity.class);
                    startActivity(intent);
                } else {
                    // Nếu đang ở ConfirmProductActivity, có thể chỉ cần thông báo hoặc không làm gì
                    Toast.makeText(this, "Bạn đang ở trang lịch sử đơn hàng!", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (menuItem.getItemId() == R.id.popup_item2) {
                // Xóa phiên đăng nhập và trở về màn hình đăng nhập
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Xóa dữ liệu đăng nhập
                editor.apply(); // Áp dụng thay đổi

                Cart.clearItems(); // Xóa giỏ hàng

                Intent intent = new Intent(OrderHistoryActivity.this, LoginActivity.class);
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
