package com.example.fypintelidea.features.profile

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.UserProfileResponse
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class ProfileActivityViewModel(private val mainRepo: MainRepo) : BaseViewModel(mainRepo) {
    val userProfileObservable: MutableLiveData<Notification<UserProfileResponse>> =
        MutableLiveData()

    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is UserProfileResponse) {
            userProfileObservable.postValue(Notification.createOnNext(apiResponse))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        userProfileObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun fetchUserProfile(user: String?) {
        if (user != null) {
            mainRepo.fetchUserProfile(this, user)
        }
    }
}
