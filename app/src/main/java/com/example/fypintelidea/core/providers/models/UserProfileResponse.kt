package com.example.fypintelidea.core.providers.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.SerializedName
import java.util.*

data class UserProfileResponse(
    var id: String? = null,
    var role: String? = null,
    var name: String? = null,
    var phone: String? = null,
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,
    @SerializedName("team_ids")
    var teamIds: List<String?>? = null
) : Parcelable, ConnectavoBaseApiResponse() {

    private constructor(`in`: Parcel) : this() {
        id = `in`.readString()
        role = `in`.readString()
        name = `in`.readString()
        phone = `in`.readString()
        avatarUrl = `in`.readString()
        teamIds = ArrayList()
        `in`.readList(teamIds as ArrayList<String?>, null)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(id)
        parcel.writeString(role)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(avatarUrl)
        parcel.writeList(teamIds)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as UserProfileResponse
        return id == user.id
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserProfileResponse> =
            object : Parcelable.Creator<UserProfileResponse> {
                override fun createFromParcel(`in`: Parcel): UserProfileResponse {
                    return UserProfileResponse(`in`)
                }

                override fun newArray(size: Int): Array<UserProfileResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}