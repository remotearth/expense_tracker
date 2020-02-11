package com.remotearthsolutions.expensetracker.activities.helpers

import android.content.Context
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.services.FirebaseService
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils


class SyncManager(
    val context: Context,
    val firebaseService: FirebaseService,
    val isPremium: Boolean,
    val isInternetAvailalbe: Boolean,
    val isLoggedIn: Boolean
) {

    fun sync() {

    }
}