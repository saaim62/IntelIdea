package com.example.fypintelidea.core.providers.models

import com.example.fypintelidea.core.providers.models.CustomField


class NewWorkOrderRequest(
    var name: String? = null,
    var status: String? = null,
    var due_date: String? = null,
    var priority: String? = null,
    var category: String? = null,
    var eta: String? = null,
    var description: String? = null,
    var asset_id: String? = null,
    var modified_by: String? = null,
    var company_id: String? = null,
    var hashed: String? = null,
    var tag_ids: List<String>? = null,
    var teamIds: List<String>? = null,
    var custom_fields: List<CustomField>? = null
)