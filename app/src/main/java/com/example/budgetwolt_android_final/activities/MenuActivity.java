package com.example.budgetwolt_android_final.activities;

import static com.example.budgetwolt_android_final.utilities.Constants.CREATE_ORDER_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.adapters.MenuAdapter;
import com.example.budgetwolt_android_final.models.Cuisine;
import com.example.budgetwolt_android_final.models.User;
import com.example.budgetwolt_android_final.utilities.Constants;
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
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MenuActivity extends AppCompatActivity {

    private List<Cuisine> cuisines = new ArrayList<>();
    private MenuAdapter menuAdapter;
    private TextView tvTotalAmount;
    private User currentUserId;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        ListView listViewMenu = findViewById(R.id.listViewMenu);
        Button btnProceed = findViewById(R.id.btnProceedOrder);

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        int restaurantId = getIntent().getIntExtra("restaurantId", 0);
        int currentUserId = getIntent().getIntExtra("userId", 0);

        fetchMenuData(restaurantId, listViewMenu);

        btnProceed.setOnClickListener(v -> {
            ArrayList<Cuisine> cart = new ArrayList<>();
            double total = 0;
            for (Cuisine c : cuisines) {
                if (c.getQuantity() > 0) {
                    cart.add(c);
                    total += (c.getPrice() * c.getQuantity());
                }
            }

            if (cart.isEmpty()) {
                Toast.makeText(this, "Please add items to your cart first!", Toast.LENGTH_SHORT).show();
                return;
            }

            Properties params = new Properties();
            params.setProperty("userId", String.valueOf(currentUserId));
            params.setProperty("restaurantId", String.valueOf(restaurantId));
            params.setProperty("totalPrice", String.valueOf(total));

            String itemsJson = gson.toJson(cart);
            params.setProperty("items", itemsJson);

            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                try {
                    String requestBody = gson.toJson(params);
                    String response = RestOperations.post(CREATE_ORDER_URL, requestBody);

                    handler.post(() -> {
                        if (response.equals("SUCCESS")) {
                            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Server error. Try again.", Toast.LENGTH_SHORT).show();
                            Log.e("ORDER_ERROR", "Server returned: " + response);
                        }
                    });
                } catch (IOException e) {
                    handler.post(() -> Toast.makeText(this, "Network error!", Toast.LENGTH_SHORT).show());
                    e.printStackTrace();
                }
            });
        });
    }

    private void fetchMenuData(int restaurantId, ListView listView) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String url = Constants.RESTAURANT_MENU_URL + "/" + restaurantId;
                String response = RestOperations.get(url);

                handler.post(() -> {
                    if (!response.equals("ERROR")) {
                        Type listType = new TypeToken<List<Cuisine>>() {}.getType();
                        cuisines = gson.fromJson(response, listType);

                        for (Cuisine c : cuisines) {
                            c.setQuantity(0);
                        }
                        tvTotalAmount.setText("Total: $0.00");

                        menuAdapter = new MenuAdapter(MenuActivity.this, cuisines);
                        listView.setAdapter(menuAdapter);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateOrderTotal() {
        double total = 0.0;
        for (Cuisine c : cuisines) {
            total += (c.getPrice() * c.getQuantity());
        }
        tvTotalAmount.setText(String.format("Total: $%.2f", total));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(Cuisine c : cuisines) {
            c.setQuantity(0);
        }
    }
}