package com.example.fypintelidea.core.providers.models

import com.google.gson.annotations.SerializedName

class RootUser(
    @SerializedName("user")
    var user: User? = null
)