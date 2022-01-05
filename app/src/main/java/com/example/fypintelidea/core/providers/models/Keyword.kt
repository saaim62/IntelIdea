package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Keyword(
    var name: String? = null,
    var id: String? = null
) : Serializable, Parcelable