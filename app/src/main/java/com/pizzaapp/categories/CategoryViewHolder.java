package com.pizzaapp.categories;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView name_rv;
    private final ImageView image_iv;
    private final TextView count_tv;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        name_rv = itemView.findViewById(R.id.name_tv);
        image_iv = itemView.findViewById(R.id.image_iv);
        count_tv = itemView.findViewById(R.id.count_tv);
    }

    public TextView getName_rv() {
        return name_rv;
    }

    public ImageView getImage_iv() {
        return image_iv;
    }

    public TextView getCount_tv() {
        return count_tv;
    }
}