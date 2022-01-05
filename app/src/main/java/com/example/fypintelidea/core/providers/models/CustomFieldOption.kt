package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class CustomFieldOption(
    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("custom_field_option_id")
    @Expose
    var customFieldOptionId: String
) : ConnectavoBaseApiResponse(), Serializable, Parcelable