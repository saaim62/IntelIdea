package com.example.fypintelidea.features.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Tag

class TagsAdapter(
    private var list: ArrayList<Tag>,
    private var selected: ArrayList<Tag>,
    private var multiSelectItemCount: (ArrayList<Tag>) -> Unit,
) : RecyclerView.Adapter<TagsAdapter.Holder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.multi_select_asset_row, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        if (item.name != null) {
            holder.tvName.text = item.name
        }

        holder.checkBox.setOnClickListener {
            if (holder.checkBox.isChecked) {
                list[position].let { it1 -> selected.add(it1) }
            } else {
                list[position].let { it1 -> selected.remove(it1) }
            }
            multiSelectItemCount(selected)
        }
        selected.forEach {
            if (it.id == item.id) {
                holder.checkBox.isChecked = true
            }
        }
    }

    fun updateList(filteredList: ArrayList<Tag>) {
        list = filteredList
        notifyDataSetChanged()
    }


    fun getSelected(): ArrayList<Tag> {
        return selected
    }

    private fun getItem(position: Int): Tag {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(v: View) : RecyclerView.ViewHolder(v) {
        var tvName: TextView = v.findViewById(R.id.tvName)
        var checkBox: CheckBox = v.findViewById(R.id.checkBox)
    }
}