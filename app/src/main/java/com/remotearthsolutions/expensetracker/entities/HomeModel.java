package com.remotearthsolutions.expensetracker.entities;

public class HomeModel {

    private int category_image;
    private String category_name;

    public HomeModel(int category_image, String category_name) {
        this.category_image = category_image;
        this.category_name = category_name;
    }

    public int getCategory_image() {
        return category_image;
    }

    public void setCategory_image(int category_image) {
        this.category_image = category_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
