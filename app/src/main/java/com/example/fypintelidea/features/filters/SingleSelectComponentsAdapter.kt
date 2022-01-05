package com.example.fypintelidea.features.filters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.SpareParts
import com.example.fypintelidea.core.providers.models.User

class SingleSelectComponentsAdapter(
    private var itemsToDisplay: ArrayList<*>,
    private var SparePartReturnCall: (SpareParts) -> Unit,
    private var userContributorReturnCall: (User) -> Unit,
) : RecyclerView.Adapter<SingleSelectComponentsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_selected_spinner_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when (val item = itemsToDisplay[position]) {
            is SpareParts -> {
                if (item.name != null) {
                    holder.tvSpinner.text = item.name

                    holder.tvSpinner.setOnClickListener {
                        SparePartReturnCall(item)
                    }
                }
            }
            is User -> {
                if (item.name != null) {
                    holder.tvSpinner.text = item.name

                    holder.tvSpinner.setOnClickListener {
                        userContributorReturnCall(item)
                    }
                }
            }
            else -> {
                Toast.makeText(holder.itemView.context, "null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateList(filteredList: ArrayList<*>) {
        this.itemsToDisplay = filteredList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemsToDisplay.size
    }

    inner class Holder(v: View) : RecyclerView.ViewHolder(v) {
        var tvSpinner: TextView = v.findViewById(R.id.tvSpinner)
    }
}