package com.example.fypintelidea.core

import android.widget.EditText
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.fypintelidea.R

internal fun ImageView.glide(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.icon_img)
        .apply(
            RequestOptions().transform(RoundedCorners(25))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProcessDrawable(this))
        )
        .into(this)
}

private fun circularProcessDrawable(imageView: ImageView): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(imageView.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    return circularProgressDrawable
}

fun EditText.getTrimmedText(): String {
    var string = this.text.toString()
    string = string.trim()
    string = string.replace(Regex("\\s+"), " ")
    return string
}