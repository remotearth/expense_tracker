package com.remotearthsolutions.expensetracker.entities;

public class DashboardModel {

    private int dashboardImage;
    private String dashboardName;

    public DashboardModel(int dashboardImage, String dashboardName) {
        this.dashboardImage = dashboardImage;
        this.dashboardName = dashboardName;
    }

    public int getDashboardImage() {
        return dashboardImage;
    }

    public void setDashboardImage(int dashboardImage) {
        this.dashboardImage = dashboardImage;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }
}
