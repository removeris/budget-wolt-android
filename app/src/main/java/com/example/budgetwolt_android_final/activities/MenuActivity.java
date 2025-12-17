package com.example.budgetwolt_android_final.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.adapters.MenuAdapter;
import com.example.budgetwolt_android_final.adapters.RestaurantAdapter;
import com.example.budgetwolt_android_final.models.BasicUser;
import com.example.budgetwolt_android_final.models.Driver;
import com.example.budgetwolt_android_final.models.User;
import com.example.budgetwolt_android_final.utilities.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MenuActivity extends AppCompatActivity {

    private User currentUser;
    private MenuAdapter menuAdapter;
    private String userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", 0);
        int restaurantId = intent.getIntExtra("restaurantId", 0);
        userInfo = intent.getStringExtra("userJson");

        Log.d("INFO", "USER: " + userInfo);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        currentUser = gson.fromJson(userInfo, User.class);
        if (currentUser instanceof Driver) {
            currentUser = gson.fromJson(userInfo, Driver.class);
        } else if (currentUser instanceof BasicUser) {
            currentUser = gson.fromJson(userInfo, BasicUser.class);
        }

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

        });
    }
}