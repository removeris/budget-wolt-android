package com.example.budgetwolt_android_final.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.activities.ChatActivity;
import com.example.budgetwolt_android_final.activities.OrderHistoryActivity;
import com.example.budgetwolt_android_final.models.FoodOrder;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<FoodOrder> {
    private Context context;
    private List<FoodOrder> orders;

    private int currentUserId;

    private boolean isDriver;

    public OrderAdapter(Context context, List<FoodOrder> orders, int currentUserId, boolean isDriver) {
        super(context, 0, orders);
        this.context = context;
        this.orders = orders;
        this.currentUserId = currentUserId;
        this.isDriver = isDriver;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        }

        FoodOrder order = orders.get(position);

        TextView title = convertView.findViewById(R.id.tvOrderTitle);
        TextView status = convertView.findViewById(R.id.tvOrderStatus);
        TextView price = convertView.findViewById(R.id.tvOrderPrice);
        Button btnChat = convertView.findViewById(R.id.btnChat);
        Button btnRate = convertView.findViewById(R.id.btnRate);

        title.setText(order.getTitle());
        status.setText("Status: " + order.getStatus().toString());
        price.setText(String.format("$%.2f", order.getPrice()));

        boolean isCompleted = order.getStatus().toString().equals("COMPLETED");

        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            intent.putExtra("orderId", order.getId());
            intent.putExtra("isReadOnly", isCompleted); // This locks the chat
            context.startActivity(intent);
        });

        if (isCompleted) {
            btnRate.setVisibility(View.VISIBLE);

            if (isDriver) {
                btnRate.setText("Rate Client");
                btnRate.setOnClickListener(v -> {
                    if (context instanceof OrderHistoryActivity) {
                        Log.d("RATING", "getView: " + order.getBuyer());
                        ((OrderHistoryActivity) context).showRatingDialog(order.getBuyer().getId(), "CLIENT");
                    }
                });
            } else {
                btnRate.setText("Rate Restaurant");
                btnRate.setOnClickListener(v -> {
                    if (context instanceof OrderHistoryActivity) {
                        ((OrderHistoryActivity) context).showRatingDialog(order.getRestaurant().getId(), "RESTAURANT");
                    }
                });
            }
        } else {
            btnRate.setVisibility(View.GONE);
        }

        return convertView;
    }
}