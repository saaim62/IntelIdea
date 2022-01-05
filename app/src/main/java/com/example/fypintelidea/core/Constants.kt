package com.example.fypintelidea.core

import com.example.fypintelidea.BuildConfig

object Constants {
    const val EMPTY_STRING = ""
    const val SPACE_STRING = " "
    const val LOGGER_TAG = "Connectavo App"
}

object Fonts {
    const val INTER_REGULAR = "fonts/inter_regular.ttf"
    const val INTER_BOLD = "fonts/inter_bold.ttf"
    const val INTER_SEMI_BOLD = "fonts/inter_semi_bold.ttf"
    const val INTER_LIGHT = "fonts/inter_light.ttf"
}

object Key {
    const val Email = "X-User-Email"
    const val Authorization = "X-User-Token"
    const val Accept = "Accept"
    const val ContentType = "Content-Type"
    const val Build = "Build"
    const val Platform = "REQ-PLATFORM"
    const val Device = "Device"
}

object HeaderValues {
    const val accept: String = "application/json"
    const val contentType: String = "application/json"
    const val contentTypeFormUrlEncoded: String = "application/x-www-form-urlencoded"
    const val build: Int = BuildConfig.VERSION_CODE
    const val platform: String = "android"
    val device: String = android.os.Build.MODEL
}

object MaxDocumentAttachmentCounts {
    const val MAX_ATTACHMENT_COUNT_WO_REQUESTS = 3
    const val MAX_ATTACHMENT_COUNT_CHECKLIST = 3
}

object EndPoints {
    const val API_UPDATE_PROFILE = "/api/v2/users/{id}.json"
    const val API_GET_PROFILE = "/api/v2/users/{id}.json"
    const val API_GET_ALL_USERS = "/api/v2/users"
    const val API_UPDATE_WO_TO_OPEN_STATE = "/api/v2/work_orders/{id}/open.json"
    const val API_UPDATE_WO_TO_IN_PROGRESS_STATE = "/api/v2/work_orders/{id}/in_progress.json"
    const val API_UPDATE_WO_TO_IN_PAUSE_STATE = "/api/v2/work_orders/{id}/pause.json"
    const val API_UPDATE_WO_TO_IN_DONE_STATE = "/api/v2/work_orders/{id}/done.json"
    const val API_GET_ALL_TEMPLATES = "/api/v2/chklists.json"
    const val API_CHECKLIST_SECTION_ASSET_VERIFICATION = "/api/v2/chklists/asset_verification.json"
    const val API_CHECKLIST_ITEM_ASSET_VERIFICATION = "/api/v2/chklists/asset_verification.json"
    const val API_CHECKLIST_SUBMIT = "/api/v2/chklists/{id}/submit.json"
    const val API_UPLOAD_DOC = "/api/v2/docs.json"
    const val API_CREATE_WO = "/api/v2/work_orders.json"
    const val API_CREATE_REQUEST = "/api/v2/tenant_requests.json"
    const val API_GET_ALL_REQUESTS = "/api/v2/tenant_requests.json"
    const val API_GET_ALL_WOS = "/api/v2/work_orders.json"
    const val API_GET_ALL_ASSETS = "/api/v2/assets.json"
    const val API_GET_LEAD_ASSETS = "/api/v2/labels.json"
    const val API_GET_ALL_TAGS = "/api/v2/companies/{id}/tags.json"
    const val API_GET_ALL_SPARE_PARTS = "/api/v2/spare_parts.json"
    const val API_GET_ALL_TEAMS = "/api/v2/teams.json"
    const val API_GET_ARTICLE = "/api/v2/articles/{id}.json"
    const val GET_WO_RECOMMENDATIONS = "/api/v2/work_orders/{id}/recommendations.json"
    const val API_WO_RECOMMENDATION_FEEDBACK = "/api/v2/send_feedback.json"
    const val API_UPDATE_ASSET = "/api/v2/assets/{id}.json"
    const val API_GET_SEARCH_LIST = "/api/v2/search.json"
    const val API_GET_SINGLE_WORKORDER = "/api/v2/work_orders/{id}.json"
    const val API_GET_SINGLE_ASSET = "/api/v2/assets/{id}.json"
    const val API_LOGIN = "/api/v2/users/sign_in"
    const val API_GET_COMPANY_DETAILS = "/api/v2/companies/{company_id}.json"
    const val API_GET_USER_PERMISSIONS = "/api/v2/users/{id}.json"
}