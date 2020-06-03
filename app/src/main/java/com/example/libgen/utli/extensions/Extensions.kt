package com.example.libgen.utli.extensions

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.libgen.data.local.AppDatabase
import com.example.libgen.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

fun FragmentActivity.getMainViewModel(): MainViewModel = ViewModelProvider(this)
    .get(MainViewModel::class.java)
    .apply { setAppDB(getAppDB()) }

fun FragmentActivity.getAppDB(): AppDatabase = AppDatabase.getInstance(application)

fun Date.formatDate(): String {
    val dateFormat = "dd-MM-yyyy"
    // Parse Date
    val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
    return sdf.format(this)
}

fun Fragment.clearFocus() = requireActivity().clearFocus()

fun FragmentActivity.clearFocus() {
    currentFocus?.clearFocus()
}

fun Fragment.hideKeyboard() = requireActivity().hideKeyboard()

fun FragmentActivity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(findViewById<View>(android.R.id.content)?.windowToken, 0)
}

fun FragmentActivity.requiredPermissions(permissions: Array<String>): Array<String> {
    return permissions.filter { permission ->
        ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
    }.toTypedArray()
}

@RequiresApi(Build.VERSION_CODES.M)
fun FragmentActivity.shouldShowRational(permissions: Array<String>): Boolean {
    return permissions.all { shouldShowRequestPermissionRationale(it) }
}

fun FragmentActivity.showRationalDialog(title: String) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage("We need permissions to perform this action")
        .setPositiveButton("Ok") { _, _ ->
            showShortToast("Please enable The permission manually")

            val appSettingsIntent = Intent()
            appSettingsIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS

            val packageUri =
                Uri.fromParts("package", packageName, null)
            appSettingsIntent.data = packageUri
            startActivity(appSettingsIntent)
        }.show()
}

fun FragmentActivity.showShortToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Fragment.showShortSnackBar(message: String?) = requireActivity().showShortSnackBar(message)

fun FragmentActivity.showShortSnackBar(message: String?) {
    Snackbar.make(
        findViewById(android.R.id.content),
        message ?: "Something went wrong!",
        Snackbar.LENGTH_SHORT
    ).show()
}
