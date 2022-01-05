package com.example.fypintelidea.features.workOrder.workOrderDetails.checklistTab

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Article
import com.example.fypintelidea.core.providers.models.Document
import com.example.fypintelidea.core.utils.LargeImageActivity


class CheckListDocItemAdapter(
    private val context: FragmentActivity,
    private var list: List<Any>
) : RecyclerView.Adapter<CheckListDocItemAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_dummy_string, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        when (item) {
            is Document -> {
                holder.checkListItemDoc.text = item.filename
                holder.checkListItemDoc.setOnClickListener {
                    val intent = Intent(holder.itemView.context, LargeImageActivity::class.java)
                    intent.putExtra(LargeImageActivity.DOC_NAME, item.filename)
                    intent.putExtra(LargeImageActivity.DOC_URL, item.url)
                    intent.putExtra(LargeImageActivity.DOC_TYPE, item.content_type)
                    holder.itemView.context.startActivity(intent)
                }
            }
            is Article -> {
                holder.checkListItemDoc.text = item.title
//                holder.checkListItemDoc.setOnClickListener {
//                    context.startActivity(
//                        Intent(context, ArticleActivity::class.java).putExtra(
//                            ArticleActivity.ARTICLE_ID,
//                            item.id
//                        )
//                    )
//                }
            }
            else -> {
                Toast.makeText(holder.itemView.context, "", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(v: View) : RecyclerView.ViewHolder(v) {
        var checkListItemDoc: TextView = v.findViewById(R.id.checkListItemDoc)
    }
}