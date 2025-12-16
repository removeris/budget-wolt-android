package com.example.budgetwolt_android_final.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.models.Restaurant;

import java.util.ArrayList;

public class RestaurantAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Restaurant> restaurants;

    public RestaurantAdapter(Activity activity, ArrayList<Restaurant> restaurants) {
        this.activity = activity;
        this.restaurants = restaurants;
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Restaurant getItem(int i) {
        return restaurants.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View restaurantItem;

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        restaurantItem = layoutInflater.inflate(R.layout.item_restaurant, viewGroup, false);

        Restaurant restaurant = this.getItem(i);

        TextView name = restaurantItem.findViewById(R.id.textViewName);
        TextView address = restaurantItem.findViewById(R.id.textViewAddress);
        TextView phoneNumber = restaurantItem.findViewById(R.id.textViewPhoneNumber);
        RatingBar ratingBar = restaurantItem.findViewById(R.id.ratingBar);

        name.setText(restaurant.getName());
        address.setText("📍" +restaurant.getAddress());
        phoneNumber.setText("📞" +restaurant.getPhoneNumber());

        ratingBar.setMax(5);
        ratingBar.setStepSize(0.5f);
        ratingBar.setRating((float) restaurant.getRating());

        return restaurantItem;
    }
}
