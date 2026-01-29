package com.example.budgetwolt_android_final.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.activities.MenuActivity;
import com.example.budgetwolt_android_final.models.Cuisine;
import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private Activity activity;
    private List<Cuisine> cuisines;

    public MenuAdapter(Activity activity, List<Cuisine> cuisines) {
        this.activity = activity;
        this.cuisines = cuisines;
    }

    @Override
    public int getCount() { return cuisines.size(); }

    @Override
    public Cuisine getItem(int i) { return cuisines.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_menu, viewGroup, false);
        }

        Cuisine cuisine = getItem(i);

        TextView name = view.findViewById(R.id.tvItemName);
        TextView price = view.findViewById(R.id.tvPrice);
        TextView ingredients = view.findViewById(R.id.tvIngredients);
        TextView tvQuantity = view.findViewById(R.id.tvQuantity);
        Button btnPlus = view.findViewById(R.id.btnPlus);
        Button btnMinus = view.findViewById(R.id.btnMinus);

        name.setText(cuisine.getName());
        price.setText(String.format("$%.2f", cuisine.getPrice()));
        ingredients.setText(cuisine.getIngredients());
        tvQuantity.setText(String.valueOf(cuisine.getQuantity()));

        btnPlus.setOnClickListener(v -> {
            cuisine.setQuantity(cuisine.getQuantity() + 1);
            tvQuantity.setText(String.valueOf(cuisine.getQuantity()));
            if (activity instanceof MenuActivity) {
                ((MenuActivity) activity).updateOrderTotal();
            }
        });

        btnMinus.setOnClickListener(v -> {
            if (cuisine.getQuantity() > 0) {
                cuisine.setQuantity(cuisine.getQuantity() - 1);
                tvQuantity.setText(String.valueOf(cuisine.getQuantity()));
                if (activity instanceof MenuActivity) {
                    ((MenuActivity) activity).updateOrderTotal();
                }
            }
        });

        return view;
    }
}