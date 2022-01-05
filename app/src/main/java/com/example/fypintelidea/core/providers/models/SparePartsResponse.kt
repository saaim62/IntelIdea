package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.providers.models.SpareParts
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by ibtisam on 11/14/2016.
 */
@Parcelize
class SparePartsResponse(
    @SerializedName("spare_parts")
    @Expose
    var spareParts: ArrayList<SpareParts>? = null
) : ConnectavoBaseApiResponse(), Parcelable