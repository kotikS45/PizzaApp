package com.pizzaapp.api;

import com.pizzaapp.dto.CategoryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesApi {
    @GET("/api/categories")
    public Call<List<CategoryDTO>> list();
}
