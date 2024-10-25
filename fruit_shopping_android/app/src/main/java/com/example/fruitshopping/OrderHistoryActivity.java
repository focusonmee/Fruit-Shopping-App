package com.example.fruitshopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import adapter.OrderAdapter;
import helper.OrderDBHelper;
import model.Order;

public class OrderHistoryActivity extends AppCompatActivity {

    private ListView orderListView;
    private OrderAdapter orderAdapter;
    private OrderDBHelper orderDBHelper;
    private TextView userNameTextView; // Khai báo TextView
    private Button back;
    // Trong OrderUserViewComponent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        // Khởi tạo TextView
        userNameTextView = findViewById(R.id.userNameTextView); // Tìm kiếm TextView
        // Khởi tạo ListView
        orderListView = findViewById(R.id.orderListView);
        back = findViewById(R.id.back);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderHistoryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


    }

}
