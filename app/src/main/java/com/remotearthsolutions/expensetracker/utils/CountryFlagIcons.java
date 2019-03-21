package com.remotearthsolutions.expensetracker.utils;

import com.remotearthsolutions.expensetracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryFlagIcons {

        private static Map<String, Integer> Countryflagicons = new HashMap<String, Integer>() {
        {
            put("flag_afghanistan", R.drawable.flag_afghanistan);

        }
    };

    public static int getIconId(String name) {
        return Countryflagicons.get(name);
    }

    public static List<String> getAllCountryIcons() {
        return new ArrayList<>(Countryflagicons.keySet());
    }
}
