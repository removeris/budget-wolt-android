package com.example.budgetwolt_android_final.activities;

import static com.example.budgetwolt_android_final.utilities.Constants.AVAILABLE_ORDERS_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.models.FoodOrder;
import com.example.budgetwolt_android_final.models.OrderStatus;
import com.example.budgetwolt_android_final.models.User;
import com.example.budgetwolt_android_final.utilities.LocalDateAdapter;
import com.example.budgetwolt_android_final.utilities.RestOperations;
import com.example.budgetwolt_android_final.adapters.DriverOrderAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DriverMainActivity extends AppCompatActivity {

    private ListView lvOrders;
    private List<FoodOrder> orderList = new ArrayList<>();
    private DriverOrderAdapter adapter;
    private User currentUser;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        String userJson = getIntent().getStringExtra("userJson");
        currentUser = gson.fromJson(userJson, User.class);

        lvOrders = findViewById(R.id.lvDriverOrders);

        findViewById(R.id.btnDriverProfile).setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("user", gson.toJson(currentUser));
            startActivity(intent);
        });

        findViewById(R.id.btnDriverHistory).setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            intent.putExtra("user", gson.toJson(currentUser));
            intent.putExtra("isDriver", true);
            startActivity(intent);
        });

        loadDriverTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDriverTasks();
    }

    public void loadDriverTasks() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String url = AVAILABLE_ORDERS_URL + "/" + currentUser.getId();
                String response = RestOperations.get(url);

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (response != null && !response.equals("ERROR")) {
                        Type listType = new TypeToken<List<FoodOrder>>(){}.getType();
                        orderList = gson.fromJson(response, listType);

                        adapter = new DriverOrderAdapter(this, orderList, currentUser.getId());
                        lvOrders.setAdapter(adapter);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}