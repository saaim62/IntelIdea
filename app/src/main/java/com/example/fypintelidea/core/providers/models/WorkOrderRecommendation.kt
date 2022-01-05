package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WorkOrderRecommendation : ConnectavoBaseApiResponse(), Serializable {

    @SerializedName("rec_wos")
    var recommendationWorkOrder: List<Recommendation> = listOf()

    @SerializedName("rec_docs")
    var recommendationDocuments: List<Recommendation> = listOf()

    inner class Recommendation : Serializable {
        var id: String? = null
        var name: String? = null
        var problem: String? = null
        var solution: String? = null
        var comment: String? = null

        @SerializedName("asset_id")
        var assetId: String? = null

        @SerializedName("asset_name")
        var assetName: String? = null
        var score: Double = 0.0
        var feedback: Int = 0
        var type: String? = null

        @SerializedName("document_store_id")
        var documentStoreId: String? = null

        @SerializedName("query_id")
        var queryId: String? = null
        var tags: List<String>? = null
        var excerpt: String? = null
        var page_no: String? = null
        var url: String? = null
        var tempSelectedFeedback: Int = 0
    }
}