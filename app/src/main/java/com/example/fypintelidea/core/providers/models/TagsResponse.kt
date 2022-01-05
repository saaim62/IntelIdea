package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.providers.models.Tag
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by ibtisam on 11/14/2016.
 */
@Parcelize
class TagsResponse(
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    @SerializedName("tags")
    @Expose
    var tags: List<Tag>? = null
) : ConnectavoBaseApiResponse(), Parcelable