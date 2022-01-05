package com.example.fypintelidea.features.on_boarding

import androidx.lifecycle.MutableLiveData
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import com.example.fypintelidea.core.providers.models.*
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class LoginViewModel(private val mainRepo: MainRepo) : BaseViewModel(mainRepo) {
    val loginObservable: MutableLiveData<Notification<Login>> = MutableLiveData()
    val userProfileObservable: MutableLiveData<Notification<UserProfileResponse>> =
        MutableLiveData()
    val companyDetailsObservable: MutableLiveData<Notification<CompanyDetails>> = MutableLiveData()
    val assetObservable: MutableLiveData<Notification<ArrayList<Asset>>> = MutableLiveData()

    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is Login) {
            loginObservable.postValue(Notification.createOnNext(apiResponse))
        } else if (apiResponse is UserProfileResponse) {
            userProfileObservable.postValue(Notification.createOnNext(apiResponse))
        } else if (apiResponse is CompanyDetails) {
            companyDetailsObservable.postValue(Notification.createOnNext(apiResponse))
        } else if (apiResponse.collection.isNotEmpty() && apiResponse.collection[0] is Asset) {
            assetObservable.postValue(Notification.createOnNext(apiResponse.collection as ArrayList<Asset>))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        loginObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun login(loginRequest: UserLoginRequest) {
        mainRepo.userLogin(this, loginRequest)
    }

    fun fetchAssetsFromServer() {
        mainRepo.fetchAssetsFromServer(this, "high")
    }

    fun fetchUserProfile(user: String) {
        mainRepo.fetchUserProfile(this, user)
    }

    fun getCompanyDetails() {
        mainRepo.getCompanyDetails(this)
    }
}
