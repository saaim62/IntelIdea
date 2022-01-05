package com.example.fypintelidea.features.workOrder.workOrderDetails

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.UpdateWOStatusResponse
import com.example.fypintelidea.core.providers.models.Workorder
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class WorkOrderDetailActivityViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val workOrderObservable: MutableLiveData<Notification<Workorder>> =
        MutableLiveData()

    val wosObservable: MutableLiveData<Notification<UpdateWOStatusResponse>> =
        MutableLiveData()


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is Workorder) {
            workOrderObservable.postValue(Notification.createOnNext(apiResponse))
        } else if (apiResponse is UpdateWOStatusResponse) {
            wosObservable.postValue(Notification.createOnNext(apiResponse))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        workOrderObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
        wosObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun getSingleWorkOrder(singleWorkOrderId: String) {
        mainRepo.getSingleWorkOrder(this, singleWorkOrderId)
    }

    fun submitChklistMultipart(
        chklistId: String,
        mapChecklist: Map<String, String>
    ) {
        mainRepo.submitChklistMultipart(this, chklistId, mapChecklist)
    }

    fun updateWorkOrderToOpenState(workOrder: Workorder) {
        mainRepo.updateWorkOrderToOpenState(this, workOrder)
    }

    fun updateWorkOrderToInProgress(workOrder: Workorder) {
        mainRepo.updateWorkOrderToInProgress(this, workOrder)
    }

    fun updateWorkOrderToPauseState(workOrder: Workorder) {
        mainRepo.updateWorkOrderToPauseState(this, workOrder)
    }

}