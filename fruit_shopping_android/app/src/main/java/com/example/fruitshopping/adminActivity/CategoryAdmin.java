package com.example.fruitshopping.adminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.R;

import java.util.List;

import adapter.CategoryAdapter;
import helper.CategoryDBHelper;
import model.Category;

public class CategoryAdmin extends AppCompatActivity {

    private ListView categoryListView;
    private CategoryAdapter categoryAdapter;
    private Button addCategory,backButton;
    private CategoryDBHelper dbHelper; // Di chuyển dbHelper ra ngoài để dễ quản lý

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_admin);
        backButton = findViewById(R.id.backButton); // Khởi tạo nút thêm loại sản phẩm
        categoryListView = findViewById(R.id.categoryListView);
        addCategory = findViewById(R.id.addCategory); // Khởi tạo nút thêm loại sản phẩm
        dbHelper = new CategoryDBHelper(this);

        // Lấy dữ liệu từ database và gán cho adapter
        loadCategoryList();

        // Thiết lập sự kiện click cho nút thêm loại sản phẩm
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryAdmin.this, AddCategory.class);
                startActivityForResult(intent, 1); // Sử dụng startActivityForResult để nhận kết quả
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryAdmin.this, AdminActivity.class );
                startActivity(intent);
            }
        });
    }

    // Phương thức để tải danh sách danh mục
    private void loadCategoryList() {
        List<Category> categoryList = dbHelper.getAllCategories(); // Lấy danh sách danh mục từ cơ sở dữ liệu
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryListView.setAdapter(categoryAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // Kiểm tra mã yêu cầu
            if (resultCode == RESULT_OK) { // Kiểm tra kết quả
                loadCategoryList(); // Tải lại danh sách danh mục
            }
        }
    }


}
