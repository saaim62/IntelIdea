package com.example.fypintelidea.core.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.TypefaceProvider


class ConnectavoButton : androidx.appcompat.widget.AppCompatButton, TypefaceProvider {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs,
        R.attr.borderlessButtonStyle
    ) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {
        this.typeface = getTypefaceFromXml(context, attrs)
    }
}