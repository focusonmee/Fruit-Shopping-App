package com.example.fruitshopping.adminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.R;

import java.util.List;

import helper.CategoryDBHelper;
import helper.ProductDBHelper;
import model.Category;

public class AddProduct extends AppCompatActivity {

    private EditText nameEditText, priceEditText, bannerEditText, imageEditText, descriptionEditText;
    private Spinner categorySpinner;
    private Button addButton, backButton;
    private ProductDBHelper productDBHelper;
    private CategoryDBHelper categoryDBHelper;
    private List<Category> categoryList; // Danh sách các danh mục

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Khởi tạo các thành phần giao diện
        nameEditText = findViewById(R.id.nameEditText);
        priceEditText = findViewById(R.id.priceEditText);
        bannerEditText = findViewById(R.id.bannerEditText);
        imageEditText = findViewById(R.id.imageEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);

        // Khởi tạo các helper
        productDBHelper = new ProductDBHelper(this);
        categoryDBHelper = new CategoryDBHelper(this);

        // Lấy danh sách danh mục từ DB
        categoryList = categoryDBHelper.getAllCategories();

        // Tạo Adapter cho Spinner
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Thiết lập sự kiện chọn item cho Spinner
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Có thể xử lý sự kiện khi chọn danh mục ở đây nếu cần
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý trường hợp không có gì được chọn
            }
        });

        // Thiết lập sự kiện cho nút Thêm
        addButton.setOnClickListener(v -> addProduct());

        // Thiết lập sự kiện cho nút Quay về
        backButton.setOnClickListener(v -> finish()); // Quay về activity trước
    }

    private void addProduct() {
        String name = nameEditText.getText().toString();
        String priceStr = priceEditText.getText().toString();
        String banner = bannerEditText.getText().toString();
        String image = imageEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Kiểm tra thông tin nhập vào
        if (name.isEmpty() || priceStr.isEmpty() || banner.isEmpty() || image.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int categoryId = categoryList.get(categorySpinner.getSelectedItemPosition()).getId(); // Lấy ID của danh mục được chọn

        // Lưu sản phẩm vào DB
        boolean result = productDBHelper.addProduct(name, price, banner, image, description, categoryId);
        if (result) {
            Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();

            // Gửi tín hiệu về ProductAdmin
            Intent intent = new Intent(); // Khởi tạo intent
            intent.putExtra("isProductAdded", true); // Gửi một tín hiệu cho biết sản phẩm đã được thêm
            setResult(RESULT_OK, intent); // Thiết lập kết quả cho Activity
            clearInputs(); // Xóa các ô nhập liệu
            finish(); // Đóng Activity hiện tại
        } else {
            Toast.makeText(this, "Thêm sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearInputs() {
        nameEditText.setText("");
        priceEditText.setText("");
        bannerEditText.setText("");
        imageEditText.setText("");
        descriptionEditText.setText("");
        categorySpinner.setSelection(0); // Đặt lại Spinner về lựa chọn đầu tiên
    }
}
