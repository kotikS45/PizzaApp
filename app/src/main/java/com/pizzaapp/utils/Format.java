package com.pizzaapp.utils;

import java.text.DecimalFormat;

public class Format {
    public static String formatCurrency(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return "₴" + decimalFormat.format(amount);
    }

    public static String formatWeight(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("0");
        return decimalFormat.format(amount) + "g";
    }

    public static String formatCategoryProductsCount(int count) {
        return "(" + count + ")";
    }
}
