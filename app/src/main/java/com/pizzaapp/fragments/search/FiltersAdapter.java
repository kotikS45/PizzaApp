package com.pizzaapp.fragments.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;
import com.pizzaapp.search.FilterItem;

import java.util.List;

public class FiltersAdapter extends RecyclerView.Adapter<FilterViewHolder> {
    private final List<FilterItem> filters;

    public FiltersAdapter(List<FilterItem> filters) {
        this.filters = filters;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_filter, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        if (filters != null && position < filters.size()) {
            FilterItem filter = filters.get(position);
            holder.getName_tv().setText(filter.getName());
            holder.getSelected_cb().setChecked(filter.isSelected());

            holder.getSelected_cb().setOnCheckedChangeListener((buttonView, isChecked) -> filter.setSelected(isChecked));
        }
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public List<FilterItem> getUpdatedFilters() {
        return filters;
    }
}