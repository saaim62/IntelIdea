package com.example.fypintelidea.features.workOrder.workOrderCompletion

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.User
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers
import java.util.*

class WorkOrderCompleteActivityViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val getAllUsersObservable: MutableLiveData<Notification<ArrayList<User>>> = MutableLiveData()


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse.collection.isNotEmpty() && apiResponse.collection[0] is User) {
            getAllUsersObservable.postValue(Notification.createOnNext(apiResponse.collection as ArrayList<User>))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        getAllUsersObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun fetchAllUsers() {
        mainRepo.fetchAllUsers(this)
    }
}
