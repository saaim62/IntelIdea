package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Checklist(
    @Expose
    var id: String,
    @Expose(serialize = false)
    var text: String,
) : Serializable, Parcelable {
    @Expose
    var done = false
}