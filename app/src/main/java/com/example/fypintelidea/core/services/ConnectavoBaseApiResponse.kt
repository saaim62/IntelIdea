package com.connectavo.app.connectavo_android.core.services

import java.io.Serializable

open class ConnectavoBaseApiResponse(
    var collection: List<Any> = listOf(),
) : Serializable
