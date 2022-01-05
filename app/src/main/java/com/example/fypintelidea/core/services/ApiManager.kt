package com.example.fypintelidea.core.services

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.core.MyURLs
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.providers.models.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class ApiManager(private val context: Context) {

    private fun getHttpClient(context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(logging)
            .addInterceptor(ChuckerInterceptor(context))
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.MINUTES).build()
    }

    fun initialize(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(MyURLs.getBaseUrl(context))
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient(context))
            .build()
            .create(ApiInterface::class.java)
    }

    fun initializeExcludedFields(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(MyURLs.getBaseUrl(context))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient(context))
            .build()
            .create(ApiInterface::class.java)
    }

    fun userLogin(apiCallbacks: ApiCallbacks, userLogin: UserLoginRequest) {
        val apiCall = initialize().loginRequest(
            userLoginRequest = UserLoginRequest(userLogin.user)
        )
        ApiExecutor<Login>().addCallToQueue(context, apiCall, apiCallbacks)
    }

    fun fetchAssetsFromServer(
        apiCallbacks: ApiCallbacks,
        verbosity: String
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initialize().getAllAssets(
                    it,
                    it1,
                    verbosity,
                    "All"
                )
            }
        }
        apiCall?.let {
            ApiExecutor<List<Asset>>().addCallToQueue(context, it, apiCallbacks)
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
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getSortByAssets(
                        userEmail = it,
                        accessToken = it1,
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
                        customField2 = customField2,
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<List<Asset>>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun fetchAssetLevelsFromServer(apiCallbacks: ApiCallbacks) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initialize().getAssetLevels(
                    it,
                    it1
                )
            }
        }
        apiCall?.let {
            ApiExecutor<List<AssetLevel>>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun fetchUserProfile(
        apiCallbacks: ApiCallbacks,
        user: String
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initialize().getProfile(
                    it,
                    it1,
                    user
                )
            }
        }
        apiCall?.let {
            ApiExecutor<UserProfileResponse>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun fetchAllUsers(
        apiCallbacks: ApiCallbacks
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initialize().getAllUsers(
                    it,
                    it1
                )
            }
        }
        apiCall?.let {
            ApiExecutor<List<User>>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getAllTemplates(
        apiCallbacks: ApiCallbacks
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initialize().getAllTemplates(
                    it,
                    it1
                )
            }
        }
        apiCall?.let {
            ApiExecutor<List<ChklistTemplateResponse>>().addCallToQueue(
                context,
                it,
                apiCallbacks
            )
        }
    }

    fun updateUserProfile(
        apiCallbacks: ApiCallbacks,
        rootUser: RootUser
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                SessionManager(context).getString(SessionManager.KEY_LOGIN_ID)?.let { it2 ->
                    initialize().updateProfile(
                        it,
                        it1,
                        it2,
                        rootUser
                    )
                }
            }
        }
        apiCall?.let {
            ApiExecutor<User>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun uploadDoc(
        apiCallbacks: ApiCallbacks,
        entity_id: RequestBody,
        file: MultipartBody.Part
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initialize().uploadDoc(
                    it,
                    it1,
                    entity_id,
                    file
                )
            }
        }
        apiCall?.let {
            ApiExecutor<JsonObject>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun updateWorkOrderToOpenState(
        apiCallbacks: ApiCallbacks,
        workorder: Workorder
    ) {
        val apiCall = workorder.id?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let { it1 ->
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it2 ->
                    initialize().updateWorkOrderToOpenState(
                        it1,
                        it2,
                        it
                    )
                }
            }
        }
        apiCall?.let {
            ApiExecutor<UpdateWOStatusResponse>().addCallToQueue(
                context,
                it,
                apiCallbacks
            )
        }
    }

    fun updateWorkOrderToInProgress(
        apiCallbacks: ApiCallbacks,
        workorder: Workorder
    ) {
        val apiCall = workorder.id?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let { it1 ->
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it2 ->
                    initialize().updateWorkOrderToInProgressState(
                        it1,
                        it2,
                        it
                    )
                }
            }
        }
        apiCall?.let {
            ApiExecutor<UpdateWOStatusResponse>().addCallToQueue(
                context,
                it,
                apiCallbacks
            )
        }
    }

    fun updateWorkOrderToPauseState(
        apiCallbacks: ApiCallbacks,
        workorder: Workorder
    ) {
        val apiCall = workorder.id?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let { it1 ->
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it2 ->
                    initialize().updateWorkOrderToPauseState(
                        it1,
                        it2,
                        it
                    )
                }
            }
        }
        apiCall?.let {
            ApiExecutor<UpdateWOStatusResponse>().addCallToQueue(
                context,
                it,
                apiCallbacks
            )
        }
    }

    //    ExcludeField
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
        val apiCall = workorder.id?.let {
            workorder.status?.let { it1 ->
                workorder.end_time?.let { it2 ->
                    workorder.hours_spent?.let { it3 ->
                        SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)
                            ?.let { it4 ->
                                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)
                                    ?.let { it5 ->
                                        SessionManager(context).getString(SessionManager.KEY_LOGIN_ID)
                                            ?.let { it6 ->
                                                initializeExcludedFields()?.updateWorkOrderDone(
                                                    it4,
                                                    it5,
                                                    it,
                                                    it6,
                                                    it1,
                                                    workorder.problem,
                                                    workorder.solution,
                                                    workorder.comment,
                                                    it2,
                                                    it3,
                                                    mapSparePartHavingWorkOrderSparePartId,
                                                    mapSparePartHavingOnlySparePartId,
                                                    mapChecklist,
                                                    mapContributor,
                                                    mapCost,
                                                    filePart,
                                                    true
                                                )
                                            }
                                    }
                            }
                    }
                }
            }
        }
        apiCall?.let {
            ApiExecutor<Workorder>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    //    ExcludeField
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
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initializeExcludedFields().createWorkOrderMultipart(
                    it,
                    it1,
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
        apiCall?.let {
            ApiExecutor<Workorder>().addCallToQueue(context, it, apiCallbacks)
        }
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
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initializeExcludedFields().createManualRequestMultipart(
                    it,
                    it1,
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
        }
        apiCall?.let {
            ApiExecutor<CreateRequestResponse>().addCallToQueue(
                context,
                it,
                apiCallbacks
            )
        }
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
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initialize().createTemplateRequestMultipart(
                    it,
                    it1,
                    asset_id,
                    request_template_id,
                    modified_by,
                    requested_by,
                    status,
                    hashed,
                    description,
                    due_date,
                    files
                )
            }
        }
        apiCall?.let {
            ApiExecutor<CreateRequestResponse>().addCallToQueue(
                context,
                it,
                apiCallbacks
            )
        }
    }

    fun submitChecklistSectionAssetVerification(
        apiCallbacks: ApiCallbacks,
        item_id: RequestBody,
        asset_id: RequestBody
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().submitChecklistSectionAssetVerification(
                        it,
                        it1,
                        item_id,
                        asset_id,
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<ChklistSection>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun submitChecklistItemAssetVerification(
        apiCallbacks: ApiCallbacks,
        item_id: RequestBody,
        asset_id: RequestBody
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().submitChecklistItemAssetVerification(
                        it,
                        it1,
                        item_id,
                        asset_id,
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<ChklistItem>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    //    ExcludeField
    fun submitChklistMultipart(
        apiCallbacks: ApiCallbacks,
        chklistId: String,
        mapChecklist: Map<String, String>
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initializeExcludedFields().submitChklistMultipart(
                    it,
                    it1,
                    chklistId,
                    mapChecklist,
                )
            }
        }
        apiCall?.let {
            ApiExecutor<Workorder>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getAllRequests(
        apiCallbacks: ApiCallbacks,
        filters: Map<String, String>
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getAllRequests(
                        it,
                        it1,
                        filters
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<List<RequestsResponse>>().addCallToQueue(
                context,
                it,
                apiCallbacks
            )
        }
    }

    //    ExcludeField
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
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                initializeExcludedFields().getAllWorkOrders(
                    it,
                    it1,
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
                    sortby
                )
            }
        }
        apiCall?.let {
            ApiExecutor<List<Workorder>>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getAllTags(
        apiCallbacks: ApiCallbacks,
        userId: String
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getAllTags(
                        it,
                        it1,
                        userId
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<TagsResponse>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getAllSpareParts(
        apiCallbacks: ApiCallbacks
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getAllSpareParts(
                        it,
                        it1
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<SparePartsResponse>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getAllTeams(
        apiCallbacks: ApiCallbacks
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getAllTeams(
                        it,
                        it1
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<List<Team>>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getArticle(
        apiCallbacks: ApiCallbacks,
        userId: String
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getArticle(
                        it,
                        it1,
                        userId
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<Article>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getWorkOrderRecommendations(
        apiCallbacks: ApiCallbacks,
        workOrderId: String
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getWorkOrderRecommendations(
                        it,
                        it1,
                        workOrderId
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<WorkOrderRecommendation>().addCallToQueue(
                context = context,
                apiCall = apiCall,
                apiCallbacks = apiCallbacks,
            )
        }
    }

    fun postRecommendationFeedback(
        apiCallbacks: ApiCallbacks,
        recommendationFeedbackRequest: RecommendationFeedbackRequest
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().postRecommendationFeedback(
                        it,
                        it1,
                        recommendationFeedbackRequest
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<RecommendationFeedbackResponse>().addCallToQueue(
                context,
                it,
                apiCallbacks
            )
        }
    }

    fun updateAsset(
        apiCallbacks: ApiCallbacks,
        assetId: String,
        rootAssetUpdateRequest: RootAssetUpdateRequest
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().updateAsset(
                        it,
                        it1,
                        assetId,
                        rootAssetUpdateRequest
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<Asset>().addCallToQueue(
                context,
                it,
                apiCallbacks
            )
        }
    }

    fun getSingleAsset(
        apiCallbacks: ApiCallbacks,
        userId: String
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getSingleAsset(
                        it,
                        it1,
                        userId
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<Asset>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getSingleWorkOrder(
        apiCallbacks: ApiCallbacks,
        userId: String
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getSingleWorkOrder(
                        it,
                        it1,
                        userId
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<Workorder>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getSearchList(
        apiCallbacks: ApiCallbacks,
        search: String
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getSearchList(
                        it,
                        it1,
                        searchRequestModel = SearchRequestModel(search)
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<Search>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getCompanyDetails(
        apiCallbacks: ApiCallbacks,
        companyId: String
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getCompanyDetails(
                        it,
                        it1,
                        companyId
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<CompanyDetails>().addCallToQueue(context, it, apiCallbacks)
        }
    }

    fun getUserPermissions(
        apiCallbacks: ApiCallbacks,
        userId: String
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    initialize().getUserPermissions(
                        it,
                        it1,
                        userId
                    )
                }
            }
        apiCall?.let {
            ApiExecutor<PermissionResponse>().addCallToQueue(context, it, apiCallbacks)
        }
    }
}