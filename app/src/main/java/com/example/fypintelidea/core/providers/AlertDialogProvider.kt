package com.example.fypintelidea.core.providers

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.fypintelidea.R

object AlertDialogProvider {

    fun showFailureDialog(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setTitle(context.getString(R.string.error))
        dialogBuilder.setMessage(context.getString(R.string.sorry_something_went_wrong))
        dialogBuilder.setPositiveButton(android.R.string.yes) { dialog, which -> }
        dialogBuilder.show()
    }

    fun showAlertDialog(context: Context, message: String?) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setTitle(context.getString(R.string.alert))
        dialogBuilder.setMessage(message)
        dialogBuilder.setPositiveButton(android.R.string.yes) { dialog, which -> }
        dialogBuilder.show()
    }

    fun showAlertDialog(
        context: Context,
        message: String?,
        yesBtnText: String?,
        yesBtnClickListener: DialogInterface.OnClickListener
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setTitle(context.getString(R.string.alert))
        dialogBuilder.setMessage(message)
        dialogBuilder.setPositiveButton(yesBtnText, yesBtnClickListener)
        dialogBuilder.show()
    }
}