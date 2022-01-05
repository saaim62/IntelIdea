package com.example.fypintelidea.features.activities

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.TagsResponse
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class TagsActivityViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val tagsObservable: MutableLiveData<Notification<TagsResponse>> = MutableLiveData()


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is TagsResponse) {
            tagsObservable.postValue(Notification.createOnNext(apiResponse))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        tagsObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun getAllTags() {
        mainRepo.getAllTags(this)
    }
}
