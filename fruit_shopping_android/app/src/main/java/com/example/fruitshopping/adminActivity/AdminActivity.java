package com.example.fruitshopping.adminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.R;
import com.example.fruitshopping.UserActivity.LoginActivity;


public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Khởi tạo các nút
        Button productListButton = findViewById(R.id.productListButton);
        Button cartButton = findViewById(R.id.cartButton);
        Button categoryAdmin = findViewById(R.id.categoryAdmin);
        Button logoutButton = findViewById(R.id.logoutButton);
        // Thiết lập sự kiện click cho các nút
        productListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, UserManagementActivity.class);
                startActivity(intent);
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ProductAdmin.class);
                startActivity(intent);
            }
        });

        categoryAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, CategoryAdmin.class);
                startActivity(intent);
            }
        });



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // End this activity to prevent returning to it after logout
            }
        });
    }
}
