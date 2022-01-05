package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Image(
    @SerializedName("filename")
    @Expose
    var filename: String,

    @SerializedName("content_type")
    @Expose
    var content_type: String,

    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("filesize")
    @Expose
    var filesize: String,

    @SerializedName("url")
    @Expose
    var url: String,
) : Serializable, Parcelable