package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Login(
    val success: Boolean?,
    val auth_token: String?,
    val email: String?,
    val user_id: String?,
    val company_id: String?,
    val role: String?,
    val permissions: List<LoginPermission>?,
    val device_token_required: Boolean?
) : Serializable, ConnectavoBaseApiResponse()

data class LoginPermission(
    @SerializedName("_id")
    val permissionId: String?,
    val name: String?
) : Serializable, ConnectavoBaseApiResponse()
