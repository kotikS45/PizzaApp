package com.pizzaapp.bottomNav.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzaapp.adapter.CategoriesAdapter;
import com.pizzaapp.api.RetrofitClient;
import com.pizzaapp.databinding.FragmentCategoriesBinding;
import com.pizzaapp.dto.CategoryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment {
    FragmentCategoriesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        getCategoriesList();

        binding.rcCategories.setHasFixedSize(true);
        binding.rcCategories.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));

        return binding.getRoot();
    }

    public void getCategoriesList() {
        RetrofitClient
                .getInstance()
                .getCategoriesApi()
                .list()
                .enqueue(new Callback<List<CategoryDTO>>() {
                    @Override
                    public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                        List<CategoryDTO> items = response.body();
                        CategoriesAdapter ca = new CategoriesAdapter(items);
                        binding.rcCategories.setAdapter(ca);
                    }

                    @Override
                    public void onFailure(Call<List<CategoryDTO>> call, Throwable throwable) {

                    }
                });
    }
}
