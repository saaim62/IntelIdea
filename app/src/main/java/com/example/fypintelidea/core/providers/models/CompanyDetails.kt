package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.providers.models.RequestsResponse
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CompanyDetails(
    val id: String,
    val name: String?,
    val subdomain: String?,
    @SerializedName("c_type")
    val cType: String,
    val url: String?,
    @SerializedName("work_order_custom_fields")
    val workOrderCustomFields: List<CustomField>?,
    @SerializedName("asset_custom_fields")
    val assetCustomFields: List<CustomField>?,
    @SerializedName("spare_part_custom_fields")
    val sparePartCustomFields: List<CustomField>?,
    @SerializedName("request_templates")
    val requestTemplates: List<RequestsResponse>?,
) : Serializable, ConnectavoBaseApiResponse()