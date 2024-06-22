package com.pizzaapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;

public class CategoryCardViewHolder extends RecyclerView.ViewHolder {
    private TextView categoryName = null;
    private ImageView ivCategoryImage = null;


    public CategoryCardViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryName = itemView.findViewById(R.id.categoryName);
        ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
    }

    public TextView getCategoryName() {
        return categoryName;
    }

    public ImageView getIvCategoryImage() {
        return ivCategoryImage;
    }
}
