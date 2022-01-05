package com.example.fypintelidea.features.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.R

class CategoriesAdapter(
    private var list: ArrayList<String>,
    private var selected: ArrayList<String>,
    private var multiSelectItemCount: (ArrayList<String>) -> Unit,
) : RecyclerView.Adapter<CategoriesAdapter.Holder>() {

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
        holder.tvName.text = item

        holder.checkBox.setOnClickListener {
            if (holder.checkBox.isChecked) {
                list[position].let { it1 -> selected.add(it1) }
            } else {
                list[position].let { it1 -> selected.remove(it1) }
            }
            multiSelectItemCount(selected)
        }
        holder.checkBox.isChecked =
            selected.contains(item)
    }

    fun updateList(filteredList: ArrayList<String>) {
        list = filteredList
        notifyDataSetChanged()
    }

    fun updateSelectedList(item: String) {
        if (!selected.contains(item)) {
            selected.add(item)
            multiSelectItemCount(selected)
            notifyDataSetChanged()
        }
    }

    fun getSelected(): ArrayList<String> {
        return selected
    }

    private fun getItem(position: Int): String {
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