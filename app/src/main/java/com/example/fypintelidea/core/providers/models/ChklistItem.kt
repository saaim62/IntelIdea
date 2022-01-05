package com.example.fypintelidea.core.providers.models

import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChklistItem(
    var intID: Int,

    @SerializedName("item_type")
    @Expose
    var item_type: String? = null,

    @SerializedName("comment")
    @Expose
    var comment: String? = null,

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("asset_id")
    @Expose
    var assetId: String? = null,

    @SerializedName("require_qr_scan")
    @Expose
    var isRequireQrScan: Boolean = false,

    @SerializedName("is_verified")
    @Expose
    var isVerified: Boolean = false,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("value")
    @Expose
    var value: String? = null,

    @SerializedName("add_images")
    @Expose
    var isAdd_images: Boolean = false,

    @SerializedName("required")
    @Expose
    var isRequired: Boolean = false,

    @SerializedName("create_request_on_complete")
    @Expose
    var isCreate_request_on_complete: Boolean = false,

    @SerializedName("create_request")
    @Expose
    var isCreate_request: Boolean = false,

    @SerializedName("selected_option_ids")
    @Expose
    var selected_option_ids: ArrayList<String>? = null,

    @SerializedName("chklist_item_options")
    @Expose
    var chklist_item_options: List<ChklistItemOptions>? = null,

    @SerializedName("articles")
    @Expose
    var articles: List<Article>? = null,

    @SerializedName("item_pdfs")
    @Expose
    var item_pdfs: List<Document>,

    @SerializedName("item_images") //for image receiving (questions)
    @Expose
    var item_images: List<Image>? = null,

    @SerializedName("images") //for image receiving (answers)
    @Expose
    var images: List<Image>? = null,

    @SerializedName("docs_attributes") //for image sending
    @Expose
    var docs_attributes_base64: List<String>? = null,
    var photoPaths: List<String>? =
        null, // for local image paths storage can be simplified and removed later.

    @SerializedName("chklist_item_range")
    @Expose
    var chklist_item_range: ChklistItemRange? = null,


    ) : ConnectavoBaseApiResponse(), Serializable {
    companion object {
        const val ITEM_TYPE_CHECKBOX = "checkbox"
        const val ITEM_TYPE_TEXTFIELD = "textfield"
        const val ITEM_TYPE_NUMBERFIELD = "numberfield"
        const val ITEM_TYPE_SCORE = "score"
        const val ITEM_TYPE_SINGLE_SELECT = "single_select"
        const val ITEM_TYPE_MULTI_SELECT = "multi_select"
        const val ITEM_TYPE_RANGE = "range"
    }
}