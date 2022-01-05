package com.example.fypintelidea.core.services

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.fypintelidea.core.MyURLs
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.providers.models.*
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    var connectavoServices: ApiInterface? = null
    var connectavoServicesWithExcludedFields: ApiInterface? = null

    // Used only for now for JSONObject type response objects
    fun getClient(context: Context): Retrofit? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(MyURLs.getBaseUrl(context))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun getHttpClient(context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(logging)
            .addInterceptor(ChuckerInterceptor(context))
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.MINUTES).build()
    }

    fun initialize(context: Context) {
        connectavoServices = Retrofit.Builder()
            .baseUrl(MyURLs.getBaseUrl(context))
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient(context))
            .build()
            .create(ApiInterface::class.java)
    }

    fun initializeConnectavoServicesWithExcludedFields(context: Context) {
        connectavoServicesWithExcludedFields = Retrofit.Builder()
            .baseUrl(MyURLs.getBaseUrl(context))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient(context))
            .build()
            .create(ApiInterface::class.java)
    }

    fun fetchAllUsers(
        context: Context,
        apiCallbacks: ApiCallbacks
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                connectavoServices?.getAllUsers(
                    it,
                    it1
                )
            }
        }
        if (apiCall != null) {
            ApiExecutor<List<User>>().addCallToQueue(context, apiCall, apiCallbacks)
        }
    }

    fun getAllSpareParts(
        context: Context,
        apiCallbacks: ApiCallbacks
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                connectavoServices?.getAllSpareParts(
                    it,
                    it1
                )
            }
        }
        if (apiCall != null) {
            ApiExecutor<SparePartsResponse>().addCallToQueue(context, apiCall, apiCallbacks)
        }
    }

    fun submitChecklistSectionAssetVerification(
        context: Context,
        apiCallbacks: ApiCallbacks,
        item_id: RequestBody,
        asset_id: RequestBody
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    connectavoServices?.submitChecklistSectionAssetVerification(
                        it,
                        it1,
                        item_id,
                        asset_id,
                    )
                }
            }
        if (apiCall != null) {
            ApiExecutor<ChklistSection>().addCallToQueue(context, apiCall, apiCallbacks)
        }
    }

    fun submitChecklistItemAssetVerification(
        context: Context,
        apiCallbacks: ApiCallbacks,
        item_id: RequestBody,
        asset_id: RequestBody
    ) {
        val apiCall =
            SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
                SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                    connectavoServices?.submitChecklistItemAssetVerification(
                        it,
                        it1,
                        item_id,
                        asset_id,
                    )
                }
            }
        if (apiCall != null) {
            ApiExecutor<ChklistItem>().addCallToQueue(context, apiCall, apiCallbacks)
        }
    }

    fun submitChklistMultipart(
        context: Context,
        apiCallbacks: ApiCallbacks,
        chklistId: String,
        mapChecklist: Map<String, String>
    ) {
        val apiCall = SessionManager(context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                connectavoServicesWithExcludedFields?.submitChklistMultipart(
                    it,
                    it1,
                    chklistId,
                    mapChecklist,
                )
            }
        }
        if (apiCall != null) {
            ApiExecutor<Workorder>().addCallToQueue(context, apiCall, apiCallbacks)
        }
    }

}