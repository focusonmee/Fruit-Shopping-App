package com.example.fruitshopping.adminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.R;

import java.util.List;

import adapter.OrderDetailManagementAdapter; // Sử dụng adapter mới
import helper.OrderDetailDBHelper;
import model.OrderDetail;

public class OrderDetailAdminActivity extends AppCompatActivity {
    private OrderDetailDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_admin); // Layout mới

        dbHelper = new OrderDetailDBHelper(this);

        // Lấy orderId từ Intent
        int orderId = getIntent().getIntExtra("orderId", -1); // Lấy orderId từ Intent

        // Khởi tạo ListView
        ListView lv = findViewById(R.id.lvadmin); // Sử dụng ID mới

        // Kiểm tra nếu orderId hợp lệ
        if (orderId != -1) {
            // Lấy danh sách chi tiết đơn hàng theo orderId
            List<OrderDetail> orderDetailList = dbHelper.getOrderDetailsByOrderId(orderId);

            if (orderDetailList != null && !orderDetailList.isEmpty()) {
                // Thiết lập adapter với danh sách chi tiết đơn hàng đã lấy
                OrderDetailManagementAdapter adapter = new OrderDetailManagementAdapter(this, orderDetailList);
                lv.setAdapter(adapter);
            } else {
                // Thông báo nếu không có chi tiết đơn hàng
                Toast.makeText(this, "Không có thông tin chi tiết cho đơn hàng này.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Xử lý trường hợp không có orderId hợp lệ
            Toast.makeText(this, "Không tìm thấy thông tin đơn hàng.", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc Activity nếu không có orderId hợp lệ
        }

        // Thiết lập sự kiện nhấn nút quay lại
        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(OrderDetailAdminActivity.this, UserManagementActivity.class);
                startActivity(intentBack);
            }
        });
    }
}
