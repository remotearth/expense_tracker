package com.remotearthsolutions.expensetracker.utils;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.remotearthsolutions.expensetracker.BuildConfig;

public class FabricAnswersUtils {

    public static void logCustom(String message) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logCustom(new CustomEvent(message));
        }

    }
}
