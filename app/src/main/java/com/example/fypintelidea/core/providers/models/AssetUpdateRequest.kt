package com.example.fypintelidea.core.providers.models

import java.io.Serializable

data class AssetUpdateRequest(
    val machine_hours: Int?,
    val pieces: Int?,
) : Serializable
