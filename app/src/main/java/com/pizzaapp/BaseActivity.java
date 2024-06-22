package com.pizzaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNavigationView(int id){

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(id);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;

                int id = item.getItemId();
                if (id == R.id.page_1) {
                    intent = new Intent(BaseActivity.this, CategoriesActivity.class);
                } else if (id == R.id.page_2) {
                    intent = new Intent(BaseActivity.this, CategoriesActivity.class);
                } else if (id == R.id.page_3) {
                    intent = new Intent(BaseActivity.this, CategoriesActivity.class);
                } else {
                    return false;
                }
                startActivity(intent);
                return true;
            }
        });
    }
}