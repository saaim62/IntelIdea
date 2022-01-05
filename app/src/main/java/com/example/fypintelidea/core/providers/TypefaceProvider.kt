package com.example.fypintelidea.core.providers

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.example.fypintelidea.R
import com.example.fypintelidea.core.Fonts

interface TypefaceProvider {

    fun getTypefaceFromXml(context: Context, attrs: AttributeSet?): Typeface? {

        var fontTypeface: Typeface? = null
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ConnectavoFontAttributes, 0, 0)
        val fontType = typedArray.getInteger(R.styleable.ConnectavoFontAttributes_fontType, 0)

        when (fontType) {
            0 -> fontTypeface =
                Typeface.createFromAsset(context.assets, Fonts.INTER_REGULAR)
            1 -> fontTypeface = Typeface.createFromAsset(context.assets, Fonts.INTER_BOLD)
            2 -> fontTypeface =
                Typeface.createFromAsset(context.assets, Fonts.INTER_SEMI_BOLD)
            3 -> fontTypeface = Typeface.createFromAsset(context.assets, Fonts.INTER_LIGHT)
        }

        typedArray.recycle()
        return fontTypeface
    }
}