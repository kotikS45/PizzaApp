package com.pizzaapp.products;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private List<Product> items = null;
    private final ProductsAdapter.OnProductClickListener onProductClickListener;

    public ProductsAdapter(List<Product> items, ProductsAdapter.OnProductClickListener listener) {
        this.items = items;
        this.onProductClickListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.product_item_view, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if(items!=null && position<items.size()) {
            Product product = items.get(position);
            holder.getName_tv().setText(product.getName());
            holder.getPrice_tv().setText(String.valueOf(formatCurrency(product.getPrice().get(0))));

            if (!product.getImages().isEmpty()) {

                Picasso.get()
                        .load(product.getImages().get(0))
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(holder.getImage_iv());

                holder.itemView.setOnClickListener(v -> {
                    if (onProductClickListener != null) {
                        onProductClickListener.onProductClick(items.get(position).getId());
                    }
                });
            } else {
                holder.getImage_iv().setImageResource(R.drawable.error_image);
            }
        }
    }

    public static String formatCurrency(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(amount) + " грн";
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnProductClickListener {
        void onProductClick(String categoryId);
    }
}