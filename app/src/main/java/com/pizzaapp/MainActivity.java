package com.pizzaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.pizzaapp.fragments.basket.BasketFragment;
import com.pizzaapp.fragments.categories.CategoriesFragment;
import com.pizzaapp.fragments.home.HomeFragment;
import com.pizzaapp.fragments.profile.ProfileFragment;
import com.pizzaapp.databinding.ActivityMainBinding;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    HashMap<Integer, Fragment> fragmentsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        fragmentsMap = getFragmentsMap();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.fragmentContainer.getId(), new HomeFragment())
                .commit();
        binding.bottomNavigation.setSelectedItemId(R.id.home);

        binding.bottomNavigation.setOnItemSelectedListener(this::onMenuItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        login();
    }

    private void login() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private HashMap<Integer, Fragment> getFragmentsMap() {
        HashMap<Integer, Fragment> fragments = new HashMap<>();

        fragments.put(R.id.home, new HomeFragment());
        fragments.put(R.id.basket, new BasketFragment());
        fragments.put(R.id.categories, new CategoriesFragment());
        fragments.put(R.id.profile, new ProfileFragment());

        return fragments;
    }

    private boolean onMenuItemSelectedListener(MenuItem item) {
        Fragment fragment = fragmentsMap.get(item.getItemId());

        if (fragment == null)
            return false;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.fragmentContainer.getId(), fragment)
                .commit();

        return true;
    }
}