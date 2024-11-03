package com.example.fruitshopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.UserActivity.ConfirmProductActivity;
import com.example.fruitshopping.UserActivity.LoginActivity;
import com.example.fruitshopping.UserActivity.OrderHistoryActivity;
import com.example.fruitshopping.UserActivity.ProductActivity;
import com.example.fruitshopping.UserActivity.ProfileActivity;

import java.util.List;
import model.Cart;
import model.CartItem;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI components
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        Button productListButton = findViewById(R.id.productListButton);
        Button cartButton = findViewById(R.id.cartButton);
        Button accountButton = findViewById(R.id.accountButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button orderHistoryButton = findViewById(R.id.orderHistoryButton);
        // Retrieve logged-in user's name from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userName = sharedPreferences.getString("name", "User");

        // Set welcome message with user's name
        welcomeTextView.setText("Xin chào, " + userName + "!");

        // Set up the View Products button click listener
        productListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });

        // Set up the Go to Cart button click listener
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CartItem> cartItems = Cart.getItems();
                if (cartItems == null || cartItems.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Giỏ hàng rỗng! Vui lòng thêm sản phẩm trước khi xác nhận.", Toast.LENGTH_SHORT).show();
                    return; // Không cho phép vào ConfirmProductActivity nếu giỏ hàng rỗng
                }

                Intent intent = new Intent(HomeActivity.this, ConfirmProductActivity.class);
                startActivity(intent);
            }
        });

        // Set up the My Account button click listener
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set up the Logout button click listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user session and redirect to login page
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Cart.clearItems();

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // End this activity to prevent returning to it after logout
            }
        });
        orderHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
