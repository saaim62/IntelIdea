package com.example.fypintelidea.core.views

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.fragment.app.FragmentActivity
import com.example.fypintelidea.R
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import com.example.fypintelidea.core.utils.Utils
import kotlinx.android.synthetic.main.layout_custom_image_view_cancelable.view.*
import java.io.File

class CustomImageViewCancelable(
    context: FragmentActivity?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context!!, attrs, defStyleAttr) {

    init {
        init(context!!)
    }

    private fun init(context: Context) {
        if (isInEditMode)
            return

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val customView: View = inflater.inflate(R.layout.layout_custom_image_view_cancelable, this)
            ?: return

    }

    fun setmImgFromPath(path: String) {
        val imgFile = File(path)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            this.img_photo.setImageBitmap(Utils.rotateImageIfNeeded(myBitmap, path))
        }
    }

    fun setmImgPhoto(drawable: Int) {
        this.img_photo.setImageResource(drawable)
    }

    fun setmImgUrl(url: String) {
        MyDateTimeStamp.setFrescoImage(this.img_photo, "https:$url")
    }

    fun getmBtnClose(): ImageButton {
        return btn_close
    }
}
