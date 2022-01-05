package com.example.fypintelidea.core.providers.models

import com.example.fypintelidea.core.providers.models.AssetUpdateRequest
import java.io.Serializable


data class RootAssetUpdateRequest(
    val asset: AssetUpdateRequest
) : Serializable