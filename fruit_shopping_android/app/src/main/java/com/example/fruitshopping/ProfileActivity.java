package com.example.fruitshopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import helper.AccountDBHelper;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, emailEditText, passwordEditText, newPasswordEditText;
    private Button editButton, saveButton, backButton;
    private AccountDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ các view từ layout
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        editButton = findViewById(R.id.editButton);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.back);
        // Khởi tạo Database Helper
        dbHelper = new AccountDBHelper(this);

        // Lấy thông tin người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        // Hiển thị thông tin người dùng
        loadUserData(email);

        // Bật chỉnh sửa khi nhấn nút "Sửa thông tin"
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing(true);
            }
        });

        // Lưu thay đổi khi nhấn nút "Lưu thay đổi"
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserData(email);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });
    }

    private void loadUserData(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);

        String name = sharedPreferences.getString("name", "");
        String phone = sharedPreferences.getString("phone", "");
        String address = sharedPreferences.getString("address", "");
        String password = sharedPreferences.getString("password", "");

        // Đặt giá trị vào EditText
        nameEditText.setText(name);
        phoneEditText.setText(phone);
        addressEditText.setText(address);
        emailEditText.setText(email);
    }

    private void enableEditing(boolean isEnabled) {
        nameEditText.setEnabled(isEnabled);
        phoneEditText.setEnabled(isEnabled);
        addressEditText.setEnabled(isEnabled);
        emailEditText.setEnabled(false); // không cho phép chỉnh sửa email
        newPasswordEditText.setEnabled(isEnabled);
        saveButton.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
    }

    private void saveUserData(String email) {
        // Lấy dữ liệu hiện tại từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String savedName = sharedPreferences.getString("name", "");
        String savedPhone = sharedPreferences.getString("phone", "");
        String savedAddress = sharedPreferences.getString("address", "");
        String currentPassword = sharedPreferences.getString("password", "");

        // Lấy các giá trị mới từ EditText
        String newName = nameEditText.getText().toString().trim();
        String newPhone = phoneEditText.getText().toString().trim();
        String newAddress = addressEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();

        // Validate dữ liệu
        if (newName.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu hiện tại trước khi cho phép đổi mật khẩu
        if (!newPassword.isEmpty() && !currentPassword.equals(passwordEditText.getText().toString().trim())) {
            Toast.makeText(this, "Mật khẩu hiện tại không đúng.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu có mật khẩu mới, kiểm tra độ dài của mật khẩu
        if (!newPassword.isEmpty() && newPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải dài hơn 6 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin tài khoản trong cơ sở dữ liệu
        boolean isUpdated = dbHelper.updateAccount(
                newName.isEmpty() ? savedName : newName,
                newPhone.isEmpty() ? savedPhone : newPhone,
                newAddress.isEmpty() ? savedAddress : newAddress,
                email
        );

        // Nếu có mật khẩu mới, cập nhật trong cơ sở dữ liệu
        if (isUpdated && !newPassword.isEmpty()) {
            dbHelper.updatePassword(email, newPassword);
        }

        if (isUpdated) {
            // Cập nhật SharedPreferences sau khi thành công
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", newName.isEmpty() ? savedName : newName);
            editor.putString("phone", newPhone.isEmpty() ? savedPhone : newPhone);
            editor.putString("address", newAddress.isEmpty() ? savedAddress : newAddress);
            if (!newPassword.isEmpty()) {
                editor.putString("password", newPassword);
            }
            editor.apply(); // Lưu thay đổi

            Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
            enableEditing(false); // Vô hiệu hóa chỉnh sửa sau khi lưu
        } else {
            Toast.makeText(this, "Không có thay đổi nào để cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }


}
