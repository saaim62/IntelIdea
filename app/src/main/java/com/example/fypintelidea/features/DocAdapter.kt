package com.example.fypintelidea.features

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.R
import com.example.fypintelidea.core.glide
import com.example.fypintelidea.core.providers.models.Document
import com.example.fypintelidea.core.utils.LargeImageActivity
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import java.lang.Long


class DocAdapter(
    private val context: Context,
    private val rowLayout: Int,
    private var list: List<Document>?
) : RecyclerView.Adapter<DocAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(rowLayout, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val docItem = getItem(position)
        holder.tvPdfName.text = docItem?.filename
        holder.tvPdfDate.text = docItem?.date?.let {
            Long.valueOf(
                it
            )
        }?.let { MyDateTimeStamp.getDateFormattedStringFromMilliseconds(it) }

        if (!docItem?.signature!!) {
            val url: String = "https:" + docItem.url
            if (docItem.content_type == "image/jpeg" || docItem.content_type == "image/png") {
                holder.ivPdf.glide(url)
            } else {
                holder.ivPdf.setBackgroundResource(R.drawable.icon_pdf)
            }
        } else {
            holder.ivPdf.visibility = View.GONE
        }

        holder.row_document.setOnClickListener {
            val intent = Intent(context, LargeImageActivity::class.java)
            intent.putExtra(LargeImageActivity.DOC_NAME, docItem.filename)
            intent.putExtra(LargeImageActivity.DOC_URL, docItem.url)
            intent.putExtra(LargeImageActivity.DOC_TYPE, docItem.content_type)
            context.startActivity(intent)
        }
    }

    private fun getItem(position: Int): Document? {
        return list?.get(position)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class Holder(v: View) : RecyclerView.ViewHolder(v) {
        var tvPdfName: TextView = v.findViewById(R.id.tvPdfName)
        var tvPdfDate: TextView = v.findViewById(R.id.tvPdfDate)
        var ivPdf: ImageView = v.findViewById(R.id.ivPdf)
        var row_document: ConstraintLayout = v.findViewById(R.id.row_document)
    }
}