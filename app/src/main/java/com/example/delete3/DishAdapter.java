package com.example.delete3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import java.util.List;

public class DishAdapter extends ArrayAdapter<Dish> {

    private final int resourceId;

    public DishAdapter(Context context, int resourceId, List<Dish> dishes) {
        super(context, resourceId, dishes);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView :
                LayoutInflater.from(getContext()).inflate(resourceId, null);

        Dish dish = getItem(position);
        TextView dishNameTextView = view.findViewById(R.id.textViewDishName);
        TextView descriptionTextView = view.findViewById(R.id.textViewDescription);
        TextView cookingTimeTextView = view.findViewById(R.id.textViewCookingTime);
        ImageView imageView = view.findViewById(R.id.imageViewDish);

        dishNameTextView.setText(dish.getDishName());
        descriptionTextView.setText(dish.getDescription());
        cookingTimeTextView.setText("Cooking Time: " + dish.getCookingTime() + " minutes");

        byte[] imageData = dish.getImageData();
        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or handle the case where no image is available
        }

        return view;
    }

}
