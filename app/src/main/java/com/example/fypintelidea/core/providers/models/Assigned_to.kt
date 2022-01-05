package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Assigned_to(
    @Expose
    @SerializedName("company_id")
    var company_id: String? = null,

    @Expose
    @SerializedName("phone")
    var phone: String? = null,

    @Expose
    @SerializedName("team_ids")
    var team_ids: Array<String>,

    @Expose
    @SerializedName("avatar_updated_at")
    var avatar_updated_at: String? = null,

    @Expose
    @SerializedName("authentication_token")
    var authentication_token: String? = null,

    @Expose
    @SerializedName("updated_at")
    var updated_at: String? = null,

    @Expose
    @SerializedName("_id")
    var id: String? = null,

    @Expose
    @SerializedName("avatar_content_type")
    var avatar_content_type: String? = null,

    @Expose
    @SerializedName("avatar_file_name")
    var avatar_file_name: String? = null,

    @Expose
    @SerializedName("email")
    var email: String? = null,

    @Expose
    @SerializedName("avatar_file_size")
    var avatar_file_size: String? = null,

    @Expose
    @SerializedName("avatar_fingerprint")
    var avatar_fingerprint: String? = null,

    @Expose
    @SerializedName("name")
    var name: String? = null,

    @Expose
    @SerializedName("subdomain")
    var subdomain: String? = null,

    @Expose
    @SerializedName("role")
    var role: String? = null,

    @Expose
    @SerializedName("created_at")
    var created_at: String? = null,

    @Expose
    @SerializedName("_role")
    private var _role: String? = null,

    ) : Serializable, Parcelable