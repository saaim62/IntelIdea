package com.example.fypintelidea.features.workOrder

import androidx.lifecycle.MutableLiveData
import com.example.fypintelidea.core.providers.models.UpdateWOStatusResponse
import com.example.fypintelidea.core.providers.models.Workorder
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import io.reactivex.rxjava3.core.Notification
import okhttp3.Headers
import org.json.JSONObject
import java.util.*

class WorkOrdersFragmentTabViewModel(private val mainRepo: MainRepo) :
    BaseViewModel(mainRepo) {
    val wosObservable: MutableLiveData<Notification<UpdateWOStatusResponse>> = MutableLiveData()
    val submitCheckListMultiPartsObservable: MutableLiveData<Notification<Workorder>> =
        MutableLiveData()
    val getAllWorkOrdersObservable: MutableLiveData<Notification<ArrayList<Workorder>>> =
        MutableLiveData()

    private var nextPage: String? = null
    var totalWorkOrders: String? = null
    var list: ArrayList<Workorder> = arrayListOf()
    var lodingList: ArrayList<Any> = arrayListOf()
    var queryParam: Int? = null

    init {
        queryParam = 1
    }

    fun fetchNewData(
        myWorkOrders: String? = null,
        dueDateGreaterThanEqualToToday: String? = null,
        dueDateLessThanEqualToToday: String? = null,
        dueDateLessThanEqualToOverDue: String? = null,
        type: List<String>,
        status: List<String>,
        priority: List<String>,
        employees: List<String>,
        teams: List<String>,
        assets: List<String>,
        selectedIncludeChildrenAssets: String? = null,
        selectedIncludeChklistAssets: String? = null,
        category: List<String>,
        tags: List<String>,
        dueDateGreaterThanEqualToDatePicker: String? = null,
        dueDateLessThanEqualToDatePicker: String? = null,
        sortby: String? = null,
    ) {
        list.clear()
        queryParam = DEFAULT_PAGE_NUMBER
        queryParam?.let {
            mainRepo.getAllWorkOrders(
                this,
                it,
                myWorkOrders,
                dueDateGreaterThanEqualToToday,
                dueDateLessThanEqualToToday,
                dueDateLessThanEqualToOverDue,
                type,
                status,
                priority,
                employees,
                teams,
                assets,
                selectedIncludeChildrenAssets,
                selectedIncludeChklistAssets,
                category,
                tags,
                dueDateGreaterThanEqualToDatePicker,
                dueDateLessThanEqualToDatePicker,
                sortby,
            )
        }
    }

    fun fetchMore(
        myWorkOrders: String? = null,
        dueDateGreaterThanEqualToToday: String? = null,
        dueDateLessThanEqualToToday: String? = null,
        dueDateLessThanEqualToOverDue: String? = null,
        type: List<String>,
        status: List<String>,
        priority: List<String>,
        employees: List<String>,
        teams: List<String>,
        assets: List<String>,
        selectedIncludeChildrenAssets: String? = null,
        selectedIncludeChklistAssets: String? = null,
        category: List<String>,
        tags: List<String>,
        dueDateGreaterThanEqualToDatePicker: String? = null,
        dueDateLessThanEqualToDatePicker: String? = null,
        sortby: String? = null,
    ) {
        if (!nextPage.isNullOrEmpty()) {
            queryParam?.let {
                mainRepo.getAllWorkOrders(
                    this,
                    it,
                    myWorkOrders,
                    dueDateGreaterThanEqualToToday,
                    dueDateLessThanEqualToToday,
                    dueDateLessThanEqualToOverDue,
                    type,
                    status,
                    priority,
                    employees,
                    teams,
                    assets,
                    selectedIncludeChildrenAssets,
                    selectedIncludeChklistAssets,
                    category,
                    tags,
                    dueDateGreaterThanEqualToDatePicker,
                    dueDateLessThanEqualToDatePicker,
                    sortby,
                )
            }
        }
    }


    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        headers: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, headers)

        if (apiResponse is UpdateWOStatusResponse) {
            wosObservable.postValue(Notification.createOnNext(apiResponse))
        } else if (apiResponse is Workorder) {
            submitCheckListMultiPartsObservable.postValue(Notification.createOnNext(apiResponse))
        } else if (apiResponse.collection.isNotEmpty() && apiResponse.collection[0] is Workorder) {
            list.addAll(apiResponse.collection as ArrayList<Workorder>)
            val headerPagination = headers["X-Pagination"]
            if (headerPagination != null) {
                val jsonObj =
                    JSONObject(headerPagination)
                nextPage = jsonObj.getString("next_page")
                totalWorkOrders = jsonObj.getString("total")
                if (nextPage != "null") {
                    nextPage?.let { nextPage ->
                        queryParam = nextPage.toInt()
                    }
                } else {
                    list.clear()
                    list.addAll(apiResponse.collection as ArrayList<Workorder>)
                }
//                val loadingItem = LoadingItem()
//                loadingItem.animation = R.drawable.dot_animation
//                lodingList.add(loadingItem)
            }
            this.totalWorkOrders = totalWorkOrders
            getAllWorkOrdersObservable.postValue(Notification.createOnNext(list))
        } else {
            //this response handling is temp need to fix this ASAP
            totalWorkOrders = "0"
            getAllWorkOrdersObservable.postValue(Notification.createOnNext(list))
        }
    }


    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        getAllWorkOrdersObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
        wosObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
        submitCheckListMultiPartsObservable.postValue(Notification.createOnError(Throwable(errorCode.toString())))
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

    companion object {
        const val DEFAULT_PAGE_NUMBER = 1
    }
}