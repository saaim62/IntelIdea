package com.example.fypintelidea.core.providers.models

import com.google.gson.annotations.Expose
import java.io.Serializable

class Document(
    var id: String,
    var filename: String,
    var content_type: String,
    var url: String,
    var signature: Boolean
) : Serializable {
    @Expose
    var date: String? = null
}