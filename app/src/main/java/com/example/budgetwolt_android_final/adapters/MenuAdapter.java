package com.example.budgetwolt_android_final.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.models.Cuisine;

import org.w3c.dom.Text;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private Activity activity;
    private List<Cuisine> cuisines;

    public MenuAdapter(Activity activity, List<Cuisine> cuisines) {
        this.activity = activity;
        this.cuisines = cuisines;
    }

    @Override
    public int getCount() {
        return cuisines.size();
    }

    @Override
    public Cuisine getItem(int i) {
        return cuisines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View menuItem;

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuItem = layoutInflater.inflate(R.layout.item_menu, viewGroup, false);

        TextView name = menuItem.findViewById(R.id.tvItemName);
        TextView price = menuItem.findViewById(R.id.tvPrice);
        TextView ingredients = menuItem.findViewById(R.id.tvIngredients);
        CheckBox checkBox = menuItem.findViewById(R.id.checkBox);

        Cuisine cuisine = this.getItem(i);

        name.setText(cuisine.getName());
        price.setText(String.valueOf(cuisine.getPrice()));
        ingredients.setText(cuisine.getIngredients());

        return menuItem;
    }
}
