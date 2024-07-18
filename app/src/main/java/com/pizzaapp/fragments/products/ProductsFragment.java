package com.pizzaapp.fragments.products;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pizzaapp.ProductActivity;
import com.pizzaapp.databinding.FragmentProductsBinding;
import com.pizzaapp.fragments.search.FilterFragment;
import com.pizzaapp.products.ProductListItem;
import com.pizzaapp.products.ProductsAdapter;
import com.pizzaapp.search.FilterItem;
import com.pizzaapp.search.OnFilterListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsFragment extends Fragment implements ProductsAdapter.OnProductClickListener, OnFilterListener {
    private FragmentProductsBinding binding;
    private FilterFragment filterFragment;
    private String categoryId;
    private String searchText = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductsBinding.inflate(inflater, container, false);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryId = bundle.getString("categoryId");
            getCategory(categoryId);
            getProductsList(categoryId);
            bundle.putString("categoryId", categoryId);
            filterFragment = new FilterFragment(this);
            filterFragment.setArguments(bundle);
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(binding.fragmentContainer.getId(), filterFragment)
                .commit();

        binding.filterBtn.setOnClickListener(v -> binding.fragmentContainer.setVisibility(View.VISIBLE));
        binding.text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = s.toString();
                updateProducts(filterFragment.getFilters(), filterFragment.getMaxPrice(), filterFragment.getMinPrice(), searchText);
            }
        });

        return binding.getRoot();
    }

    private void getCategory(String id) {
        FirebaseDatabase.getInstance().getReference().child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    if (Objects.equals(categorySnapshot.getKey(), id)) {
                        String name = Objects.requireNonNull(categorySnapshot.child("name").getValue()).toString();

                        binding.titleTv.setText(name);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getProductsList(String categoryId) {

        ArrayList<ProductListItem> products = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String category = Objects.requireNonNull(productSnapshot.child("categoryId").getValue()).toString();
                    if (!category.equals(categoryId))
                        continue;

                    String id = productSnapshot.getKey();
                    String name = Objects.requireNonNull(productSnapshot.child("name").getValue()).toString();

                    Double price = productSnapshot.child("price").getChildren().iterator().next().getValue(Double.class);

                    String image = Objects.requireNonNull(productSnapshot.child("images").getChildren().iterator().next().getValue()).toString();

                    products.add(new ProductListItem(id, name, price, image));
                }

                binding.rcProducts.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rcProducts.setAdapter(new ProductsAdapter(products, ProductsFragment.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onProductClick(String productId) {
        Intent intent = new Intent(requireActivity(), ProductActivity.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
    }

    @Override
    public void onFilterApplied(View view, double minPrice, double maxPrice, List<FilterItem> filters) {
        binding.fragmentContainer.setVisibility(View.GONE);

        updateProducts(filters, maxPrice, minPrice, searchText);
    }

    private void updateProducts(List<FilterItem> filters, double maxPrice, double minPrice, String text) {
        ArrayList<ProductListItem> filteredProducts = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String category = Objects.requireNonNull(productSnapshot.child("categoryId").getValue()).toString();
                    if (!category.equals(categoryId)) {
                        continue;
                    }

                    String id = productSnapshot.getKey();
                    String name = Objects.requireNonNull(productSnapshot.child("name").getValue()).toString();
                    Double price = productSnapshot.child("price").getChildren().iterator().next().getValue(Double.class);
                    String image = Objects.requireNonNull(productSnapshot.child("images").getChildren().iterator().next().getValue()).toString();

                    boolean passFilters = price == null || (!(price < minPrice) && !(price > maxPrice));

                    if (!name.toLowerCase().contains(text.toLowerCase())) {
                        passFilters = false;
                    }

                    if (passFilters) {
                        filteredProducts.add(new ProductListItem(id, name, price, image));
                    }
                }

                binding.rcProducts.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rcProducts.setAdapter(new ProductsAdapter(filteredProducts, ProductsFragment.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}