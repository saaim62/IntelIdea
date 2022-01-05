package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class CustomField(
    @SerializedName("custom_field_id")
    @Expose
    var customFieldId: String? = null,

    @SerializedName("name")
    @Expose(serialize = false)
    var name: String? = null,

    @SerializedName("field_value")
    @Expose
    var fieldValue: String? = null,

    @SerializedName("field_type")
    @Expose(serialize = false)
    var fieldType: String? = null,

    @SerializedName("type")
    @Expose(serialize = false)
    var type: String? = null,

    @SerializedName("required")
    @Expose(serialize = false)
    var required: String? = null,

    @SerializedName("custom_field_option_id")
    @Expose(serialize = false)
    var customFieldOptionId: String? = null,

    @SerializedName("custom_field_options")
    @Expose
    var customFieldOptions: List<CustomFieldOption>? = null,
) : ConnectavoBaseApiResponse(), Serializable, Parcelable