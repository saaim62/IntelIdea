package com.example.fypintelidea.features.custom_status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Status

class CustomStatusAdapter(private val context: Context, private val list: List<Status>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View? = convertView
        val holder: Holder?
        if (view == null) {
            view = inflater.inflate(R.layout.custom_spinner_row, parent, false)
            holder = Holder()
            if (view != null) {
                holder.tvName = view.findViewById(R.id.tvName)
                holder.ivIcon = view.findViewById(R.id.ivIcon)
                view.setTag(R.id.ivIcon, holder)
            }
        } else {
            holder = view.getTag(R.id.ivIcon) as Holder
        }
        val fo = list[position]
        holder.tvName!!.text = fo.name
        holder.ivIcon!!.setBackgroundResource(fo.image!!)
        return view
    }

    inner class Holder {
        internal var tvName: TextView? = null
        internal var ivIcon: ImageView? = null
    }
}