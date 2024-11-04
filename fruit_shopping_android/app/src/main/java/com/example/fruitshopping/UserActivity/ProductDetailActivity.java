package com.example.fruitshopping.UserActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fruitshopping.R;

import dtos.ProductDTO;
import model.Cart;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView productName, productDescription, productPrice, quantityText;
    private Button increaseButton, decreaseButton, addToCartButton, buyButton, back;
    private ImageView productImage;

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút quay lại
        getSupportActionBar().setTitle("FruitShopping"); // Thiết lập tiêu đề cho toolbar

        // Thiết lập sự kiện click cho tiêu đề của Toolbar
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về HomeActivity
                Intent intent = new Intent(ProductDetailActivity.this, ProductActivity.class);
                startActivity(intent);
                finish(); // Kết thúc ProfileActivity
            }
        });
        // Khởi tạo các thành phần giao diện
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        quantityText = findViewById(R.id.quantityText);
        increaseButton = findViewById(R.id.increaseButton);
        decreaseButton = findViewById(R.id.decreaseButton);
        addToCartButton = findViewById(R.id.addToCartButton);
        buyButton = findViewById(R.id.buyButton);
        productImage = findViewById(R.id.productImage);
        back = findViewById(R.id.back);

        // Lấy dữ liệu sản phẩm từ Intent
        ProductDTO product = (ProductDTO) getIntent().getSerializableExtra("product");
        if (product != null) {
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(formatCurrency(product.getPrice()));

            int resId = getResources().getIdentifier(product.getImage(), "drawable", getPackageName());
            if (resId != 0) {
                productImage.setImageResource(resId);
            } else {
                productImage.setImageResource(R.drawable.placeholder);
            }
        }

        // Xử lý sự kiện tăng giảm số lượng
        increaseButton.setOnClickListener(view -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
        });

        decreaseButton.setOnClickListener(view -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        // Xử lý sự kiện "Add to Cart"
        addToCartButton.setOnClickListener(view -> {
            addToCart(product, quantity);
        });

        // Xử lý sự kiện "Buy Now"
        buyButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailActivity.this, ConfirmProductActivity.class);
            intent.putExtra("product", product);
            intent.putExtra("quantity", quantity);
            startActivity(intent);
        });

        // Xử lý sự kiện quay lại
        back.setOnClickListener(view -> {
            finish(); // Kết thúc Activity hiện tại
        });
    }

    // Tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer, menu); // Thay thế bằng tên file menu của bạn
        return true;
    }

    // Xử lý sự kiện chọn menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Kiểm tra các mục menu
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
        } else if (id == android.R.id.home) { // Xử lý sự kiện nút quay lại
            // Quay về Activity trước đó
            finish(); // Thay vì khởi tạo lại Activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Phương thức hiển thị PopupMenu
    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor); // Truyền view gốc cho PopupMenu
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu()); // Inflate menu

        // Thiết lập sự kiện cho các mục trong PopupMenu
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.popup_item1) {
                // Chuyển đến OrderHistoryActivity
                Intent intent = new Intent(ProductDetailActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;
            } else if (menuItem.getItemId() == R.id.popup_item2) {
                // Xóa phiên đăng nhập và trở về màn hình đăng nhập
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Xóa dữ liệu đăng nhập
                editor.apply(); // Áp dụng thay đổi

                Cart.clearItems(); // Xóa giỏ hàng

                Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Kết thúc Activity hiện tại
                return true;
            }
            return false;
        });

        // Hiển thị PopupMenu
        popupMenu.show();
    }

    // Phương thức định dạng giá thành
    public static String formatCurrency(double amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return numberFormat.format(amount);
    }

    // Phương thức thêm sản phẩm vào giỏ hàng
    private void addToCart(ProductDTO product, int quantity) {
        if (product != null) {
            Cart.addItem(product, quantity);
            Log.d("Cart", "Added to cart: " + product.getName() + ", Quantity: " + quantity);
        }
    }

    // Phương thức quay lại
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Quay lại Activity trước
    }
}
