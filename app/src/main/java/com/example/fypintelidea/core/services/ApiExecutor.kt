package com.example.fypintelidea.core.services

import android.content.Context
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.AlertDialogProvider
import com.example.fypintelidea.core.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiExecutor<T> : Callback<T> {
    private lateinit var mApiCallbacks: ApiCallbacks
    var showProgressBar: Boolean = true

    override fun onResponse(call: Call<T>, response: Response<T>) {
        mApiCallbacks.doAfterApiCall()
        if (response.isSuccessful) {
            val errorCodes = response.code()
            val headers = response.headers()
            try {
                val apiResponse = response.body() as ConnectavoBaseApiResponse
                mApiCallbacks.onApiSuccess(apiResponse, errorCodes, headers)
            } catch (e: Exception) {
                if (e is ClassCastException) {
                    val apiResponse = ConnectavoBaseApiResponse()
                    val collectionResponse = response.body() as List<Any>
                    apiResponse.collection = collectionResponse
                    mApiCallbacks.onApiSuccess(apiResponse, errorCodes, headers)
                } else {
                    throw e
                }
            }
        } else {
            doOnError(response)
        }
    }

    private fun doOnError(response: Response<T>) {
        mApiCallbacks.onApiFailure(response.code())
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        mApiCallbacks.doAfterApiCall()
        mApiCallbacks.onApiFailure(HttpErrorCodes.Unknown.code)
    }

    fun addCallToQueue(
        context: Context,
        apiCall: Call<T>,
        apiCallbacks: ApiCallbacks,
    ) {
        if (Utils.isNetworkAvailable(context)) {
            this.mApiCallbacks = apiCallbacks
            apiCallbacks.doBeforeApiCall()
            apiCall.enqueue(this)
        } else {
            AlertDialogProvider.showAlertDialog(context, context.getString(R.string.check_internet))
        }
    }
}