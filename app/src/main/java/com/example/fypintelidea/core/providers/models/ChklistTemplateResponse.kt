package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.providers.models.ChklistSection
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChklistTemplateResponse(
    @SerializedName("company_id")
    @Expose
    var company_id: String? = null,

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("chklist_sections")
    @Expose
    var chklistSections: List<ChklistSection>? = null,

    @SerializedName("chklist_type")
    @Expose
    var chklist_type: String? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,
) : ConnectavoBaseApiResponse(), Serializable