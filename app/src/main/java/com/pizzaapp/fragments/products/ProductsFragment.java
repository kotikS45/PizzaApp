package com.pizzaapp.fragments.products;

import android.os.Bundle;
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
import com.pizzaapp.R;
import com.pizzaapp.databinding.FragmentProductsBinding;
import com.pizzaapp.products.ProductListItem;
import com.pizzaapp.products.ProductsAdapter;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ProductsFragment extends Fragment implements ProductsAdapter.OnProductClickListener {
    private FragmentProductsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductsBinding.inflate(inflater, container, false);

        binding.backBtn.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

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
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.error_image)
                                .into(binding.imageIv);

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
        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);

        Fragment fragment = new ProductFragment();
        fragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
