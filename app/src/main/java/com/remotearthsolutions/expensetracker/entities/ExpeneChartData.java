package com.remotearthsolutions.expensetracker.entities;

public class ExpeneChartData {

    private double value;
    private String categoryName;

    public ExpeneChartData(double value, String categoryName) {
        this.value = value;
        this.categoryName = categoryName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
