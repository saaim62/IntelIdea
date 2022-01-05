package com.example.fypintelidea.features.workOrder.newWorkorder

import androidx.lifecycle.MutableLiveData
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import com.example.fypintelidea.core.providers.models.ChklistTemplateResponse
import com.example.fypintelidea.core.providers.models.User
import com.example.fypintelidea.core.providers.models.Workorder
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NewWorkOrderActivityViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val multiPartsWorkOrderObservable: MutableLiveData<Notification<Workorder>> =
        MutableLiveData()
    val getAllTemplatesObservable: MutableLiveData<Notification<ArrayList<ChklistTemplateResponse>>> =
        MutableLiveData()
    val fetchAllUsersObservable: MutableLiveData<Notification<ArrayList<User>>> = MutableLiveData()


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is Workorder) {
            multiPartsWorkOrderObservable.postValue(Notification.createOnNext(apiResponse))
        } else if (apiResponse.collection.isNotEmpty() && apiResponse.collection[0] is ChklistTemplateResponse) {
            getAllTemplatesObservable.postValue(Notification.createOnNext(apiResponse.collection as ArrayList<ChklistTemplateResponse>))
        } else if (apiResponse.collection.isNotEmpty() && apiResponse.collection[0] is User) {
            fetchAllUsersObservable.postValue(Notification.createOnNext(apiResponse.collection as ArrayList<User>))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        fetchAllUsersObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
        getAllTemplatesObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
        multiPartsWorkOrderObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
    }

    fun fetchAllUsers() {
        mainRepo.fetchAllUsers(this)
    }

    fun getAllTemplates() {
        mainRepo.getAllTemplates(this)
    }

    fun createWorkOrderMultipart(
        name: RequestBody?,
        asset_id: RequestBody?,
        modified_by: RequestBody?,
        status: RequestBody?,
        company_id: RequestBody?,
        hashed: RequestBody?,
        assigned_to: RequestBody?,
        team_ids: List<String>?,
        due_date: RequestBody?,
        priority: RequestBody?,
        eta: RequestBody?,
        category: RequestBody?,
        description: RequestBody?,
        chklistTemplateId: RequestBody?,
        tag_ids: List<String>?,
        mapSpareParts: Map<String, String>?,
        mapCustomFields: Map<String, String>?,
        files: Array<MultipartBody.Part?>,
    ) {
        mainRepo.createWorkOrderMultipart(
            this,
            name,
            asset_id,
            modified_by,
            status,
            company_id,
            hashed,
            assigned_to,
            team_ids,
            due_date,
            priority,
            eta,
            category,
            description,
            chklistTemplateId,
            tag_ids,
            mapSpareParts,
            mapCustomFields,
            files
        )
    }
}