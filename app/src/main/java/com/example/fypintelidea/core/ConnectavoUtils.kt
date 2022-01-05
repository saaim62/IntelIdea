package com.example.fypintelidea.core

import android.content.Context
import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import java.util.*

object ConnectavoUtils {

    fun convertDpToPx(context: Context, dip: Float): Float {
        val r: Resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.displayMetrics
        )
    }

    fun getPixel(dp: Float, displayMetrics: DisplayMetrics): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            displayMetrics
        ).toInt()
    }

    fun makeSpannableText(
        strings: ArrayList<String>,
        typeface: ArrayList<Int>, textSizes: ArrayList<Float>?
    ): SpannableStringBuilder {
        var string = ""
        for (str in strings) {
            string += str
        }
        val spannable = SpannableStringBuilder(string)
        var count = 0
        for (i in 0 until strings.count()) {
            spannable.setSpan(
                StyleSpan(typeface[i]), count, count + strings[i].count(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            textSizes?.let {
                spannable.setSpan(
                    RelativeSizeSpan(it[i]), count, count + strings[i].count(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
            count += strings[i].count()
        }
        return spannable
    }
}