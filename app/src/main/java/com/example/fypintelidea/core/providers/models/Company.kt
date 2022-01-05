package com.example.fypintelidea.core.providers.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Company(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("subdomain")
    @Expose
    var subdomain: String? = null,

    @SerializedName("c_type")
    @Expose
    var cType: String? = null,
) {
    companion object {
        const val COMPANY_TYPE_INDUSTRY = "industry"
        const val COMPANY_TYPE_PROPERTY = "property"
    }
}