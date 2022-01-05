package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.SerializedName

data class PermissionResponse(
    val id: String,
    val email: String,

    @SerializedName("sign_in_count")
    val signInCount: Long,

    @SerializedName("company_id")
    val companyID: String,

    @SerializedName("updated_at")
    val updatedAt: Long,

    @SerializedName("created_at")
    val createdAt: Long,

    val name: String,
    val phone: String,

    @SerializedName("avatar_file_name")
    val avatarFileName: String,

    @SerializedName("avatar_content_type")
    val avatarContentType: String,

    @SerializedName("avatar_file_size")
    val avatarFileSize: Long,

    @SerializedName("avatar_fingerprint")
    val avatarFingerprint: String,

    @SerializedName("avatar_updated_at")
    val avatarUpdatedAt: String,

    @SerializedName("authentication_token")
    val authenticationToken: String,

    @SerializedName("team_ids")
    val teamIDS: List<String>,

    val role: String,
    val permissions: List<PermissionModel>,

    @SerializedName("avatar_url")
    val avatarURL: String
) : ConnectavoBaseApiResponse()

data class PermissionModel(
    val id: String,
    val name: String
) : ConnectavoBaseApiResponse()