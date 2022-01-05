package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import java.io.Serializable

data class Asset(
    var id: String? = null,
    var custom_scan_code: String? = null,
    var description: String? = null,
    var responsible_emp: String? = null,
    var name: String? = null,
    var label_id: String? = null,
    var parent_id: String? = null,
    var parent_ids: List<String>? = null,
    var request_templates: List<Request_templates>? = null,
    var has_children: Boolean = false,
    var pieces: Int? = 0,
    var machine_hours: Int? = 0,
    var created_at: String? = null,
    var updated_at: String? = null,
    var schedules: List<Schedule>? = null,
    var custom_fields: List<CustomField>? = null,
    var documents: ArrayList<Document>? = null,
    var team_ids: List<String>? = null
) : Serializable, ConnectavoBaseApiResponse() {
    constructor(name: String) : this() {
        this.name = name
    }
}