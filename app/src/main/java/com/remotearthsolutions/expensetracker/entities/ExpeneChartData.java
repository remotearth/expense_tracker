package com.remotearthsolutions.expensetracker.entities;

public class ExpeneChartData {

    private float value;
    private String color;
    private String description;

    public ExpeneChartData(float value, String color, String description) {
        this.value = value;
        this.color = color;
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
