package com.example.budgetwolt_android_final.activities;

import static com.example.budgetwolt_android_final.utilities.Constants.ALL_RESTAURANTS_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.adapters.RestaurantAdapter;
import com.example.budgetwolt_android_final.models.BasicUser;
import com.example.budgetwolt_android_final.models.Driver;
import com.example.budgetwolt_android_final.models.Restaurant;
import com.example.budgetwolt_android_final.models.User;
import com.example.budgetwolt_android_final.utilities.LocalDateAdapter;
import com.example.budgetwolt_android_final.utilities.RestOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private User currentUser;
    private RestaurantAdapter restaurantAdapter;
    private String userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
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

        if (currentUser instanceof Restaurant) {
            Toast.makeText(MainActivity.this, "Restaurants must use desktop app", Toast.LENGTH_SHORT);
        } else if (currentUser instanceof Driver) {
            // Ignore
        } else {

            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {

                try {
                    String response = RestOperations.get(ALL_RESTAURANTS_URL);

                    Log.d("INFO", response);

                    handler.post(() -> {

                        Type restaurantListType = new TypeToken<List<Restaurant>>() {}.getType();
                        List<Restaurant> restaurants = gson.fromJson(response, restaurantListType);


                        ListView listViewRestaurants = findViewById(R.id.listViewRestaurants);
                        restaurantAdapter = new RestaurantAdapter((Activity) MainActivity.this, restaurants);
                        listViewRestaurants.setAdapter(restaurantAdapter);

                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void openProfileActivity(View view) {

        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);

        intent.putExtra("userJson", userInfo);
        startActivity(intent);
    }

    public void openOrderActivity(View view) {
    }
}