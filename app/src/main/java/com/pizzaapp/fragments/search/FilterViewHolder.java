package com.pizzaapp.fragments.search;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;
import com.pizzaapp.databinding.ItemFilterBinding;

public class FilterViewHolder extends RecyclerView.ViewHolder {
    private final TextView name_tv;
    private final CheckBox selected_cb;
    public FilterViewHolder(@NonNull View itemView) {
        super(itemView);
        name_tv = itemView.findViewById(R.id.name_tv);
        selected_cb = itemView.findViewById(R.id.filter_cb);
    }

    public TextView getName_tv() {
        return name_tv;
    }

    public CheckBox getSelected_cb() {
        return selected_cb;
    }
}
