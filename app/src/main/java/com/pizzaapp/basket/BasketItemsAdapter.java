package com.pizzaapp.basket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pizzaapp.R;
import com.pizzaapp.utils.Format;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class BasketItemsAdapter extends RecyclerView.Adapter<BasketItemViewHolder> {
    private static final int TYPE_PRODUCT = 0;
    private static final int TYPE_PADDING = 1;
    private final List<BasketItem> items;

    public BasketItemsAdapter(List<BasketItem> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == items.size()) {
            return TYPE_PADDING;
        }
        return TYPE_PRODUCT;
    }

    private void updateItemCountInDatabase(String userId, String itemId, int newCount) {
        DatabaseReference basketRef = FirebaseDatabase.getInstance().getReference().child("Baskets").child(userId).child(itemId);
        basketRef.child("count").setValue(newCount);
    }

    @NonNull
    @Override
    public BasketItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_PADDING) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.basket_item_padding, parent, false);
        } else {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.basket_item_view, parent, false);
        }
        return new BasketItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketItemViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PADDING)
            return;
        if (items != null && position < items.size()) {
            BasketItem item = items.get(position);
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            String itemId = item.getId();

            holder.getName_tv().setText(item.getName());
            holder.getPrice_tv().setText(Format.formatCurrency(item.getPrice()));
            holder.getCount_et().setText(String.valueOf(item.getCount()));
            holder.getProportion_tv().setText(item.getProportion());

            holder.getMinus_ib().setOnClickListener(v -> {
                TextInputEditText text = holder.getCount_et();
                int count = Integer.parseInt(Objects.requireNonNull(text.getText()).toString());
                if (count > 1) {
                    count--;
                    text.setText(String.valueOf(count));
                    updateItemCountInDatabase(userId, itemId, count);
                    item.setCount(count);
                }
            });

            holder.getPlus_ib().setOnClickListener(v -> {
                TextInputEditText text = holder.getCount_et();
                int count = Integer.parseInt(Objects.requireNonNull(text.getText()).toString());
                if (count < 100) {
                    count++;
                    text.setText(String.valueOf(count));
                    updateItemCountInDatabase(userId, itemId, count);
                    item.setCount(count);
                }
            });

            if (!item.getImage().isEmpty()) {

                Picasso.get()
                        .load(item.getImage())
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(holder.getImage_iv());
            } else {
                holder.getImage_iv().setImageResource(R.drawable.error_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size() > 8 ? items.size() + 1 : items.size();
    }
}
