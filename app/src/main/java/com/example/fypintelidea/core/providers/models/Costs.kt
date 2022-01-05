package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Costs(
    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("value")
    @Expose
    var value: String? = null,

    @SerializedName("id")
    @Expose
    var id: String? = null
) : Serializable, Parcelable