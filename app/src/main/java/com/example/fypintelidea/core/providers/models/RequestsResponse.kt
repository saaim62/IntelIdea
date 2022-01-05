package com.connectavo.app.connectavo_android.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import java.io.Serializable

data class RequestsResponse(
    var name: String? = null,
    var status: String? = null,
    var created_at: String? = null,
    var asset_id: String? = null,
    var decline_reason: String? = null,
    var requested_by: RequestedBy? = null,
) : ConnectavoBaseApiResponse(), Serializable

data class RequestedBy(
    val name: String? = null,
    val phone: String? = null,
)

enum class RequestTypes(val rawValue: String) {
    REQUEST_STATUS_REQUESTED("requested"),
    REQUEST_STATUS_OPEN("pending"),
    REQUEST_STATUS_CANCELED("cancelled"),
    REQUEST_STATUS_IN_PROGRESS("doing"),
    REQUEST_STATUS_DONE("done"),
    REQUEST_STATUS_PAUSED("paused");

    companion object {
        operator fun invoke(rawValue: String) = values().firstOrNull { it.rawValue == rawValue }
    }
}