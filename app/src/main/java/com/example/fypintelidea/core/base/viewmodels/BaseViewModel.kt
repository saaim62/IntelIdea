package com.example.fypintelidea.core.base.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fypintelidea.core.services.ApiCallbacks
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.BaseRepo
import okhttp3.Headers

open class BaseViewModel(private val baseRepo: BaseRepo) : ViewModel(), ApiCallbacks {

    override fun doBeforeApiCall() {

    }

    override fun doAfterApiCall() {
    }

//    override fun onNoNetworkAvailable() {
//    }

    override fun onApiFailure(errorCode: Int) {
    }

    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {

    }
}