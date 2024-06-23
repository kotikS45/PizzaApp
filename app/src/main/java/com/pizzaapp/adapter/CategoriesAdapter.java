package com.pizzaapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;
import com.pizzaapp.config.Config;
import com.pizzaapp.dto.CategoryDTO;
import com.pizzaapp.viewholder.CategoryCardViewHolder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryCardViewHolder> {
    private List<CategoryDTO> items = null;

    public CategoriesAdapter(List<CategoryDTO> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_categories_item, parent, false);
        return new CategoryCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCardViewHolder holder, int position) {
        if(items!=null && position<items.size()) {
            CategoryDTO item = items.get(position);
            holder.getCategoryName().setText(item.getName());
            String imageUrl = Config.BASE_URL + "/images/" + item.getImage();
            Picasso.get()
                    .load(imageUrl)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.getIvCategoryImage());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
