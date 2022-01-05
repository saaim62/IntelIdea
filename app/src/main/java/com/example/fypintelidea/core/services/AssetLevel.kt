package com.example.fypintelidea.core.services

import java.io.Serializable

data class AssetLevel(
    var id: String? = null,
    var en_value: String? = null,
    var de_value: String? = null,
    var lead_level: Boolean? = false,
    var a_type: String? = null
) : Serializable