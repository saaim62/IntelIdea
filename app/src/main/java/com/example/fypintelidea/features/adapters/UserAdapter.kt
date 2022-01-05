package com.example.fypintelidea.features.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.User

class UserAdapter(
    private var list: ArrayList<User>,
    private var selected: ArrayList<String>,
    private var multiSelectItemCount: (ArrayList<String>) -> Unit,
) : RecyclerView.Adapter<UserAdapter.Holder>() {

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
                list[position].id?.let { it1 -> selected.add(it1) }
            } else {
                list[position].id?.let { it1 -> selected.remove(it1) }
            }
            multiSelectItemCount(selected)
        }
        holder.checkBox.isChecked =
            selected.contains(item.id)
    }

    fun updateList(filteredList: ArrayList<User>) {
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

    private fun getItem(position: Int): User {
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