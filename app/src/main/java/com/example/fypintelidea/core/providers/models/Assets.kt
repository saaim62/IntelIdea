package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Assets(
    var asset_id: String? = null,
    var include_children_assets: String? = null
) : ConnectavoBaseApiResponse(), Serializable, Parcelable