package com.example.fypintelidea.features.search

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.Search
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class SearchActivityViewModel(private val mainRepo: MainRepo) : BaseViewModel(mainRepo) {
    val searchObservable: MutableLiveData<Notification<Search>> = MutableLiveData()

    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is Search) {
            searchObservable.postValue(Notification.createOnNext(apiResponse))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        searchObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun getSearchList(search: String) {
        mainRepo.getSearchList(this, search)
    }
}