package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.providers.models.ChklistItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ChklistSection(
    var intID: Int,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("asset_id")
    @Expose
    var assetId: String? = null,

    @SerializedName("require_qr_scan")
    @Expose
    var isRequireScanQr: Boolean = false,

    @SerializedName("is_verified")
    @Expose
    var isVerified: Boolean = false,

    @SerializedName("items")
    @Expose(serialize = false)
    var chklistItem: List<ChklistItem>? = null,

    ) : ConnectavoBaseApiResponse(), Serializable {
    fun setIsVerified(isVerified: Boolean) {
        this.isVerified = isVerified
    }
}