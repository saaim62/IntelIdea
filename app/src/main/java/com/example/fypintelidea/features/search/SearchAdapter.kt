package com.example.fypintelidea.features.search

import com.example.fypintelidea.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.core.providers.models.Asset
import com.example.fypintelidea.core.providers.models.Workorder

class SearchAdapter(
    private var searchListToDisplay: ArrayList<*>,
    private var workOrderReturnCall: (Workorder) -> Unit,
    private var AssetReturnCall: (Asset) -> Unit
) : RecyclerView.Adapter<SearchAdapter.Holder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_row, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when (val item = searchListToDisplay[position]) {
            is Workorder -> {
                holder.tvSearchName.text = item.name
                holder.ivSearch.setBackgroundResource(R.drawable.ic_workorder_small)
                holder.tvSearchName.setOnClickListener {
                    workOrderReturnCall(item)
                }
            }
            is Asset -> {
                holder.tvSearchName.text = item.name
                holder.ivSearch.setBackgroundResource(R.drawable.ic_asset_small)
                holder.tvSearchName.setOnClickListener {
                    AssetReturnCall(item)
                }
            }
            else ->{
                Toast.makeText(holder.itemView.context, "", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun updateList(list: ArrayList<*>) {
        this.searchListToDisplay = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return searchListToDisplay.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class Holder(v: View) : RecyclerView.ViewHolder(v) {
        var tvSearchName: TextView = v.findViewById(R.id.tvSearchName)
        var ivSearch: ImageView = v.findViewById(R.id.ivSearch)
    }
}