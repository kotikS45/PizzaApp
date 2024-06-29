package com.pizzaapp.fragments.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pizzaapp.R;
import com.pizzaapp.categories.CategoriesAdapter;
import com.pizzaapp.databinding.FragmentCategoriesBinding;
import com.pizzaapp.categories.Category;
import com.pizzaapp.fragments.products.ProductsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CategoriesFragment extends Fragment implements CategoriesAdapter.OnCategoryClickListener {
    private FragmentCategoriesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        getCategoriesList();

        return binding.getRoot();
    }

    private void getCategoriesList() {

        ArrayList<Category> categories = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Category> categoryMap = new HashMap<>();
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String id = categorySnapshot.getKey();
                    String name = Objects.requireNonNull(categorySnapshot.child("name").getValue()).toString();
                    String image = Objects.requireNonNull(categorySnapshot.child("image").getValue()).toString();

                    categoryMap.put(id, new Category(id, name, image, 0));
                }

                FirebaseDatabase.getInstance().getReference().child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            String categoryId = Objects.requireNonNull(productSnapshot.child("categoryId").getValue()).toString();
                            Category category = categoryMap.get(categoryId);
                            if (category != null) {
                                category.setProductsCount(category.getProductsCount() + 1);
                            }
                        }

                        categories.addAll(categoryMap.values());

                        binding.rcCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        binding.rcCategories.setAdapter(new CategoriesAdapter(categories, CategoriesFragment.this));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCategoryClick(String categoryId) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryId", categoryId);

        Fragment productsFragment = new ProductsFragment();
        productsFragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, productsFragment)
                .addToBackStack(null)
                .commit();
    }
}
