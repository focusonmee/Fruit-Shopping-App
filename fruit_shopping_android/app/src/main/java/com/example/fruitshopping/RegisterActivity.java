package com.example.fruitshopping;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import helper.AccountDBHelper;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Set your actual layout here

        // Initialize UI components
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText phoneEditText = findViewById(R.id.phoneEditText); // Thêm trường số điện thoại
        EditText addressEditText = findViewById(R.id.addressEditText); // Thêm trường địa chỉ
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton); // Khởi tạo nút Login

        // Set up the register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim(); // Lấy số điện thoại
                String address = addressEditText.getText().toString().trim(); // Lấy địa chỉ
                int role_id = 2; // Thiết lập role_id là 2

                // Call the register method
                insertDatabase(email, password, name, phone, address, role_id);
            }
        });

        // Set up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại trang đăng nhập
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Kết thúc hoạt động này để không quay lại khi nhấn nút Back
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your action
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean check(String email, String password, String userName, String phone, String address) {
        // Check for empty fields
        if (email.isEmpty() || password.isEmpty() || userName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check password length
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Validate email format
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check if email already exists
        AccountDBHelper accountDBHelper = new AccountDBHelper(this);
        if (accountDBHelper.isAccountExist(email)) {
            Toast.makeText(this, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void insertDatabase(String email, String password, String userName, String phone, String address, int roleId) {
        if (check(email, password, userName, phone, address)) {
            AccountDBHelper accountDBHelper = new AccountDBHelper(this);
            boolean isInserted = accountDBHelper.addAccount(userName, phone, address, email, password, roleId);
            if (isInserted) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                // Redirect to login page
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // End this activity to prevent going back
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
