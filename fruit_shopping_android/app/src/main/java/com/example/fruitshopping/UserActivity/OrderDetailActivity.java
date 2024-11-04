package com.example.fruitshopping.UserActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fruitshopping.R;
import adapter.OrderDetailAdapter;
import model.Cart;
import model.OrderDetail;
import helper.OrderDetailDBHelper;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private OrderDetailDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail_history);

        // Khởi tạo Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Kiểm tra xem ActionBar có khác null không
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút quay lại
        }
        getSupportActionBar().setTitle("FruitShopping"); // Thiết lập tiêu đề cho toolbar

        // Thiết lập sự kiện click cho tiêu đề của Toolbar
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về HomeActivity
                Intent intent = new Intent(OrderDetailActivity.this, ProductActivity.class);
                startActivity(intent);
                finish(); // Kết thúc ProfileActivity
            }
        });

        // Khởi tạo OrderDetailDBHelper
        dbHelper = new OrderDetailDBHelper(this);

        // Lấy orderId từ Intent
        int orderId = getIntent().getIntExtra("orderId", -1); // Ví dụ lấy orderId từ Intent

        // Kiểm tra nếu orderId hợp lệ
        if (orderId != -1) {
            // Lấy danh sách chi tiết đơn hàng theo orderId
            List<OrderDetail> orderDetailList = dbHelper.getOrderDetailsByOrderId(orderId);

            // Khởi tạo ListView
            ListView orderDetailListView = findViewById(R.id.orderDetailListView);

            // Thiết lập adapter với danh sách chi tiết đơn hàng đã lấy
            OrderDetailAdapter adapter = new OrderDetailAdapter(this, orderDetailList);
            orderDetailListView.setAdapter(adapter);
        } else {
            // Xử lý trường hợp không có orderId hợp lệ
            Toast.makeText(this, "Không tìm thấy thông tin đơn hàng.", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc Activity nếu không có orderId hợp lệ
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Gán file menu vào Activity
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Xử lý sự kiện khi một mục trong menu được chọn
        int id = item.getItemId();
        if (id == android.R.id.home) { // Xử lý sự kiện nút quay lại
            finish(); // Quay về Activity trước đó
            return true;
        } else if (id == R.id.menu_item1) {
            // Chuyển đến ProfileActivity
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item2) {
            // Chuyển đến ConfirmProductActivity
            Intent intent = new Intent(this, ConfirmProductActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item3) {
            showPopupMenu(findViewById(R.id.menu_item3)); // Gọi hàm hiển thị PopupMenu
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Phương thức hiển thị PopupMenu
    private void showPopupMenu(View anchor) {
        // Tạo một PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, anchor); // Truyền view gốc cho PopupMenu
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu()); // Inflate menu

        // Thiết lập sự kiện cho các mục trong PopupMenu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Xử lý các mục trong popup menu
                if (menuItem.getItemId() == R.id.popup_item1) {
                    // Chuyển đến OrderHistoryActivity
                    Intent intent = new Intent(OrderDetailActivity.this, OrderHistoryActivity.class);
                    startActivity(intent);
                    return true;
                } else if (menuItem.getItemId() == R.id.popup_item2) {
                    // Xóa phiên đăng nhập và trở về màn hình đăng nhập
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear(); // Xóa dữ liệu đăng nhập
                    editor.apply(); // Áp dụng thay đổi

                    Cart.clearItems(); // Xóa giỏ hàng

                    Intent intent = new Intent(OrderDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc Activity hiện tại
                    return true;
                }
                return false;
            }
        });

        // Hiển thị PopupMenu
        popupMenu.show();
    }
}
