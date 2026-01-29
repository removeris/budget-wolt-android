package com.example.budgetwolt_android_final.adapters;

import static com.example.budgetwolt_android_final.utilities.Constants.HOME_URL;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.activities.ChatActivity;
import com.example.budgetwolt_android_final.activities.DriverMainActivity;
import com.example.budgetwolt_android_final.models.FoodOrder;
import com.example.budgetwolt_android_final.utilities.RestOperations;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

public class DriverOrderAdapter extends ArrayAdapter<FoodOrder> {
    private Context context;
    private int driverId;

    public DriverOrderAdapter(Context context, List<FoodOrder> orders, int driverId) {
        super(context, 0, orders);
        this.context = context;
        this.driverId = driverId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FoodOrder order = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.driver_order_item, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvDriverOrderTitle);
        TextView tvAddress = convertView.findViewById(R.id.tvDriverOrderAddress);
        TextView tvStatus = convertView.findViewById(R.id.tvDriverOrderStatus);
        Button btnAction = convertView.findViewById(R.id.btnDriverAction);
        Button btnChat = convertView.findViewById(R.id.btnDriverChat);

        tvTitle.setText("Order #" + order.getId());
        tvAddress.setText("Address: " + order.getBuyer().getAddress());
        tvStatus.setText("Status: " + order.getStatus());

        String statusStr = String.valueOf(order.getStatus());

        if (statusStr.equals("SEEN_BY_STAFF")) {
            btnAction.setText("Accept Pickup");
            btnChat.setVisibility(View.GONE);
            btnAction.setOnClickListener(v -> updateStatus("acceptOrder/" + order.getId() + "/" + driverId));
        } else if (statusStr.equals("IN_DELIVERY")) {
            btnAction.setText("Complete Order");
            btnChat.setVisibility(View.VISIBLE);

            btnAction.setOnClickListener(v -> updateStatus("completeOrder/" + order.getId()));

            btnChat.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("orderId", order.getId());
                intent.putExtra("currentUserId", driverId);
                context.startActivity(intent);
            });
        }

        return convertView;
    }

    private void updateStatus(String endpoint) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String response = RestOperations.put(HOME_URL + endpoint, "");
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (response.equals("SUCCESS")) {
                        if (context instanceof DriverMainActivity) {
                            ((DriverMainActivity) context).loadDriverTasks();
                        }
                    } else {
                        Toast.makeText(context, "Update failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}