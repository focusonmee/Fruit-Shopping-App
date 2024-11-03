package com.example.fruitshopping.adminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.R;
import helper.CategoryDBHelper;
import model.Category;

public class AddCategory extends AppCompatActivity {

    private EditText categoryNameEditText;
    private Button saveCategoryButton, backButton;
    private CategoryDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Khởi tạo các view
        categoryNameEditText = findViewById(R.id.categoryNameEditText);
        saveCategoryButton = findViewById(R.id.saveCategoryButton);
        backButton = findViewById(R.id.back);
        dbHelper = new CategoryDBHelper(this);

        // Thiết lập sự kiện khi nhấn nút lưu
        saveCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        // Thiết lập sự kiện khi nhấn nút quay lại
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED); // Trả về mã không thành công
                finish(); // Đóng activity và quay lại activity trước đó
            }
        });
    }

    private void addCategory() {
        // Lấy tên loại sản phẩm từ EditText
        String categoryName = categoryNameEditText.getText().toString().trim();

        // Kiểm tra nếu tên loại sản phẩm không rỗng
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Tên loại sản phẩm không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo một đối tượng Category
        Category newCategory = new Category();
        newCategory.setName(categoryName);
        newCategory.setIsActive(1); // Mặc định là kích hoạt

        // Lưu loại sản phẩm vào cơ sở dữ liệu
        long result = dbHelper.addCategory(newCategory);
        if (result != -1) {
            Toast.makeText(this, "Thêm loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Trả về mã thành công
            finish(); // Đóng activity sau khi thêm thành công
        } else {
            Toast.makeText(this, "Thêm loại sản phẩm thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
