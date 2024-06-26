package com.pizzaapp.fragments.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pizzaapp.databinding.FragmentProductsBinding;
import com.pizzaapp.products.Product;
import com.pizzaapp.products.ProductsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsFragment extends Fragment implements ProductsAdapter.OnProductClickListener {
    private FragmentProductsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductsBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String categoryId = bundle.getString("categoryId");
            getCategory(categoryId);
            getProductsList(categoryId);
        }

        return binding.getRoot();
    }

    private void getCategory(String id) {
        FirebaseDatabase.getInstance().getReference().child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    if (Objects.equals(categorySnapshot.getKey(), id)) {
                        String name = Objects.requireNonNull(categorySnapshot.child("name").getValue()).toString();
                        String image = Objects.requireNonNull(categorySnapshot.child("image").getValue()).toString();

                        binding.textView.setText(name);
                        Picasso.get()
                                .load(image)
                                .into(binding.imageIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getProductsList(String categoryId) {

        ArrayList<Product> products = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Categories").child(categoryId).child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String id = productSnapshot.getKey();
                    String name = Objects.requireNonNull(productSnapshot.child("name").getValue()).toString();

                    List<Double> prices = new ArrayList<>();
                    for (DataSnapshot priceSnapshot : productSnapshot.child("price").getChildren()) {
                        double price = Double.parseDouble(Objects.requireNonNull(priceSnapshot.getValue()).toString());
                        prices.add(price);
                    }

                    List<Double> weights = new ArrayList<>();
                    for (DataSnapshot weightSnapshot : productSnapshot.child("weight").getChildren()) {
                        double weight = Double.parseDouble(Objects.requireNonNull(weightSnapshot.getValue()).toString());
                        weights.add(weight);
                    }

                    ArrayList<String> images = new ArrayList<>();
                    for (DataSnapshot imageSnapshot : productSnapshot.child("images").getChildren()) {
                        images.add(Objects.requireNonNull(imageSnapshot.getValue()).toString());
                    }

                    products.add(new Product(id, name, prices, weights, images));
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
    public void onProductClick(String categoryId) {

    }
}
