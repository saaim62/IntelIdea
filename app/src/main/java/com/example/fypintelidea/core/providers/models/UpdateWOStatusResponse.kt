package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse


data class UpdateWOStatusResponse(
    val id: String? = null,
    var status: String? = null
) : ConnectavoBaseApiResponse()