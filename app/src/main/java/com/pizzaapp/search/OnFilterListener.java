package com.pizzaapp.search;

import android.view.View;

import java.util.List;

public interface OnFilterListener {
    void onFilterApplied(View view, double minPrice, double maxPrice, List<FilterItem> filters);
}