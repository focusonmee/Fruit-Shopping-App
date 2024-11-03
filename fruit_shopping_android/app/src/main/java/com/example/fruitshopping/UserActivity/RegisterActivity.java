package com.example.fruitshopping.UserActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.R;

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
        EditText retypePasswordEditText = findViewById(R.id.retype_passwordEditText); // Add retype password field
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        EditText addressEditText = findViewById(R.id.addressEditText);
        Button registerButton = findViewById(R.id.registerButton);
        TextView loginTextView = findViewById(R.id.loginTextView); // Thay đổi từ Button thành TextView

        // Set up the register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String retypePassword = retypePasswordEditText.getText().toString().trim(); // Get retype password
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                int role_id = 2; // Set role_id to 2

                // Call the register method with retypePassword check
                if (check(email, password, retypePassword, name, phone, address)) {
                    insertDatabase(email, password, name, phone, address, role_id);
                }
            }
        });

        // Set up the login button click listener
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the login page
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finish this activity to prevent going back with the back button
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
                Toast.makeText(this, "Quyền bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean check(String email, String password, String retypePassword, String userName, String phone, String address) {
        // Check for empty fields
        if (email.isEmpty() || password.isEmpty() || retypePassword.isEmpty() || userName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Tất cả các trường đều bắt buộc.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check if passwords match
        if (!password.equals(retypePassword)) {
            Toast.makeText(this, "Mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check password length
        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Validate email format
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            Toast.makeText(this, "Định dạng email không hợp lệ.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Validate phone number (exactly 10 digits)
        String phonePattern = "^[0-9]{10}$"; // Exactly 10 digits
        Matcher phoneMatcher = Pattern.compile(phonePattern).matcher(phone);
        if (!phoneMatcher.matches()) {
            Toast.makeText(this, "Số điện thoại phải chứa đúng 10 chữ số.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check if email already exists
        AccountDBHelper accountDBHelper = new AccountDBHelper(this);
        if (accountDBHelper.isAccountExist(email)) {
            Toast.makeText(this, "Email đã tồn tại. Vui lòng sử dụng email khác.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void insertDatabase(String email, String password, String userName, String phone, String address, int roleId) {
        AccountDBHelper accountDBHelper = new AccountDBHelper(this);
        boolean isInserted = accountDBHelper.addAccount(userName, phone, address, email, password, roleId);
        if (isInserted) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            // Redirect to login page
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // End this activity to prevent going back
        } else {
            Toast.makeText(this, "Đăng ký không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }
}
