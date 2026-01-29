package com.example.budgetwolt_android_final.activities;

import static com.example.budgetwolt_android_final.utilities.Constants.HOME_URL;
import static com.example.budgetwolt_android_final.utilities.Constants.ORDERS_BY_USER_URL;
import static com.example.budgetwolt_android_final.utilities.Constants.SUBMIT_RATING_URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.adapters.OrderAdapter;
import com.example.budgetwolt_android_final.models.FoodOrder;
import com.example.budgetwolt_android_final.utilities.LocalDateAdapter;
import com.example.budgetwolt_android_final.utilities.RestOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class OrderHistoryActivity extends AppCompatActivity {
    private ListView lvOrders;
    private List<FoodOrder> orderList = new ArrayList<>();
    private Gson gson;

    private int currentUserId;
    private String userJson;

    private boolean isDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        lvOrders = findViewById(R.id.lvOrders);

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        userJson = getIntent().getStringExtra("user");
        isDriver = getIntent().getBooleanExtra("isDriver", false);
        JsonObject userObj = gson.fromJson(userJson, JsonObject.class);
        currentUserId = userObj.get("id").getAsInt();
        Log.d("INFO_MSG", "Check history driver issues: " + currentUserId);
        Log.d("ORD_HISTORY", "onCreate: " + userJson);

        Log.d("ORD_HISTORY", "Loading history for ID: " + currentUserId);

        fetchOrders(userObj);
    }

    private void fetchOrders(JsonObject userObj) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String endpoint;
                if (isDriver) {
                    endpoint = "getDriverHistory";
                } else {
                    endpoint = "getOrderByUser";
                }
                Log.d("ORD_HISTORY", "Loading history " + endpoint);
                String url = HOME_URL + endpoint + "/" + currentUserId;
                String response = RestOperations.get(url);

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (response != null && !response.equals("ERROR")) {
                        Log.d("INFO_MSG", "fetchOrders: " + response);
                        Type listType = new TypeToken<List<FoodOrder>>(){}.getType();
                        orderList = gson.fromJson(response, listType);

                        OrderAdapter adapter = new OrderAdapter(this, orderList, currentUserId, isDriver);
                        lvOrders.setAdapter(adapter);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void showRatingDialog(int targetId, String targetType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_rating, null);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        builder.setView(view);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            float rating = ratingBar.getRating();

            JsonObject data = new JsonObject();
            data.addProperty("rating", String.valueOf(rating));
            data.addProperty("targetId", String.valueOf(targetId));
            data.addProperty("authorId", String.valueOf(currentUserId));
            data.addProperty("targetType", targetType);

            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    String response = RestOperations.post(SUBMIT_RATING_URL, gson.toJson(data));

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (response.equals("SUCCESS")) {
                            Toast.makeText(this, "Rating submitted for " + targetType, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) { e.printStackTrace(); }
            });
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}