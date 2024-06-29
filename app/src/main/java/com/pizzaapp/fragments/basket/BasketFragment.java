package com.pizzaapp.fragments.basket;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pizzaapp.basket.BasketItem;
import com.pizzaapp.basket.BasketItemsAdapter;
import com.pizzaapp.databinding.FragmentBasketBinding;

import java.util.ArrayList;
import java.util.Objects;

public class BasketFragment extends Fragment {
    private FragmentBasketBinding binding;
    private ArrayList<BasketItem> items;
    private long count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBasketBinding.inflate(inflater, container, false);
        items = new ArrayList<>();

        getBasketItems(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        return binding.getRoot();
    }

    private void setData() {
        if (items.size() == count) {
            binding.productsRv.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.productsRv.setAdapter(new BasketItemsAdapter(items));
        }
    }

    private void getBasketItems(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Baskets").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    count = snapshot.getChildrenCount();
                    String id = itemSnapshot.getKey();
                    String productId = itemSnapshot.child("productId").getValue(String.class);
                    int count = Integer.parseInt(Objects.requireNonNull(itemSnapshot.child("count").getValue()).toString());

                    Integer proportion;
                    Object proportionObj = itemSnapshot.child("proportion").getValue();
                    if (proportionObj != null){
                        proportion = Integer.parseInt(proportionObj.toString());
                    }
                    else {
                        proportion = null;
                    }

                    assert productId != null;
                    DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
                    productReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                            String name = productSnapshot.child("name").getValue(String.class);
                            String image = productSnapshot.child("images").child("0").getValue(String.class);
                            double price = 0;
                            String proportionText = "";

                            if (proportion != null && productSnapshot.child("price").hasChild(String.valueOf(proportion))) {
                                price = Double.parseDouble(Objects.requireNonNull(productSnapshot.child("price").child(String.valueOf(proportion)).getValue()).toString());
                                proportionText = productSnapshot.child("proportion").child(String.valueOf(proportion)).getValue(String.class);
                            } else {
                                Object priceObj = productSnapshot.child("price").child("0").getValue();
                                if (priceObj != null) {
                                    price = Double.parseDouble(priceObj.toString());
                                }
                            }

                            BasketItem basketItem = new BasketItem(id, productId, image, name, proportionText, price, count);
                            items.add(basketItem);
                            setData();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    binding.productsRv.setAdapter(new BasketItemsAdapter(items));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
