package com.example.fypintelidea.features.on_boarding

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.Asset
import com.example.fypintelidea.core.providers.models.User
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class SplashScreenViewModel(private val mainRepo: MainRepo) : BaseViewModel(mainRepo) {
    val assetObservable: MutableLiveData<Notification<ArrayList<Asset>>> = MutableLiveData()
    val getAllUsersObservable: MutableLiveData<Notification<ArrayList<User>>> = MutableLiveData()

    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)
        if (apiResponse.collection.isNotEmpty() && apiResponse.collection[0] is Asset) {
            assetObservable.postValue(Notification.createOnNext(apiResponse.collection as ArrayList<Asset>))
        } else if (apiResponse.collection.isNotEmpty() && apiResponse.collection[0] is User) {
            getAllUsersObservable.postValue(Notification.createOnNext(apiResponse.collection as ArrayList<User>))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        assetObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun fetchAssetsFromServer() {
        mainRepo.fetchAssetsFromServer(this, "medium")
    }

    fun fetchAllUsers() {
        mainRepo.fetchAllUsers(this)
    }
}
