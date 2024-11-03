package com.example.fruitshopping.adminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.R;

import java.util.List;

import adapter.UserManagementAdapter;
import helper.AccountDBHelper;
import model.Account;

public class UserManagementActivity extends AppCompatActivity {
    private String loggedInAdminEmail; // Biến để lưu địa chỉ email của admin đang đăng nhập
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        // Lấy tham chiếu đến ListView
        ListView userListView = findViewById(R.id.user_list_view);

        // Khởi tạo DBHelper và lấy dữ liệu từ DB
        AccountDBHelper dbHelper = new AccountDBHelper(this);
        List<Account> userList = dbHelper.getAllUsers();

        // Giả sử bạn đã có phương thức để lấy email của admin đang đăng nhập
        loggedInAdminEmail = getLoggedInAdminEmail(); // Thay thế bằng phương thức thực tế của bạn

        // Tạo và thiết lập adapter với email của admin
        UserManagementAdapter adapter = new UserManagementAdapter(this, userList, loggedInAdminEmail);
        userListView.setAdapter(adapter);

        // Thiết lập sự kiện click cho nút quay lại
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserManagementActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    // Phương thức để lấy email của admin đang đăng nhập
    private String getLoggedInAdminEmail() {
        // Thực hiện lấy email của admin đang đăng nhập từ SharedPreferences hoặc một nơi nào đó
        // Đây chỉ là một ví dụ, bạn cần thay thế bằng cách thực tế của bạn
        return "a@example.com"; // Thay thế bằng giá trị thực tế
    }
}
