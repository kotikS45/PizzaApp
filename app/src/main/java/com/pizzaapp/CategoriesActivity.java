package com.pizzaapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.adapter.CategoriesAdapter;
import com.pizzaapp.api.RetrofitClient;
import com.pizzaapp.dto.CategoryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends BaseActivity {
    RecyclerView rcCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        setupBottomNavigationView(R.id.bottom_navigation);

        rcCategories = findViewById(R.id.rcCategories);
        rcCategories.setHasFixedSize(true);
        rcCategories.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));

        RetrofitClient
                .getInstance()
                .getCategoriesApi()
                .list()
                .enqueue(new Callback<List<CategoryDTO>>() {
                    @Override
                    public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                        List<CategoryDTO> items = response.body();
                        CategoriesAdapter ca = new CategoriesAdapter(items);
                        rcCategories.setAdapter(ca);
                    }

                    @Override
                    public void onFailure(Call<List<CategoryDTO>> call, Throwable throwable) {

                    }
                });
    }
}