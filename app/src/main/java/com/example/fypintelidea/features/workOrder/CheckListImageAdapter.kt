package com.example.fypintelidea.features.workOrder

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.core.providers.models.Image
import com.example.fypintelidea.R
import com.example.fypintelidea.core.glide
import com.example.fypintelidea.core.utils.LargeImageActivity


class CheckListImageAdapter(
    private var list: List<Image>
) : RecyclerView.Adapter<CheckListImageAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_dummy_image, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        val url: String = "https:" + item.url
        holder.IvCheckList.glide(url)

        holder.IvCheckList.setOnClickListener {
            val intent = Intent(holder.itemView.context, LargeImageActivity::class.java)
            intent.putExtra(LargeImageActivity.DOC_NAME, item.filename)
            intent.putExtra(LargeImageActivity.DOC_URL, item.url)
            intent.putExtra(LargeImageActivity.DOC_TYPE, item.content_type)
            holder.itemView.context.startActivity(intent)
        }
    }

    private fun getItem(position: Int): Image {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(v: View) : RecyclerView.ViewHolder(v) {
        var IvCheckList: ImageView = v.findViewById(R.id.imageView10)
    }
}