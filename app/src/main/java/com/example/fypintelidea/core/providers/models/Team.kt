package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Team(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("userIds")
    @Expose
    var userIds: List<String>? = null,

    @SerializedName("workOrderIds")
    @Expose
    var workOrderIds: List<String>? = null,

    @SerializedName("teamLeadId")
    @Expose
    var teamLeadId: String? = null,

    @SerializedName("createdAt")
    @Expose
    var createdAt: Int? = null,

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: Int? = null,

    @SerializedName("url")
    @Expose
    var url: String? = null,
) : ConnectavoBaseApiResponse(), Serializable, Parcelable