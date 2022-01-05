package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Tag(
    @Expose
    var id: String? = null,
    @Expose
    var name: String? = null,
) : Serializable, Parcelable