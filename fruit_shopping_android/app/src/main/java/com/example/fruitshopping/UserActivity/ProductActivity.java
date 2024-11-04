package com.example.fruitshopping.UserActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fruitshopping.HomeActivity;
import com.example.fruitshopping.R;

import java.util.ArrayList;

import adapter.ProductAdapter;
import dtos.ProductDTO;
import helper.ProductDBHelper;
import model.Cart;

public class ProductActivity extends AppCompatActivity {

    private ListView listView;
    private ProductAdapter productAdapter;
    private ProductDBHelper dbHelper;
    private ArrayList<ProductDTO> productList;
    private ArrayList<ProductDTO> filteredList; // Danh sách sản phẩm đã lọc
    private EditText searchEditText; // Thêm EditText cho tìm kiếm
    private Button searchButton; // Thêm Button cho tìm kiếm
    private SharedPreferences sharedPreferences; // Khai báo SharedPreferences
    private TextView welcomeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Khởi tạo SharedPreferences
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userName = sharedPreferences.getString("name", "User");

        welcomeTextView.setText("Xin chào, " + userName + "!");

        // Khởi tạo Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FruitShopping"); // Thiết lập tiêu đề Toolbar

        searchEditText = findViewById(R.id.searchEditText); // Khởi tạo EditText tìm kiếm
        searchButton = findViewById(R.id.searchButton); // Khởi tạo Button tìm kiếm

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Gán file menu vào Activity
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Xử lý sự kiện khi một mục trong menu được chọn
        int id = item.getItemId();
        if (id == R.id.menu_item1) {
            // Chuyển đến ProfileActivity
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item2) {
            // Chuyển đến ConfirmProductActivity
            Intent intent = new Intent(this, ConfirmProductActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item3) {
            showPopupMenu(findViewById(R.id.menu_item3)); // Gọi hàm hiển thị PopupMenu
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Phương thức hiển thị PopupMenu
    private void showPopupMenu(View anchor) {
        // Tạo một PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, anchor); // Truyền view gốc cho PopupMenu
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu()); // Inflate menu

        // Thiết lập sự kiện cho các mục trong PopupMenu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Xử lý các mục trong popup menu
                if (menuItem.getItemId() == R.id.popup_item1) {
                    // Chuyển đến OrderHistoryActivity
                    Intent intent = new Intent(ProductActivity.this, OrderHistoryActivity.class);
                    startActivity(intent);
                    return true;
                } else if (menuItem.getItemId() == R.id.popup_item2) {
                    // Xóa phiên đăng nhập và trở về màn hình đăng nhập
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear(); // Xóa dữ liệu đăng nhập
                    editor.apply(); // Áp dụng thay đổi

                    Cart.clearItems(); // Xóa giỏ hàng

                    Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc Activity hiện tại
                    return true;
                }
                return false;
            }
        });

        // Hiển thị PopupMenu
        popupMenu.show();
    }
}
