package com.example.fypintelidea.features.on_boarding

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.CompanyDetails
import com.example.fypintelidea.core.providers.models.PermissionResponse
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class DashboardActivityViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val companyDetailsObservable: MutableLiveData<Notification<CompanyDetails>> = MutableLiveData()
    val permissionObservable: MutableLiveData<Notification<PermissionResponse>> = MutableLiveData()


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is CompanyDetails) {
            companyDetailsObservable.postValue(Notification.createOnNext(apiResponse))
        } else if (apiResponse is PermissionResponse) {
            permissionObservable.postValue(Notification.createOnNext(apiResponse))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        companyDetailsObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
        permissionObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun getUserPermissions() {
        mainRepo.getUserPermissions(this)
    }

    fun getCompanyDetails() {
        mainRepo.getCompanyDetails(this)
    }
}
