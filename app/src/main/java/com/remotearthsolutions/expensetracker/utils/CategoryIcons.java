package com.remotearthsolutions.expensetracker.utils;

import com.remotearthsolutions.expensetracker.R;

import java.util.HashMap;
import java.util.Map;

public class CategoryIcons {
    private static Map<String, Integer> icons = new HashMap<String, Integer>() {
        {
            put("cat_bills", R.drawable.cat_bills);
            put("cat_bills1", R.drawable.cat_bills1);
            put("cat_bills2", R.drawable.cat_bills2);
            put("cat_bills3", R.drawable.cat_bills3);
            put("cat_bills4", R.drawable.cat_bills4);
            put("cat_bills5", R.drawable.cat_bills5);
            put("cat_car", R.drawable.cat_car);
            put("cat_car1", R.drawable.cat_car1);
            put("cat_car2", R.drawable.cat_car2);
            put("cat_car3", R.drawable.cat_car3);
            put("cat_car4", R.drawable.cat_car4);
            put("cat_clothes", R.drawable.cat_clothes);
            put("cat_clothes1", R.drawable.cat_clothes1);
            put("cat_clothes2", R.drawable.cat_clothes2);
            put("cat_clothes3", R.drawable.cat_clothes3);
            put("cat_clothes4", R.drawable.cat_clothes4);
            put("cat_clothes5", R.drawable.cat_clothes5);
            put("cat_communication", R.drawable.cat_communication);
            put("cat_communication1", R.drawable.cat_communication1);
            put("cat_communication2", R.drawable.cat_communication2);
            put("cat_communication3", R.drawable.cat_communication3);
            put("cat_communication4", R.drawable.cat_communication4);
            put("cat_communication5", R.drawable.cat_communication5);
            put("cat_eatingout", R.drawable.cat_eatingout);
            put("cat_eatingout1", R.drawable.cat_eatingout1);
            put("cat_eatingout2", R.drawable.cat_eatingout2);
            put("cat_eatingout3", R.drawable.cat_eatingout3);
            put("cat_eatingout4", R.drawable.cat_eatingout4);
            put("cat_eatingout5", R.drawable.cat_eatingout5);
            put("cat_entertainment", R.drawable.cat_entertainment);
            put("cat_entertainment1", R.drawable.cat_entertainment1);
            put("cat_entertainment2", R.drawable.cat_entertainment2);
            put("cat_entertainment3", R.drawable.cat_entertainment3);
            put("cat_entertainment4", R.drawable.cat_entertainment4);
            put("cat_entertainment5", R.drawable.cat_entertainment5);
            put("cat_food", R.drawable.cat_food);
            put("cat_gift", R.drawable.cat_gift);
            put("cat_health", R.drawable.cat_health);
            put("cat_pets", R.drawable.cat_pets);
            put("cat_sports", R.drawable.cat_sports);
            put("cat_taxi", R.drawable.cat_taxi);
            put("cat_toiletry", R.drawable.cat_toiletry);
            put("cat_transport", R.drawable.cat_transport);
            put("cat_family", R.drawable.cat_family);

        }
    };

    public static int getIconId(String name) {
        return icons.get(name);
    }

    public static Map<String, Integer> getAllIcons() {
        return icons;
    }
}
