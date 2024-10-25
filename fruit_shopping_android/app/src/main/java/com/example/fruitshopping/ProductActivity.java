//package com.example.fruitshopping;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ListView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//
//import adapter.ProductAdapter;
//import dtos.ProductDTO;
//
//import helper.ProductDBHelper;
//
//public class ProductComponent extends AppCompatActivity {
//
//    private ListView listView;
//    private ProductAdapter productAdapter;
//    private ProductDBHelper dbHelper;
//    private ArrayList<ProductDTO> productList;
//    private Button backButton;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_product);
//        backButton = findViewById(R.id.backButton);
//        // Khởi tạo ListView
//        listView = findViewById(R.id.lv);
//
//        // Khởi tạo DB helper
//        dbHelper = new ProductDBHelper(this);
//
//        // Lấy danh sách sản phẩm từ cơ sở dữ liệu
//        productList = dbHelper.getAllProducts();
//
//        // Khởi tạo và gán adapter cho ListView
//        productAdapter = new ProductAdapter(ProductComponent.this, R.layout.product_layout, productList);
//        listView.setAdapter(productAdapter);
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProductComponent.this, HomeComponent.class);
//                startActivity(intent);
//            }
//        });
//
//        // Thiết lập sự kiện nhấn vào từng sản phẩm
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ProductDTO selectedProduct = productList.get(position);
//
//                // Chuyển đến ProductDetailActivity với thông tin sản phẩm
//                Intent intent = new Intent(ProductComponent.this, ProductDetailComponent.class);
//                intent.putExtra("product", selectedProduct); // Gửi đối tượng sản phẩm
//                startActivity(intent);
//            }
//        });
//    }
//
//
//}
//
//
//
package com.example.fruitshopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import adapter.ProductAdapter;
import dtos.ProductDTO;
import helper.ProductDBHelper;

public class ProductActivity extends AppCompatActivity {

    private ListView listView;
    private ProductAdapter productAdapter;
    private ProductDBHelper dbHelper;
    private ArrayList<ProductDTO> productList;
    private ArrayList<ProductDTO> filteredList; // Danh sách sản phẩm đã lọc
    private Button backButton;
    private EditText searchEditText; // Thêm EditText cho tìm kiếm
    private Button searchButton; // Thêm Button cho tìm kiếm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        searchEditText = findViewById(R.id.searchEditText); // Khởi tạo EditText tìm kiếm
        searchButton = findViewById(R.id.searchButton); // Khởi tạo Button tìm kiếm
        backButton = findViewById(R.id.backButton);

        // Khởi tạo ListView
        listView = findViewById(R.id.lv);

        // Khởi tạo DB helper
        dbHelper = new ProductDBHelper(this);

        // Lấy danh sách sản phẩm từ cơ sở dữ liệu
        productList = dbHelper.getAllProducts();
        filteredList = new ArrayList<>(productList); // Khởi tạo danh sách đã lọc với tất cả sản phẩm

        // Khởi tạo và gán adapter cho ListView
        productAdapter = new ProductAdapter(ProductActivity.this, R.layout.product_item, filteredList);
        listView.setAdapter(productAdapter);

        // Sự kiện nhấn vào nút tìm kiếm
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchEditText.getText().toString().trim();
                searchProducts(query); // Gọi hàm tìm kiếm
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Thiết lập sự kiện nhấn vào từng sản phẩm
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductDTO selectedProduct = filteredList.get(position); // Lấy sản phẩm từ danh sách đã lọc

                // Chuyển đến ProductDetailActivity với thông tin sản phẩm
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", selectedProduct); // Gửi đối tượng sản phẩm
                startActivity(intent);
            }
        });
    }

    private void searchProducts(String query) {
        filteredList.clear(); // Xóa danh sách đã lọc

        // Kiểm tra từng sản phẩm trong danh sách gốc
        for (ProductDTO product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product); // Thêm vào danh sách đã lọc nếu tên sản phẩm chứa từ khóa
            }
        }

        // Cập nhật adapter với danh sách đã lọc
        productAdapter.notifyDataSetChanged();
    }
}
