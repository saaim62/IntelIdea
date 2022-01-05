package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class ChklistItemOptions(
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("option")
    @Expose
    var option: String
) : Serializable, Parcelable