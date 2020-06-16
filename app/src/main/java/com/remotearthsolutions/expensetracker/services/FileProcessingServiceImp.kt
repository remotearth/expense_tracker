package com.remotearthsolutions.expensetracker.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils.show
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.Constants.Companion.KEY_UTF_VERSION
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getCurrentDate
import com.remotearthsolutions.expensetracker.utils.PermissionUtils
import java.io.*
import java.nio.charset.Charset
import java.util.*

class FileProcessingServiceImp(val context: Context) : FileProcessingService {
    private val writePermission: PermissionUtils = PermissionUtils()
    override fun writeOnCsvFile(
        activity: Activity,
        content: String?,
        onSuccessRunnable: Runnable?,
        onFailureRunnable: Runnable?
    ) {
        writePermission.writeExternalStoragePermission(activity, object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                if (writeExternalFile(activity, content)) {
                    onSuccessRunnable?.run()
                } else {
                    onFailureRunnable?.run()
                }
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                onFailureRunnable!!.run()
                if (response.isPermanentlyDenied) {
                    forceUserToGrantPermission(activity)
                } else {
                    show(
                        activity, "",
                        activity.getString(R.string.without_this_app_cannot_export),
                        activity.getString(R.string.ok), null, null, null
                    )
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest,
                token: PermissionToken
            ) {
                onFailureRunnable!!.run()
                show(activity, activity.getString(R.string.attention),
                    activity.getString(R.string.storage_permission_is_needed_to_export_data),
                    activity.getString(R.string.yes),
                    activity.getString(R.string.no),
                    null,
                    object : BaseView.Callback {
                        override fun onOkBtnPressed() {
                            token.continuePermissionRequest()
                        }

                        override fun onCancelBtnPressed() {
                            token.cancelPermissionRequest()
                        }
                    })
            }
        })
    }

    override fun readFromCsvFile(activity: Activity): List<CategoryExpense>? {
        val file = File(activity.getExternalFilesDir(null), createFileNameAccordingToDate())
        val categoryExpenseList: MutableList<CategoryExpense> = ArrayList()
        try {
            var line: String
            val fileReader =
                BufferedReader(FileReader(file))
            fileReader.readLine()
            while (fileReader.readLine().also { line = it } != null) {
                val tokens = line.split(", ").toTypedArray()
                if (tokens.isNotEmpty()) {
                    val categoryExpense =
                        CategoryExpense(
                            tokens[0].toInt(),
                            tokens[1],
                            tokens[2],
                            tokens[3].toDouble(),
                            tokens[4].toLong()
                        )
                    categoryExpenseList.add(categoryExpense)
                }
            }
            fileReader.close()
        } catch (e: Exception) {
            Log.d("Exception", "" + e.message)
        }
        return categoryExpenseList
    }

    override fun loadTableData(
        fileURI: Uri?,
        callback: FileProcessingService.Callback?
    ) {
        var expenseModels: List<ExpenseModel>? = null
        var categoryModels: List<CategoryModel>? = null
        var accountModels: List<AccountModel>? = null
        val inputStream = context.contentResolver.openInputStream(fileURI!!)

        var fileReader: BufferedReader? = null
        try {
            var line: String?

            fileReader = BufferedReader(InputStreamReader(inputStream!!))
            while (fileReader.readLine().also { line = it } != null) {
                when {
                    line!!.contains(Constants.KEY_META1_REPLACE) -> {
                        line = line!!.replace(
                            Constants.KEY_META1_REPLACE,
                            ""
                        )
                        val jsonContent = String(
                            Base64.decode(line, Base64.NO_WRAP), Charset.forName(KEY_UTF_VERSION)
                        )
                        expenseModels =
                            Gson().fromJson(jsonContent, Array<ExpenseModel>::class.java).toList()
                    }
                    line!!.contains(Constants.KEY_META2_REPLACE) -> {
                        line = line!!.replace(
                            Constants.KEY_META2_REPLACE,
                            ""
                        )
                        val jsonContent = String(
                            Base64.decode(line, Base64.NO_WRAP), Charset.forName(
                                KEY_UTF_VERSION
                            )
                        )
                        categoryModels =
                            Gson().fromJson(jsonContent, Array<CategoryModel>::class.java).toList()
                    }
                    line!!.contains(Constants.KEY_META3_REPLACE) -> {
                        line = line!!.replace(
                            Constants.KEY_META3_REPLACE,
                            ""
                        )
                        val jsonContent =
                            String(
                                Base64.decode(line, Base64.NO_WRAP),
                                Charset.forName(KEY_UTF_VERSION)
                            )
                        accountModels =
                            Gson().fromJson(jsonContent, Array<AccountModel>::class.java).toList()
                    }
                }
            }
            if (categoryModels == null || expenseModels == null || accountModels == null) {
                callback!!.onFailure()
            } else {
                callback!!.onComplete(categoryModels, expenseModels, accountModels)
            }
        } catch (e: Exception) {
            Log.d("Exception", "" + e.message)
            FirebaseCrashlytics.getInstance().recordException(e)
            e.printStackTrace()
            callback!!.onFailure()
        } finally {
            try {
                fileReader!!.close()
            } catch (e: IOException) {
                FirebaseCrashlytics.getInstance().recordException(e)
                Log.d("Exception", "" + e.message)
            }
        }
    }

    override val nameOfAllCsvFile: List<String>
        get() {
            val fileList: MutableList<String> =
                ArrayList()
            val dataDirectory =
                File(context.getExternalFilesDir(null)?.toURI())
            val listOfAllItems = dataDirectory.list()
            if (listOfAllItems != null && listOfAllItems.isNotEmpty()) {
                for (item in listOfAllItems) {
                    if (item.contains(Constants.KEY_EXPENSE_TRACKER)) {
                        fileList.add(item)
                    }
                }
            }
            return fileList
        }

    override fun shareFile(activity: Activity) {
        val emailAddress = ""
        val emailSubject = activity.getString(R.string.report_from_expense_tracker)
        try {
            val fileLocation =
                File(activity.getExternalFilesDir(null), createFileNameAccordingToDate())
            val uri = FileProvider.getUriForFile(
                activity,
                Constants.KEY_PROVIDER,
                fileLocation
            )
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "plain/text"
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
            emailIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(emailAddress)
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            activity.startActivity(
                Intent.createChooser(
                    emailIntent,
                    activity.getString(R.string.choose_email_client_to_send_report)
                )
            )
            with(AnalyticsManager) { logEvent(DATA_EXPORTED) }
        } catch (t: Throwable) {
            t.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(t)
            Toast.makeText(
                activity,
                activity.getString(R.string.report_sending_failed_please_try_again_later),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun writeExternalFile(activity: Activity, content: String?): Boolean {
        var isSuccess = true
        val file = File(activity.getExternalFilesDir(null), createFileNameAccordingToDate())
        var fw: FileWriter? = null
        var printWriter: PrintWriter? = null
        try {
            if (file.exists()) {
                file.delete()
                file.createNewFile()
            }
            fw = FileWriter(file, true)
            printWriter = PrintWriter(fw)
            printWriter.print(content)
        } catch (e: FileNotFoundException) {
            Log.d("error", "File Not Found")
            isSuccess = false
        } catch (io: IOException) {
            Log.d("error", "Error File Creating")
            isSuccess = false
        } finally {
            if (fw != null) {
                try {
                    fw.close()
                    printWriter!!.close()
                } catch (e: IOException) {
                    Log.d("error", "Error File Creating" + e.message)
                }
            }
        }
        return isSuccess
    }

    private fun forceUserToGrantPermission(activity: Activity) {
        writePermission.showSettingsDialog(activity)
    }

    private fun createFileNameAccordingToDate(): String {
        return Constants.KEY_EXPENSE_TRACKER + getCurrentDate(
            Constants.KEY_DATE_MONTH_YEAR_HOUR_MIN_SEC
        ) + Constants.KEY_CSV_FILE_EXTENSION
    }

}