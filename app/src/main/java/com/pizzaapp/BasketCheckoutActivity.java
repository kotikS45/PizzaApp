package com.pizzaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pizzaapp.basket.BasketCheckoutItemsAdapter;
import com.pizzaapp.basket.BasketItem;
import com.pizzaapp.databinding.ActivityBasketCheckoutBinding;
import com.pizzaapp.utils.Format;

import java.util.ArrayList;

public class BasketCheckoutActivity extends AppCompatActivity {
    private ActivityBasketCheckoutBinding binding;

    private ArrayList<BasketItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBasketCheckoutBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        items = getIntent().getParcelableArrayListExtra("basketItems");

        binding.productsRv.setLayoutManager(new LinearLayoutManager(BasketCheckoutActivity.this));
        binding.productsRv.setAdapter(new BasketCheckoutItemsAdapter(items, this::updatePrice));

        binding.backToMenuCl.setOnClickListener(this::backToMenuListener);

        updatePrice(null);
    }

    private void updatePrice(View v){
        double totalPrice = 0;
        for (BasketItem item : items) {
            totalPrice += item.getPrice() * item.getCount();
        }

        double deliveryFee = 0;

        binding.subtotalTv.setText(Format.formatCurrency(totalPrice));
        binding.basketTotalTv.setText(Format.formatCurrency(totalPrice));
        binding.deliveryFeeTv.setText(Format.formatCurrency(deliveryFee));
        binding.totalTv.setText(Format.formatCurrency(totalPrice + deliveryFee));
    }

    private void backToMenuListener(View v) {
        Intent intent = new Intent(BasketCheckoutActivity.this, MainActivity.class);
        intent.putExtra("page", R.id.categories);
        startActivity(intent);
    }
}