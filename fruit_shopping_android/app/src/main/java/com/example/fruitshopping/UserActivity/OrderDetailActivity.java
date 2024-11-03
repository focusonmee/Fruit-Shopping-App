package com.example.fruitshopping.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.HomeActivity;
import com.example.fruitshopping.R;

import adapter.OrderDetailAdapter;
import model.OrderDetail;
import helper.OrderDetailDBHelper;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private OrderDetailDBHelper dbHelper;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail_history);

        // Khởi tạo OrderDetailDBHelper
        dbHelper = new OrderDetailDBHelper(this);

        // Lấy orderId từ Intent
        int orderId = getIntent().getIntExtra("orderId", -1); // Ví dụ lấy orderId từ Intent

        // Khởi tạo nút quay lại
        back = findViewById(R.id.back); // Đảm bảo rằng bạn đã định nghĩa ID này trong layout XML

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

        // Thiết lập sự kiện nhấn nút quay lại
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Kết thúc Activity hiện tại sau khi quay lại
            }
        });
    }
}
