package com.example.fypintelidea.features.workOrder.workOrderCompletion

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.Workorder
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers
import okhttp3.MultipartBody
import java.util.*

class WorkOrderCompleteSignatureActivityViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val workOrderSignatureObservable: MutableLiveData<Notification<Workorder>> = MutableLiveData()


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is Workorder) {
            workOrderSignatureObservable.postValue(Notification.createOnNext(apiResponse))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        workOrderSignatureObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun updateWorkOrderDone(
        workorder: Workorder,
        mapSparePartHavingWorkOrderSparePartId: HashMap<String, String>,
        mapSparePartHavingOnlySparePartId: HashMap<String, String>,
        mapChecklist: HashMap<String, String>,
        mapContributor: HashMap<String, String>,
        mapCost: HashMap<String, String>,
        filePart: MultipartBody.Part
    ) {
        mainRepo.updateWorkOrderDone(
            this,
            workorder,
            mapSparePartHavingWorkOrderSparePartId,
            mapSparePartHavingOnlySparePartId,
            mapChecklist,
            mapContributor,
            mapCost,
            filePart
        )
    }
}