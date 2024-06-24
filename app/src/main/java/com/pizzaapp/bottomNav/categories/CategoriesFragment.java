package com.pizzaapp.bottomNav.categories;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pizzaapp.categories.CategoriesAdapter;
import com.pizzaapp.databinding.FragmentCategoriesBinding;
import com.pizzaapp.categories.Category;

import java.util.ArrayList;
import java.util.Objects;

public class CategoriesFragment extends Fragment {
    FragmentCategoriesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        getCategoriesList();

        return binding.getRoot();
    }

    public void getCategoriesList() {

        ArrayList<Category> categories = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String name = Objects.requireNonNull(categorySnapshot.child("name").getValue()).toString();
                    String image = Objects.requireNonNull(categorySnapshot.child("image").getValue()).toString();

                    categories.add(new Category(name, image));
                }

                Context context = getContext();
                if (context != null) {
                    binding.rcCategories.setLayoutManager(new LinearLayoutManager(context));
                    binding.rcCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                    binding.rcCategories.setAdapter(new CategoriesAdapter(categories));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
