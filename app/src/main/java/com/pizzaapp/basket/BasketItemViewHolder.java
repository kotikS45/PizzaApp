package com.pizzaapp.basket;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.pizzaapp.R;

public class BasketItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView name_tv;
    private final TextView price_tv;
    private final TextView proportion_tv;
    private final ImageView image_iv;
    private final ImageButton plus_ib;
    private final ImageButton minus_ib;
    private final TextInputEditText count_et;

    public BasketItemViewHolder(@NonNull View itemView) {
        super(itemView);
        name_tv = itemView.findViewById(R.id.name_tv);
        price_tv = itemView.findViewById(R.id.price_tv);
        proportion_tv = itemView.findViewById(R.id.proportion_tv);
        image_iv = itemView.findViewById(R.id.image_iv);
        plus_ib = itemView.findViewById(R.id.plus_ib);
        minus_ib = itemView.findViewById(R.id.minus_ib);
        count_et = itemView.findViewById(R.id.count_et);
    }

    public TextView getName_tv() {
        return name_tv;
    }

    public TextView getPrice_tv() {
        return price_tv;
    }

    public TextView getProportion_tv() {
        return proportion_tv;
    }

    public ImageView getImage_iv() {
        return image_iv;
    }

    public ImageButton getPlus_ib() {
        return plus_ib;
    }

    public ImageButton getMinus_ib() {
        return minus_ib;
    }

    public TextInputEditText getCount_et() {
        return count_et;
    }
}
