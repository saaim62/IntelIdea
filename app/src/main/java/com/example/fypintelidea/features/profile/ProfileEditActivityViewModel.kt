package com.example.fypintelidea.features.profile

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.RootUser
import com.example.fypintelidea.core.providers.models.User
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class ProfileEditActivityViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val userProfileEditObservable: MutableLiveData<Notification<User>> = MutableLiveData()


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is User) {
            userProfileEditObservable.postValue(Notification.createOnNext(apiResponse))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        userProfileEditObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun updateUserProfile(rootUser: RootUser) {
        mainRepo.updateUserProfile(this, rootUser)
    }
}
