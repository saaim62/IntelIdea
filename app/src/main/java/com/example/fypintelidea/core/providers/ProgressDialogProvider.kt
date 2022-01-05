package com.example.fypintelidea.core.providers

import android.annotation.SuppressLint
import android.content.Context
import com.kaopiz.kprogresshud.KProgressHUD

object ProgressDialogProvider {
    var counter = 0

    @SuppressLint("StaticFieldLeak")
    private var progressHud: KProgressHUD? = null

    fun show(context: Context) {
        counter++
        if (progressHud == null || progressHud?.isShowing == false) {
            progressHud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
        }
    }

    fun dismiss() {
        counter--
        if (counter == 0 && progressHud?.isShowing == true) {
            progressHud?.dismiss()
        }
    }
}