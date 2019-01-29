package com.remotearthsolutions.expensetracker.entities;

public class ExpeneChartData {

    private double value;
    private String color;
    private String categoryName;

    public ExpeneChartData(double value, String color, String categoryName) {
        this.value = value;
        this.color = color;
        this.categoryName = categoryName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
