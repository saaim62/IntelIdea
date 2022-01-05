package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class User(

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("phone")
    @Expose
    var phone: String? = null,

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("role")
    @Expose
    var role: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String? = null,

    @SerializedName("avatar") //for image sending
    @Expose
    var avatarBase64: String? = null,

    @SerializedName("team_ids")
    @Expose
    var teamIds: List<String>? = null,

    @SerializedName("display_name")
    @Expose
    var display_name: String? = null,

    @SerializedName("hours_spent")
    @Expose
    var hours_spent: String? = null,
) : ConnectavoBaseApiResponse(), Parcelable {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val user = o as User
        return id == user.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}