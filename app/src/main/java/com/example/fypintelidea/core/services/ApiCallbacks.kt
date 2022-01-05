package com.example.fypintelidea.core.services

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import okhttp3.Headers

interface ApiCallbacks {

    fun doBeforeApiCall()

    fun doAfterApiCall()

    fun onApiFailure(errorCode: Int)

    fun onApiSuccess(apiResponse: ConnectavoBaseApiResponse, errorCode: Int, headers: Headers)
}