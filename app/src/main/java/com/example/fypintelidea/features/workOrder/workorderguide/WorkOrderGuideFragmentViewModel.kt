package com.example.fypintelidea.features.workOrder.workorderguide

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.RecommendationFeedbackRequest
import com.example.fypintelidea.core.providers.models.RecommendationFeedbackResponse
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers

class WorkOrderGuideFragmentViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val workOrderGuideObservable: MutableLiveData<Notification<RecommendationFeedbackResponse>> =
        MutableLiveData()

    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is RecommendationFeedbackResponse) {
            workOrderGuideObservable.postValue(Notification.createOnNext(apiResponse))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        workOrderGuideObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun postRecommendationFeedback(recommendationFeedbackRequest: RecommendationFeedbackRequest) {
        mainRepo.postRecommendationFeedback(this, recommendationFeedbackRequest)
    }
}
