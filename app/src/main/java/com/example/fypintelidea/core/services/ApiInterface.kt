package com.example.fypintelidea.core.services

import com.example.fypintelidea.core.EndPoints
import com.example.fypintelidea.core.HeaderValues
import com.example.fypintelidea.core.Key
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.core.providers.models.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @PUT(EndPoints.API_UPDATE_PROFILE)
    fun updateProfile(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Body rootUser: RootUser,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<User>

    @GET(EndPoints.API_GET_PROFILE)
    fun getProfile(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<UserProfileResponse>

    @GET(EndPoints.API_GET_ALL_USERS)
    fun getAllUsers(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<List<User>>

    @PUT(EndPoints.API_UPDATE_WO_TO_OPEN_STATE)
    fun updateWorkOrderToOpenState(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<UpdateWOStatusResponse>

    @PUT(EndPoints.API_UPDATE_WO_TO_IN_PROGRESS_STATE)
    fun updateWorkOrderToInProgressState(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<UpdateWOStatusResponse>

    @PUT(EndPoints.API_UPDATE_WO_TO_IN_PAUSE_STATE)
    fun updateWorkOrderToPauseState(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<UpdateWOStatusResponse>

    @Multipart
    @POST(EndPoints.API_UPDATE_WO_TO_IN_DONE_STATE)
    fun updateWorkOrderDone(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Part("work_order[modified_by]") modified_by: String,
        @Part("work_order[status]") status: String,
        @Part("work_order[problem]") problem: String?,
        @Part("work_order[solution]") solution: String?,
        @Part("work_order[comment]") comment: String?,
        @Part("work_order[end_time]") end_time: String,
        @Part("work_order[hours_spent]") hours_spent: String,
        @PartMap mapSPHavingWork_order_spare_part_id: Map<String, String>,
        @PartMap mapSPHavingOnlySpare_part_id: Map<String, String>,
        @PartMap mapChecklist: Map<String, String>,
        @PartMap mapContributor: Map<String, String>,
        @PartMap mapCost: Map<String, String>,
        @Part file: MultipartBody.Part,
        @Part("work_order[docs_attributes][1538183123520][signature]") signatureBool: Boolean?,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<Workorder>

    @GET(EndPoints.API_GET_ALL_TEMPLATES)
    fun getAllTemplates(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<List<ChklistTemplateResponse>>

    @Multipart
    @PATCH(EndPoints.API_CHECKLIST_SECTION_ASSET_VERIFICATION)
    fun submitChecklistSectionAssetVerification(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Part("item_id") item_id: RequestBody,
        @Part("asset_id") asset_id: RequestBody,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<ChklistSection>

    @Multipart
    @PATCH(EndPoints.API_CHECKLIST_ITEM_ASSET_VERIFICATION)
    fun submitChecklistItemAssetVerification(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Part("item_id") item_id: RequestBody,
        @Part("asset_id") asset_id: RequestBody,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<ChklistItem>

    @Multipart
    @PATCH(EndPoints.API_CHECKLIST_SUBMIT)
    fun submitChklistMultipart(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") chklistId: String,
        @PartMap mapChecklist: Map<String, String>,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<Workorder>

    @Multipart
    @POST(EndPoints.API_UPLOAD_DOC)
    fun uploadDoc(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Part("entity_id") entity_id: RequestBody,
        @Part file: MultipartBody.Part,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<JsonObject>

    @Multipart
    @POST(EndPoints.API_CREATE_WO)
    fun createWorkOrderMultipart(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Part("work_order[name]") name: RequestBody?,
        @Part("work_order[asset_id]") asset_id: RequestBody?,
        @Part("work_order[modified_by]") modified_by: RequestBody?,
        @Part("work_order[status]") status: RequestBody?,
        @Part("work_order[company_id]") company_id: RequestBody?,
        @Part("work_order[hashed]") hashed: RequestBody?,
        @Part("work_order[assigned_to]") assigned_to: RequestBody?,
        @Part("work_order[team_ids][]") team_ids: List<String>?,
        @Part("work_order[due_date]") due_date: RequestBody?,
        @Part("work_order[priority]") priority: RequestBody?,
        @Part("work_order[eta]") eta: RequestBody?,
        @Part("work_order[category]") category: RequestBody?,
        @Part("work_order[description]") description: RequestBody?,
        @Part("work_order[chklist_id]") chklistTemplateId: RequestBody?,
        @Part("work_order[tag_ids][]") tag_ids: List<String>?,
        @PartMap mapSpareParts: Map<String, String>?,
        @PartMap mapCustomFields: Map<String, String>?,
        @Part files: Array<MultipartBody.Part?>,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<Workorder>

    @Multipart
    @POST(EndPoints.API_CREATE_REQUEST)
    fun createManualRequestMultipart(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Part("work_order[name]") name: RequestBody,
        @Part("work_order[asset_id]") asset_id: RequestBody,
        @Part("work_order[modified_by]") modified_by: RequestBody,
        @Part("work_order[requested_by]") requested_by: RequestBody,
        @Part("work_order[status]") status: RequestBody,
        @Part("work_order[hashed]") hashed: RequestBody,
        @Part("work_order[description]") description: RequestBody?,
        @Part("work_order[tag_ids][]") tag_ids: List<String>,
        @Part files: Array<MultipartBody.Part?>,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<CreateRequestResponse>

    @Multipart
    @POST(EndPoints.API_CREATE_REQUEST)
    fun createTemplateRequestMultipart(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Part("work_order[asset_id]") asset_id: RequestBody,
        @Part("work_order[request_template_id]") request_template_id: RequestBody,
        @Part("work_order[modified_by]") modified_by: RequestBody,
        @Part("work_order[requested_by]") requested_by: RequestBody,
        @Part("work_order[status]") status: RequestBody,
        @Part("work_order[hashed]") hashed: RequestBody,
        @Part("work_order[description]") description: RequestBody?,
        @Part("work_order[due_date]") due_date: RequestBody,
        @Part files: Array<MultipartBody.Part?>,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<CreateRequestResponse>

    @GET(EndPoints.API_GET_ALL_REQUESTS)
    fun getAllRequests(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @QueryMap filters: Map<String, String>,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<List<RequestsResponse>>

    @GET(EndPoints.API_GET_ALL_WOS)
    fun getAllWorkOrders(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Query("page") page: Int,
        @Query("w[assigned_to_eq]") myWorkOrders: String? = null,
        @Query("v[due_date_gteq]") dueDateGreaterThanEqualToToday: String? = null,
        @Query("v[due_date_lteq]") dueDateLessThanEqualToToday: String? = null,
        @Query("n[due_date_lteq]") dueDateLessThanEqualToOverDue: String? = null,
        @Query("t[_a_type_in][]") type: List<String>,
        @Query("t[_status_in][]") status: List<String>,
        @Query("x[_priority_in][]") priority: List<String>,
        @Query("p[assigned_to_in][]") employees: List<String>,
        @Query("o[team_ids_in][]") teams: List<String>,
        @Query("r[asset_id_in][]") assets: List<String>,
        @Query("include_children_assets") selectedIncludeChildrenAssets: String? = null,
        @Query("include_chklist_assets") selectedIncludeChklistAssets: String? = null,
        @Query("y[_category_in][]") category: List<String>,
        @Query("z[tag_ids_in][]") tags: List<String>,
        @Query("u[due_date_gteq]") dueDateGreaterThanEqualToDatePicker: String? = null,
        @Query("u[due_date_lteq]") dueDateLessThanEqualToDatePicker: String? = null,
        @Query("q[s]") sortby: String? = null,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<List<Workorder>>


    @GET(EndPoints.API_GET_ALL_ASSETS)
    fun getAllAssets(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Query("verbosity") verbosity: String,
        @Query("level") level: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<List<Asset>>

    @GET(EndPoints.API_GET_ALL_ASSETS)
    fun getSortByAssets(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Query("level") level: String?,
        @Query("verbosity") verbosity: String,
        @Query("sort") sort: String?,
        @Query("direction") direction: String?,
        @Query("parent") parentId: String?,
        @Query("filters[responsible_emp][]") employees: ArrayList<String>?,
        @Query("filters[team_ids][]") teams: ArrayList<String>?,
        @Query("filters[creation_daterange]") dateRange: String?,
        @Query("filters[assets_with_schedules]") assetsSchedule: Boolean?,
        @Query("filters[custom_fields][Dropdown 1][]") customField: ArrayList<String>?,
        @Query("filters[custom_fields][Test 2 updated][]") customField2: ArrayList<String>?,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<List<Asset>>

    @GET(EndPoints.API_GET_LEAD_ASSETS)
    fun getAssetLevels(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<List<AssetLevel>>

    @GET(EndPoints.API_GET_ALL_TAGS)
    fun getAllTags(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<TagsResponse>

    @GET(EndPoints.API_GET_ALL_SPARE_PARTS)
    fun getAllSpareParts(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<SparePartsResponse>

    @GET(EndPoints.API_GET_ALL_TEAMS)
    fun getAllTeams(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<List<Team>>

    @GET(EndPoints.API_GET_ARTICLE)
    fun getArticle(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<Article>

    @GET(EndPoints.GET_WO_RECOMMENDATIONS)
    fun getWorkOrderRecommendations(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") workOrderId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform
    ): Call<WorkOrderRecommendation>

    @POST(EndPoints.API_WO_RECOMMENDATION_FEEDBACK)
    fun postRecommendationFeedback(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Body recommendationFeedbackRequest: RecommendationFeedbackRequest,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<RecommendationFeedbackResponse>

    @PUT(EndPoints.API_UPDATE_ASSET)
    fun updateAsset(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") assetId: String,
        @Body rootAssetUpdateRequest: RootAssetUpdateRequest,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<Asset>

    @POST(EndPoints.API_GET_SEARCH_LIST)
    fun getSearchList(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Body searchRequestModel: SearchRequestModel,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<Search>

    @GET(EndPoints.API_GET_SINGLE_WORKORDER)
    fun getSingleWorkOrder(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<Workorder>

    @GET(EndPoints.API_GET_SINGLE_ASSET)
    fun getSingleAsset(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<Asset>

    @POST(EndPoints.API_LOGIN)
    fun loginRequest(
        @Body userLoginRequest: UserLoginRequest,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<Login>

    @GET(EndPoints.API_GET_COMPANY_DETAILS)
    fun getCompanyDetails(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("company_id") companyId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<CompanyDetails>

    @GET(EndPoints.API_GET_USER_PERMISSIONS)
    fun getUserPermissions(
        @Header(Key.Email) userEmail: String,
        @Header(Key.Authorization) accessToken: String,
        @Path("id") userId: String,
        @Header(Key.Accept) accept: String = HeaderValues.accept,
        @Header(Key.ContentType) contentType: String = HeaderValues.contentType,
        @Header(Key.Build) build: Int = HeaderValues.build,
        @Header(Key.Platform) requestSource: String = HeaderValues.platform,
        @Header(Key.Device) requestDevice: String = HeaderValues.device
    ): Call<PermissionResponse>
}