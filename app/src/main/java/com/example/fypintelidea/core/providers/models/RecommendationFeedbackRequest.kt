package com.example.fypintelidea.core.providers.models

import com.google.gson.annotations.SerializedName

data class RecommendationFeedbackRequest(
    @SerializedName("parent_doc_id")
    var parentDocID: String? = null,

    @SerializedName("searched_text")
    var searchedText: String? = "",

    @SerializedName("recommended_doc_id")
    var recommendedDocID: String? = null,

    @SerializedName("recommended_doc_type")
    var recommendedDocType: String? = null,

    var score: String? = null,

    @SerializedName("current_feedback")
    var currentFeedback: String? = null,

    @SerializedName("previous_feedback")
    var previousFeedback: String = "0",

    @SerializedName("query_id")
    var queryID: String? = "",

    @SerializedName("feedback_source")
    var feedbackSource: String? = "combined_rec_engine"
)