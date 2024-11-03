package com.example.fruitshopping.adminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.R;

import java.util.List;

import adapter.OrderManagementAdapter;
import helper.OrderDBHelper;
import model.Order;

public class OrderAdminHistoryActivity extends AppCompatActivity {
    private TextView userNameTextView;
    private ListView orderListView;
    private Button back;
    private OrderDBHelper orderDBHelper;
    private OrderManagementAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_admin_history);

        // Ánh xạ các thành phần UI
        userNameTextView = findViewById(R.id.userNameTextView);
        orderListView = findViewById(R.id.orderListViewAdmin);
        back = findViewById(R.id.backButton);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        int userId = intent.getIntExtra("user_id", 0);
        String userName = intent.getStringExtra("name");

        // Khởi tạo OrderDBHelper và lấy danh sách đơn hàng
        orderDBHelper = new OrderDBHelper(this);
        List<Order> orders = orderDBHelper.getOrdersByUserId(userId);

        // Hiển thị tên người dùng
        userNameTextView.setText("Đây là những order của: " + userName);

        if (orders == null || orders.isEmpty()) {
            Toast.makeText(this, "Không có đơn hàng nào được tìm thấy.", Toast.LENGTH_SHORT).show();
        } else {
            // Khởi tạo OrderAdapter với danh sách orders
            orderAdapter = new OrderManagementAdapter(this, orders);

            // Thiết lập adapter cho ListView
            orderListView.setAdapter(orderAdapter);
        }

        // Xử lý sự kiện click cho nút back
        back.setOnClickListener(view -> {
            Intent intentBack = new Intent(OrderAdminHistoryActivity.this, UserManagementActivity.class);
            startActivity(intentBack);
        });
    }
}
