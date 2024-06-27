package com.pizzaapp.products;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;

public class ProportionViewHolder extends RecyclerView.ViewHolder {
    private final Button proportion_btn;

    public ProportionViewHolder(@NonNull View itemView) {
        super(itemView);
        proportion_btn = itemView.findViewById(R.id.proportion_btn);
    }

    public Button getProportion_btn() {
        return proportion_btn;
    }
}
