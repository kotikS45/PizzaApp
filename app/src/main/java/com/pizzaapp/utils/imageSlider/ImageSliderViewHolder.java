package com.pizzaapp.utils.imageSlider;


import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;

public class ImageSliderViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public ImageSliderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
    }
}