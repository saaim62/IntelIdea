package com.example.fypintelidea.features.filters

import androidx.lifecycle.MutableLiveData
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import com.example.fypintelidea.core.providers.models.SparePartsResponse
import com.example.fypintelidea.core.providers.models.User
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class SingleSelectComponentActivityViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val sparePartsObservable: MutableLiveData<Notification<SparePartsResponse>> = MutableLiveData()
    val userObservable: MutableLiveData<Notification<List<User>>> = MutableLiveData()


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is SparePartsResponse) {
            sparePartsObservable.postValue(Notification.createOnNext(apiResponse))
        }else if(apiResponse.collection.isNotEmpty() && apiResponse.collection[0] is User){
            userObservable.postValue(Notification.createOnNext(apiResponse.collection as ArrayList<User>))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        sparePartsObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
        userObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun getAllSpareParts() {
        mainRepo.getAllSpareParts(this)
    }

    fun fetchAllUsers() {
        mainRepo.fetchAllUsers(this)
    }
}
