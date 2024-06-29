package com.pizzaapp.products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.R;

import java.util.List;

public class ProportionAdapter extends RecyclerView.Adapter<ProportionViewHolder> {
    private final List<String> proportions;
    private final ProportionAdapter.OnProportionClickListener listener;
    private int selectedPosition = 0;
    private Context context;

    public ProportionAdapter(List<String> proportions, ProportionAdapter.OnProportionClickListener listener) {
        this.proportions = proportions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProportionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.proportion_button_view, parent, false);
        return new ProportionViewHolder(view);
    }

    @SuppressLint({"NotifyDataSetChanged", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ProportionViewHolder holder, int position) {
        if(proportions != null && position < proportions.size()) {
            String proportion = proportions.get(position);

            if (context != null){
                int colorGray = ContextCompat.getColor(context, R.color.gray);
                int colorPurple = ContextCompat.getColor(context, R.color.purpleLight);
                holder.getProportion_btn().setBackgroundColor(position == selectedPosition ? colorPurple : colorGray);
            }

            holder.getProportion_btn().setText(proportion);

            holder.getProportion_btn().setOnClickListener(v -> {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
                listener.onProportionClick(v, position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return proportions.size();
    }

    public interface OnProportionClickListener {
        void onProportionClick (View v, int position);
    }
}