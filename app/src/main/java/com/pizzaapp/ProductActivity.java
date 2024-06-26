package com.pizzaapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pizzaapp.categories.Category;
import com.pizzaapp.databinding.ActivityProductBinding;
import com.pizzaapp.products.Product;
import com.pizzaapp.products.ProportionAdapter;
import com.pizzaapp.utils.Format;
import com.pizzaapp.utils.imageSlider.ImageSliderAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity implements ProportionAdapter.OnProportionClickListener {

    private ActivityProductBinding binding;

    private Product product;
    private List<String> proportions = new ArrayList<>();
    private int selectedProportion = 0;
    private Category category;
    private boolean alreadyInBasket = false;
    private ProportionAdapter proportionsAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.backBtn.setOnClickListener(v -> finish());
        binding.addToCartBtn.setOnClickListener(this::onAddToCartListener);

        String productId = getIntent().getStringExtra("productId");
        getProductInfo(productId);
        checkInBasket(0);
    }

    private void checkInBasket(int indexProportion) {
        binding.addToCartBtn.setImageResource(R.drawable.heart_filled);
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference().child("Baskets").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alreadyInBasket = false;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String productId = Objects.requireNonNull(childSnapshot.child("productId").getValue()).toString();

                    if (proportions.isEmpty()) {
                        if (productId.equals(product.getId())) {
                            alreadyInBasket = true;
                            return;
                        }
                    }
                    else {
                        int proportion;
                        Object proportionObj = childSnapshot.child("proportion").getValue();
                        if (proportionObj != null){
                            proportion = Integer.parseInt(proportionObj.toString());
                            if (productId.equals(product.getId()) && proportion == indexProportion) {
                                alreadyInBasket = true;
                                return;
                            }
                        }
                    }
                }
                binding.addToCartBtn.setImageResource(R.drawable.heart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onAddToCartListener(View v) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference basketRef = FirebaseDatabase.getInstance().getReference().child("Baskets").child(uid);
        String itemId = product.getId();

        if (!proportions.isEmpty()) {
            itemId += "_" + selectedProportion;
        }

        if (alreadyInBasket) {
            basketRef.child(itemId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    alreadyInBasket = false;
                }
                binding.addToCartBtn.setImageResource(R.drawable.heart);
            });
        } else {
            HashMap<String, Object> basketItem = new HashMap<>();
            basketItem.put("productId", product.getId());
            basketItem.put("count", 1);
            if (!proportions.isEmpty()) {
                basketItem.put("proportion", selectedProportion);
            }
            basketRef.child(itemId).setValue(basketItem).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    alreadyInBasket = true;
                }
                binding.addToCartBtn.setImageResource(R.drawable.heart_filled);
            });
        }
    }

    private void getProductInfo(String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("Categories");

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                if (productSnapshot.exists()) {
                    String id = productSnapshot.getKey();
                    String name = Objects.requireNonNull(productSnapshot.child("name").getValue()).toString();
                    String categoryId = Objects.requireNonNull(productSnapshot.child("categoryId").getValue()).toString();
                    String details = Objects.requireNonNull(productSnapshot.child("details").getValue()).toString();

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

                    List<String> images = new ArrayList<>();
                    for (DataSnapshot imageSnapshot : productSnapshot.child("images").getChildren()) {
                        String image = Objects.requireNonNull(imageSnapshot.getValue()).toString();
                        images.add(image);
                    }

                    proportions = new ArrayList<>();
                    for (DataSnapshot proportionSnapshot : productSnapshot.child("proportion").getChildren()) {
                        String proportion = Objects.requireNonNull(proportionSnapshot.getValue()).toString();
                        proportions.add(proportion);
                    }

                    product = new Product(id, name, prices, weights, images, details);

                    binding.proportionsRv.setLayoutManager(new LinearLayoutManager(ProductActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    proportionsAdapter = new ProportionAdapter(proportions, ProductActivity.this);
                    binding.proportionsRv.setAdapter(proportionsAdapter);

                    categoryRef.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot categorySnapshot) {
                            if (categorySnapshot.exists()) {
                                String catId = categorySnapshot.getKey();
                                String catName = Objects.requireNonNull(categorySnapshot.child("name").getValue()).toString();
                                String catImage = Objects.requireNonNull(categorySnapshot.child("image").getValue()).toString();

                                category = new Category(catId, catName, catImage, 0);

                                setViewProductInfo();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void setViewProductInfo() {
        if (product == null || category == null)
            return;

        ImageSliderAdapter adapter = new ImageSliderAdapter(product.getImages());
        binding.imageVp.setAdapter(adapter);

        binding.nameTv.setText(product.getName());

        binding.priceTv.setText(Format.formatCurrency(product.getPrice().get(0)));

        binding.categoriesTv.setText(category.getName());

        binding.weightTv.setText(Format.formatWeight(product.getWeight().get(0)));

        binding.detailsTv.setText(product.getDetails());
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onProportionClick(View v, int position) {
        selectedProportion = position;
        checkInBasket(position);
        if (v instanceof Button) {
            Button clickedButton = (Button) v;

            for (int i = 0; i < binding.proportionsRv.getChildCount(); i++) {
                View child = binding.proportionsRv.getChildAt(i);
                if (child instanceof Button) {
                    Button button = (Button) child;
                    int colorGray = ContextCompat.getColor(ProductActivity.this, R.color.gray);
                    int colorPurple = ContextCompat.getColor(ProductActivity.this, R.color.purpleLight);
                    button.setBackgroundColor(button == clickedButton ? colorPurple : colorGray);
                }
            }

            binding.priceTv.setText(Format.formatCurrency(product.getPrice().get(position)));

            binding.weightTv.setText(Format.formatWeight(product.getWeight().get(position)));
        }
    }
}