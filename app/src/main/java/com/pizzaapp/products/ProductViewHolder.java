package com.pizzaapp.products;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private final TextView name_tv;
    private final ImageView image_iv;
    private final TextView price_tv;
    private final TextView details_tv;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        name_tv = itemView.findViewById(R.id.name_tv);
        image_iv = itemView.findViewById(R.id.image_iv);
        price_tv = itemView.findViewById(R.id.price_tv);
        details_tv = itemView.findViewById(R.id.details_tv);
    }

    public TextView getPrice_tv() {
        return price_tv;
    }

    public TextView getName_tv() {
        return name_tv;
    }

    public ImageView getImage_iv() {
        return image_iv;
    }

    public TextView getDetails_tv() {
        return details_tv;
    }
}
