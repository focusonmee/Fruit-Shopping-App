package com.example.fruitshopping.adminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.R;

import java.util.List;

import adapter.ProductManagementAdapter;
import dtos.ProductDTO;
import helper.ProductDBHelper;
import model.Product;

public class ProductAdmin extends AppCompatActivity {

    private ListView listView;
    private ProductManagementAdapter adapter;
    private List<ProductDTO> products;
    private ProductDBHelper dbHelper;
    private Button addProduct, backButton;

    private static final int REQUEST_CODE_ADD_PRODUCT = 1; // Mã yêu cầu cho AddProduct

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_admin);

        // Khởi tạo các thành phần giao diện
        addProduct = findViewById(R.id.addProduct);
        backButton = findViewById(R.id.backButton);

        // Khởi tạo DBHelper
        dbHelper = new ProductDBHelper(this);

        // Khởi tạo ListView
        listView = findViewById(R.id.productListView); // Đảm bảo bạn đã định nghĩa ListView trong layout

        // Lấy danh sách sản phẩm từ DB
        products = dbHelper.getAllProducts();

        // Khởi tạo và gán adapter
        adapter = new ProductManagementAdapter(this, products);
        listView.setAdapter(adapter);

        // Thiết lập sự kiện cho nút Thêm sản phẩm
        addProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductAdmin.this, AddProduct.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT); // Mở AddProduct và chờ tín hiệu trả về
        });

        // Thiết lập sự kiện cho nút Quay về
        backButton.setOnClickListener(v -> finish()); // Đóng Activity hiện tại
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_PRODUCT && resultCode == RESULT_OK) {
            // Nhận tín hiệu từ AddProduct
            boolean isProductAdded = data.getBooleanExtra("isProductAdded", false);
            if (isProductAdded) {
                // Làm mới danh sách sản phẩm trong ProductAdmin
                products = dbHelper.getAllProducts(); // Cập nhật danh sách sản phẩm
                adapter.updateProductList(products); // Cập nhật adapter
                Toast.makeText(this, "Sản phẩm mới đã được thêm!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
