package com.example.fruitshopping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitshopping.UserActivity.LoginActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Chờ một khoảng thời gian trước khi chuyển đến Activity chính
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Kết thúc SplashActivity
            }
        }, 3000); // Thời gian chờ (3 giây)
    }
}