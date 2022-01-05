package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Search(
    @SerializedName("work_orders")
    val workOrders: List<Workorder>? = null,
    val assets: List<Asset>? = null,
) : Serializable, ConnectavoBaseApiResponse()

data class SearchAsset(
    val name: String,
    val id: String
)