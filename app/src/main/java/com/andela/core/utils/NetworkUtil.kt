package com.andela.core.utils

import android.content.Context
import com.andela.currencyexcercise.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object NetworkUtil {

    var baseUrl: String = "https://api.apilayer.com/fixer/"
    var apiKey: String = "a39xPJ41EWBhJnPCnPf9UPx2ZlyJD0I3"


    fun connectionAvailable(context: Context): Boolean {
        val cd = ConnectionUtil(context)
        return cd.isConnectingToInternet
    }

    fun showDialog(context: Context) {
        val title = context.getString(R.string.alert)
        val message =
            context.getString(R.string.no_Internet)
        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                context.getString(R.string.label_ok)
            ) { dialogInterface, i ->

            }
            .setCancelable(false)
            .show()
    }
}