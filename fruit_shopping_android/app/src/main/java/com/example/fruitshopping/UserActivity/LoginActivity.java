package com.example.fruitshopping.UserActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.HomeActivity;
import com.example.fruitshopping.R;
import com.example.fruitshopping.adminActivity.AdminActivity;

import helper.AccountDBHelper;

public class LoginActivity extends AppCompatActivity {

    public static String currentEmail; // Khi đăng nhập, email sẽ được lưu vào biến này.
    private EditText editTextEmail; // Ánh xạ với @+id/editTextEmail
    private EditText editTextPassword; // Ánh xạ với @+id/editTextPassword
    private Button buttonLogin; // Ánh xạ với @+id/buttonLogin
    private TextView textSignUp; // Ánh xạ với @+id/textSignUp

    private AccountDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo biến
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textSignUp = findViewById(R.id.textSignUp);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (checkLogin(email, password).equals("fail")) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                } else {
                    currentEmail = checkLogin(email, password);
                    // Nếu đăng nhập thành công, chuyển đến màn hình chính hoặc màn hình quản trị viên.
                }
            }
        });

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent); // Bắt đầu hoạt động đăng ký
            }
        });
    }

//    private String checkLogin(String email, String password) {
//        // Kiểm tra email và password
//        if (email.isEmpty() || password.isEmpty()) {
//            return "fail";
//        }
//
//        this.dbHelper = new AccountDBHelper(this);
//        Cursor cursor = dbHelper.getAccountByEmailAndPassword(email, password);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            // Đăng nhập thành công
//            int idIndex = cursor.getColumnIndex("id");
//            int emailIndex = cursor.getColumnIndex("email");
//            int roleIndex = cursor.getColumnIndex("role_id");
//            int nameIndex = cursor.getColumnIndex("name");
//            int phoneIndex = cursor.getColumnIndex("phone");
//            int addressIndex = cursor.getColumnIndex("address");
//
//            if (idIndex == -1 || emailIndex == -1 || roleIndex == -1 || nameIndex == -1 || phoneIndex == -1 || addressIndex == -1) {
//                cursor.close();
//                return "fail"; // Trả về thất bại nếu có cột không tồn tại
//            }
//
//            int id = cursor.getInt(idIndex);
//            String fetchedEmail = cursor.getString(emailIndex);
//            String role = cursor.getString(roleIndex);
//            String userName = cursor.getString(nameIndex);
//            String phone = cursor.getString(phoneIndex);
//            String address = cursor.getString(addressIndex);
//
//            cursor.close(); // Đóng cursor
//
//            // Lưu thông tin vào SharedPreferences
//            SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//            editor.putInt("id", id); // Sửa đổi từ putString thành putInt
//            editor.putString("name", userName);
//            editor.putString("phone", phone);
//            editor.putString("address", address);
//            editor.putString("email", fetchedEmail);
//            editor.putString("password", password); // Cân nhắc mã hóa trước khi lưu
//            editor.putInt("role_id", Integer.parseInt(role)); // Lưu role_id
//            editor.apply(); // Lưu thay đổi
//
//            Intent intent;
//            if ("1".equals(role)) { // Nếu role = 1, điều hướng đến AdminActivity
//                intent = new Intent(LoginActivity.this, AdminActivity.class);
//            } else { // Nếu role khác, điều hướng đến HomeActivity
//                intent = new Intent(LoginActivity.this, HomeActivity.class);
//            }
//            startActivity(intent);
//            return fetchedEmail;
//        } else {
//            if (cursor != null) {
//                cursor.close();
//            }
//            return "fail";
//        }
//    }
private String checkLogin(String email, String password) {
    // Kiểm tra email và password
    if (email.isEmpty() || password.isEmpty()) {
        return "fail";
    }

    this.dbHelper = new AccountDBHelper(this);
    Cursor cursor = dbHelper.getAccountByEmailAndPassword(email, password);

    if (cursor != null && cursor.moveToFirst()) {
        // Lấy chỉ số của các cột
        int idIndex = cursor.getColumnIndex("id");
        int emailIndex = cursor.getColumnIndex("email");
        int roleIndex = cursor.getColumnIndex("role_id");
        int nameIndex = cursor.getColumnIndex("name");
        int phoneIndex = cursor.getColumnIndex("phone");
        int addressIndex = cursor.getColumnIndex("address");
        int isActiveIndex = cursor.getColumnIndex("is_active"); // Lấy chỉ số cho cột is_active

        // Kiểm tra nếu có cột không tồn tại
        if (idIndex == -1 || emailIndex == -1 || roleIndex == -1 || nameIndex == -1 || phoneIndex == -1 || addressIndex == -1 || isActiveIndex == -1) {
            cursor.close();
            return "fail"; // Trả về thất bại nếu có cột không tồn tại
        }

        int id = cursor.getInt(idIndex);
        String fetchedEmail = cursor.getString(emailIndex);
        String role = cursor.getString(roleIndex);
        String userName = cursor.getString(nameIndex);
        String phone = cursor.getString(phoneIndex);
        String address = cursor.getString(addressIndex);
        int isActive = cursor.getInt(isActiveIndex); // Lấy giá trị is_active

        cursor.close(); // Đóng cursor

        // Kiểm tra trạng thái is_active
        if (isActive == 0) {
            // Tài khoản bị khóa
            Toast.makeText(this, "Tài khoản bị khóa. Vui lòng liên hệ quản trị viên.", Toast.LENGTH_SHORT).show();
            return "fail"; // Trả về thất bại nếu tài khoản bị khóa
        }

        // Lưu thông tin vào SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", id); // Lưu id
        editor.putString("name", userName); // Lưu tên
        editor.putString("phone", phone); // Lưu số điện thoại
        editor.putString("address", address); // Lưu địa chỉ
        editor.putString("email", fetchedEmail); // Lưu email
        editor.putString("password", password); // Cân nhắc mã hóa trước khi lưu
        editor.putInt("role_id", Integer.parseInt(role)); // Lưu role_id
        editor.apply(); // Lưu thay đổi

        Intent intent;
        if ("1".equals(role)) { // Nếu role = 1, điều hướng đến AdminActivity
            intent = new Intent(LoginActivity.this, AdminActivity.class);
        } else { // Nếu role khác, điều hướng đến HomeActivity
            intent = new Intent(LoginActivity.this, HomeActivity.class);
        }
        startActivity(intent);
        return fetchedEmail;
    } else {
        if (cursor != null) {
            cursor.close();
        }
        return "fail"; // Nếu không tìm thấy tài khoản
    }
}



}

