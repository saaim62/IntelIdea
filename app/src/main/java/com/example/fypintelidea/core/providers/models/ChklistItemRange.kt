package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class ChklistItemRange(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("min")
    @Expose
    var min: Int = 0,

    @SerializedName("max")
    @Expose
    var max: Int = 0
) : Serializable, Parcelable