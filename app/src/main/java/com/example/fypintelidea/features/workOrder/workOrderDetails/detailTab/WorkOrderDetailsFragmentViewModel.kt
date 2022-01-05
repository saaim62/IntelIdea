package com.example.fypintelidea.features.workOrder.workOrderDetails.detailTab

import androidx.lifecycle.MutableLiveData
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import com.example.fypintelidea.core.providers.models.Team
import com.example.fypintelidea.core.providers.models.WorkOrderRecommendation
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers


class WorkOrderDetailsFragmentViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val workOrderRecommendationObservable: MutableLiveData<Notification<WorkOrderRecommendation>> =
        MutableLiveData()

    //    val uploadDocObservable: MutableLiveData<Notification<Document>> = MutableLiveData()
    val getAllTeamsObservable: MutableLiveData<Notification<ArrayList<Team>>> = MutableLiveData()


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is WorkOrderRecommendation) {
            workOrderRecommendationObservable.postValue(Notification.createOnNext(apiResponse))
        }
//        else if (apiResponse is Document) {
//            uploadDocObservable.postValue(Notification.createOnNext(apiResponse))
//        }
        else if (apiResponse.collection.isNotEmpty() && apiResponse.collection[0] is Team) {
            getAllTeamsObservable.postValue(Notification.createOnNext(apiResponse.collection as ArrayList<Team>))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        workOrderRecommendationObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
//        uploadDocObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
        getAllTeamsObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun getWorkOrderRecommendations(selectedWorkOrderId: String) {
        mainRepo.getWorkOrderRecommendations(this, selectedWorkOrderId)
    }

    fun getAllTeams() {
        mainRepo.getAllTeams(this)
    }

//    fun uploadDoc(
//        entity_id: RequestBody,
//        file: MultipartBody.Part
//    ) {
//        accountRepo.uploadDoc(this, entity_id, file)
//    }
}