package com.example.fypintelidea.core.base.models

import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.core.services.ApiCallbacks
import com.example.fypintelidea.core.services.ApiManager
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.providers.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*


class MainRepo(
    private val apiManager: ApiManager, private val sessionManager: SessionManager

) : BaseRepo(apiManager, sessionManager) {

    fun userLogin(apiCallbacks: ApiCallbacks, userLogin: UserLoginRequest) {
        apiManager.userLogin(apiCallbacks, userLogin)
    }

    fun fetchAssetsFromServer(apiCallbacks: ApiCallbacks, verbosity: String) {
        apiManager.fetchAssetsFromServer(apiCallbacks, verbosity)
    }

    fun fetchAssetLevelsFromServer(apiCallbacks: ApiCallbacks) {
        apiManager.fetchAssetLevelsFromServer(apiCallbacks)
    }

    fun fetchUserProfile(
        apiCallbacks: ApiCallbacks,
        user: String
    ) {
        apiManager.fetchUserProfile(apiCallbacks, user)
    }

    fun fetchAllUsers(
        apiCallbacks: ApiCallbacks
    ) {
        apiManager.fetchAllUsers(apiCallbacks)
    }

    fun getAllTemplates(
        apiCallbacks: ApiCallbacks
    ) {
        apiManager.getAllTemplates(apiCallbacks)
    }

    fun updateUserProfile(
        apiCallbacks: ApiCallbacks,
        rootUser: RootUser
    ) {
        apiManager.updateUserProfile(apiCallbacks, rootUser)
    }

    fun uploadDoc(
        apiCallbacks: ApiCallbacks,
        entity_id: RequestBody,
        file: MultipartBody.Part
    ) {
        apiManager.uploadDoc(apiCallbacks, entity_id, file)
    }

    fun updateWorkOrderToOpenState(
        apiCallbacks: ApiCallbacks,
        workorder: Workorder
    ) {
        apiManager.updateWorkOrderToOpenState(apiCallbacks, workorder)
    }

    fun updateWorkOrderToInProgress(
        apiCallbacks: ApiCallbacks,
        workorder: Workorder
    ) {
        apiManager.updateWorkOrderToInProgress(apiCallbacks, workorder)
    }

    fun updateWorkOrderToPauseState(
        apiCallbacks: ApiCallbacks,
        workorder: Workorder
    ) {
        apiManager.updateWorkOrderToPauseState(apiCallbacks, workorder)
    }

    fun updateWorkOrderDone(
        apiCallbacks: ApiCallbacks,
        workorder: Workorder,
        mapSparePartHavingWorkOrderSparePartId: HashMap<String, String>,
        mapSparePartHavingOnlySparePartId: HashMap<String, String>,
        mapChecklist: HashMap<String, String>,
        mapContributor: HashMap<String, String>,
        mapCost: HashMap<String, String>,
        filePart: MultipartBody.Part
    ) {
        apiManager.updateWorkOrderDone(
            apiCallbacks,
            workorder,
            mapSparePartHavingWorkOrderSparePartId,
            mapSparePartHavingOnlySparePartId,
            mapChecklist,
            mapContributor,
            mapCost,
            filePart
        )

    }

    fun createWorkOrderMultipart(
        apiCallbacks: ApiCallbacks,
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
        apiManager.createWorkOrderMultipart(
            apiCallbacks,
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

    fun createManualRequestMultipart(
        apiCallbacks: ApiCallbacks,
        name: RequestBody,
        asset_id: RequestBody,
        modified_by: RequestBody,
        requested_by: RequestBody,
        status: RequestBody,
        hashed: RequestBody,
        description: RequestBody?,
        tag_ids: List<String>,
        files: Array<MultipartBody.Part?>,
    ) {
        apiManager.createManualRequestMultipart(
            apiCallbacks,
            name,
            asset_id,
            modified_by,
            requested_by,
            status,
            hashed,
            description,
            tag_ids,
            files
        )
    }

    fun createTemplateRequestMultipart(
        apiCallbacks: ApiCallbacks,
        asset_id: RequestBody,
        request_template_id: RequestBody,
        modified_by: RequestBody,
        requested_by: RequestBody,
        status: RequestBody,
        hashed: RequestBody,
        description: RequestBody?,
        due_date: RequestBody,
        files: Array<MultipartBody.Part?>,
    ) {
        apiManager.createTemplateRequestMultipart(
            apiCallbacks,
            asset_id,
            request_template_id,
            modified_by,
            requested_by,
            status,
            hashed,
            description,
            due_date,
            files,
        )
    }

    fun submitChecklistSectionAssetVerification(
        apiCallbacks: ApiCallbacks,
        item_id: RequestBody,
        asset_id: RequestBody
    ) {
        apiManager.submitChecklistSectionAssetVerification(
            apiCallbacks,
            item_id,
            asset_id
        )
    }

    fun submitChecklistItemAssetVerification(
        apiCallbacks: ApiCallbacks,
        item_id: RequestBody,
        asset_id: RequestBody
    ) {
        apiManager.submitChecklistItemAssetVerification(
            apiCallbacks,
            item_id,
            asset_id
        )
    }

    fun submitChklistMultipart(
        apiCallbacks: ApiCallbacks,
        chklistId: String,
        mapChecklist: Map<String, String>
    ) {
        apiManager.submitChklistMultipart(
            apiCallbacks,
            chklistId,
            mapChecklist
        )
    }

    fun getAllRequests(
        apiCallbacks: ApiCallbacks,
        filters: Map<String, String>
    ) {
        apiManager.getAllRequests(
            apiCallbacks,
            filters
        )
    }

    fun getAllWorkOrders(
        apiCallbacks: ApiCallbacks,
        page: Int,
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
        apiManager.getAllWorkOrders(
            apiCallbacks,
            page,
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

    fun getAllTags(
        apiCallbacks: ApiCallbacks
    ) {
        sessionManager.getString(SessionManager.KEY_LOGIN_COMPANY_ID)?.let {
            apiManager.getAllTags(
                apiCallbacks,
                it
            )
        }
    }

    fun getAllSpareParts(
        apiCallbacks: ApiCallbacks
    ) {
        apiManager.getAllSpareParts(
            apiCallbacks
        )
    }

    fun getAllTeams(
        apiCallbacks: ApiCallbacks
    ) {
        apiManager.getAllTeams(
            apiCallbacks
        )
    }

    fun getArticle(
        apiCallbacks: ApiCallbacks,
        userId: String
    ) {
        apiManager.getArticle(
            apiCallbacks,
            userId
        )
    }

    fun getWorkOrderRecommendations(
        apiCallbacks: ApiCallbacks,
        workOrderId: String
    ) {
        apiManager.getWorkOrderRecommendations(
            apiCallbacks,
            workOrderId
        )
    }

    fun postRecommendationFeedback(
        apiCallbacks: ApiCallbacks,
        recommendationFeedbackRequest: RecommendationFeedbackRequest
    ) {
        apiManager.postRecommendationFeedback(
            apiCallbacks,
            recommendationFeedbackRequest
        )
    }

    fun updateAsset(
        apiCallbacks: ApiCallbacks,
        assetId: String,
        rootAssetUpdateRequest: RootAssetUpdateRequest
    ) {
        apiManager.updateAsset(
            apiCallbacks,
            assetId,
            rootAssetUpdateRequest
        )
    }

    fun getSingleAsset(
        apiCallbacks: ApiCallbacks,
        userId: String
    ) {
        apiManager.getSingleAsset(
            apiCallbacks,
            userId
        )
    }

    fun getSingleWorkOrder(
        apiCallbacks: ApiCallbacks,
        userId: String
    ) {
        apiManager.getSingleWorkOrder(
            apiCallbacks,
            userId
        )
    }

    fun getSearchList(
        apiCallbacks: ApiCallbacks,
        search: String
    ) {
        apiManager.getSearchList(
            apiCallbacks,
            search
        )
    }

    fun getCompanyDetails(
        apiCallbacks: ApiCallbacks
    ) {
        sessionManager.getString(SessionManager.KEY_LOGIN_COMPANY_ID)?.let {
            apiManager.getCompanyDetails(
                apiCallbacks,
                it
            )
        }
    }

    fun getUserPermissions(
        apiCallbacks: ApiCallbacks,
    ) {
        sessionManager.getString(SessionManager.KEY_LOGIN_ID)?.let {
            apiManager.getUserPermissions(
                apiCallbacks,
                it
            )
        }
    }

    fun getSortByAssets(
        apiCallbacks: ApiCallbacks,
        level: String?,
        verbosity: String,
        sort: String?,
        direction: String?,
        parentId: String?,
        employees: ArrayList<String>?,
        teams: ArrayList<String>?,
        dateRange: String?,
        assetsSchedule: Boolean?,
        customField: ArrayList<String>?,
        customField2: ArrayList<String>?,
    ) {
        apiManager.getSortByAssets(
            apiCallbacks = apiCallbacks,
            level = level,
            verbosity = verbosity,
            sort = sort,
            direction = direction,
            parentId = parentId,
            employees = employees,
            teams = teams,
            dateRange = dateRange,
            assetsSchedule = assetsSchedule,
            customField = customField,
            customField2 = customField2
        )
    }
}