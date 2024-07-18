package com.pizzaapp.fragments.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pizzaapp.databinding.FragmentFilterBinding;
import com.pizzaapp.search.FilterItem;
import com.pizzaapp.search.OnFilterListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FilterFragment extends Fragment {
    private FragmentFilterBinding binding;
    private final OnFilterListener okListener;
    private double minPrice = 0;
    private double maxPrice = 0;
    private final ArrayList<FilterItem> filters = new ArrayList<>();
    private FiltersAdapter filtersAdapter;

    public FilterFragment (OnFilterListener listener) {
        okListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilterBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String categoryId = bundle.getString("categoryId");
            loadFilters(categoryId);
        }

        binding.okBtn.setOnClickListener((view) -> {
            List<FilterItem> updatedFilters = filtersAdapter.getUpdatedFilters();

            okListener.onFilterApplied(view, minPrice, maxPrice, updatedFilters);
        });

        binding.priceRsb.addOnChangeListener((slider, value, fromUser) -> {
            minPrice = slider.getValues().get(0);
            maxPrice = slider.getValues().get(1);
        });

        return binding.getRoot();
    }

    private void loadFilters(String categoryId) {
        FirebaseDatabase.getInstance().getReference().child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (categoryId.equals("premium_pizza")) {
                    Set<String> uniqueProportions = new HashSet<>();
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        String category = Objects.requireNonNull(productSnapshot.child("categoryId").getValue()).toString();
                        if (category.equals(categoryId)) {
                            for (DataSnapshot proportionSnapshot : productSnapshot.child("proportion").getChildren()) {
                                String proportion = proportionSnapshot.getValue(String.class);
                                if (proportion != null) {
                                    uniqueProportions.add(proportion);
                                }
                            }
                        }
                    }
                    for (String proportion : uniqueProportions) {
                        filters.add(new FilterItem(proportion, true));
                    }
                }

                loadPrice(categoryId);

                binding.filtersRv.setLayoutManager(new LinearLayoutManager(getContext()));
                filtersAdapter = new FiltersAdapter(filters);
                binding.filtersRv.setAdapter(filtersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPrice(String categoryId) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        double price = getProductMinPrice(productSnapshot);
                        if (minPrice == 0) {
                            minPrice = price;
                        }
                        else if (price < minPrice) {
                            minPrice = price;
                        }

                        if (maxPrice == 0) {
                            maxPrice = price;
                        }
                        else if (price > maxPrice) {
                            maxPrice = price;
                        }
                    }

                    binding.priceRsb.setValueFrom((float) minPrice);
                    binding.priceRsb.setValueTo((float) maxPrice);
                    binding.priceRsb.setValues((float) minPrice, (float)maxPrice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Double getProductMinPrice(DataSnapshot productSnapshot) {
        Double minPrice = null;
        for (DataSnapshot priceSnapshot : productSnapshot.child("price").getChildren()) {
            Double price = priceSnapshot.getValue(Double.class);
            if (price != null) {
                if (minPrice == null || price < minPrice) {
                    minPrice = price;
                }
            }
        }
        return minPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public ArrayList<FilterItem> getFilters() {
        return filters;
    }
}
