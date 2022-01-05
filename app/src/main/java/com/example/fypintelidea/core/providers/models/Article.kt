package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Article(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("user_id")
    @Expose
    var user_id: String? = null,

    @SerializedName("user")
    var user: User? = null,

    @SerializedName("content")
    @Expose
    var content: String? = null,

    @SerializedName("url")
    @Expose
    var url: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("assets")
    @Expose
    var asset: List<Asset>? = null,

    @SerializedName("spare_parts")
    @Expose
    var spare_parts: List<SpareParts>? = null,

    @SerializedName("keywords")
    @Expose
    var keywords: List<Keyword>? = null,

    @SerializedName("documents")
    @Expose
    var documents: List<Document>? = null,
) : ConnectavoBaseApiResponse(), Serializable, Parcelable