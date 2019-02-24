package com.remotearthsolutions.expensetracker.utils;

import com.remotearthsolutions.expensetracker.R;

import java.util.HashMap;
import java.util.Map;

public class CategoryIcons {
    private Map<String,Integer> icons = new HashMap<String,Integer>(){
        {
            put("ic_bills", R.drawable.ic_bills);
            put("ic_taxi",R.drawable.ic_taxi);
        }
    };
}
