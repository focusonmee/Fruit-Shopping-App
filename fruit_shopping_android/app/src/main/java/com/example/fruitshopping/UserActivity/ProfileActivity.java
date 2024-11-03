package com.example.fruitshopping.UserActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.HomeActivity;
import com.example.fruitshopping.R;

import helper.AccountDBHelper;
import model.Cart;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, emailEditText, passwordEditText, newPasswordEditText;
    private Button editButton, saveButton, backButton, deleteButton;
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
        deleteButton = findViewById(R.id.deleteButton);
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
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa tài khoản này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Lấy email từ SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
                                String email = sharedPreferences.getString("email", "");

                                // Gọi hàm deleteAccount trong DBHelper
                                dbHelper.deleteAccount(email);

                                // Hiển thị thông báo xóa thành công
                                Toast.makeText(ProfileActivity.this, "Tài khoản đã được xóa.", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                Cart.clearItems();

                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Hủy", null) // Đóng hộp thoại nếu nhấn "Hủy"
                        .show();
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

        // Validate dữ liệu trống
        if (newName.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra tên (chỉ chứa ký tự chữ cái, độ dài tối thiểu)
        if (!newName.matches("^[a-zA-Z\\s]+$") || newName.length() < 2) {
            Toast.makeText(this, "Tên chỉ được chứa ký tự chữ và phải có ít nhất 2 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng số điện thoại (chứa đúng 10 chữ số)
        if (!newPhone.matches("^[0-9]{10}$")) {
            Toast.makeText(this, "Số điện thoại phải chứa đúng 10 chữ số.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra địa chỉ (không quá dài và không chứa ký tự đặc biệt không hợp lệ)
        if (newAddress.length() > 100) {
            Toast.makeText(this, "Địa chỉ không được vượt quá 100 ký tự.", Toast.LENGTH_SHORT).show();
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
