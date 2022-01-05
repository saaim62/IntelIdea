package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class SpareParts(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("uid")
    @Expose
    var uid: String? = null,

    @SerializedName("unit")
    @Expose
    var unit: String? = null,

    @SerializedName("location")
    @Expose
    var location: String? = null,


    @SerializedName("spare_part_id")
    @Expose
    var spare_part_id: String? = null,

    @SerializedName("quantity")
    @Expose
    var quantity: String? = null,

    @SerializedName("current_quantity")
    @Expose
    var current_quantity: String? = null,

    @SerializedName("work_order_spare_part_id")
    @Expose
    var work_order_spare_part_id: String? = null,
) : ConnectavoBaseApiResponse(), Serializable, Parcelable {
    companion object {
        const val UNIT_PIECES = "pieces"
        const val UNIT_LITER = "liter"
    }
}