package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;

public interface FileProcessingService {
    void writeOnCsvFile(Activity activity, String content);
    void shareFile(Activity activity);
}
