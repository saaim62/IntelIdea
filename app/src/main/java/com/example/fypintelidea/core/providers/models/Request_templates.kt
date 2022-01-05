package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.providers.models.Assets
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Request_templates(
    var name: String? = null,
    var eta: String? = null,
    var due_within: String? = null,
    var due_within_unit: String? = null,
    var spare_parts: String? = null,
    var teams: String? = null,
    var documents: String? = null,
    var description: String? = null,
    var checklist: String? = null,
    var id: String? = null,
    var employee: String? = null,
    var priority: String? = null,
    var category: String? = null,
    var tags: String? = null,
    var assets: List<Assets>? = null,

    ) : ConnectavoBaseApiResponse(), Serializable, Parcelable {
    companion object {
        const val DUE_WITHIN_DAYS = "days"
        const val DUE_WITHIN_HOURS = "hours"
    }
}