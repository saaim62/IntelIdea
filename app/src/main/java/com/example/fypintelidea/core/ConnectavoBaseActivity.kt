package com.example.fypintelidea.core

import androidx.appcompat.app.AppCompatActivity
import com.example.fypintelidea.core.services.ApiCallbacks
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import okhttp3.Headers

abstract class ConnectavoBaseActivity : AppCompatActivity(), ApiCallbacks {

    override fun doBeforeApiCall() {}

    override fun doAfterApiCall() {}

    override fun onApiFailure(errorCode: Int) {
//        AlertDialogProvider.showFailureDialog(this)
    }

    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        hearders: Headers
    ) {
    }

    companion object {
        internal const val REQUEST_EXIT = 500
    }
}