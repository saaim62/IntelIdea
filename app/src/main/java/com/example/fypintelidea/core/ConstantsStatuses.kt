package com.example.fypintelidea.core

import android.content.Context
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Status
import java.util.*

class ConstantsStatuses {

    private var icons = arrayOf(
        R.drawable.ic_r_pending,    //requested
        R.drawable.ic_icon_open_svg,                //open
        R.drawable.ic_r_cancelled,              //cancelled
        R.drawable.ic_in_progress_icon,         //inprogress
        R.drawable.ic_r_cancelled,               //archived
        R.drawable.ic_pause_icon,               //paused
        R.drawable.ic_done_icon                 //done
    )

    private val list = ArrayList<Status>()

    // for data showing & should be in language selected by user
    fun statusesForNewWO(mContext: Context): Collection<Status> {
        list.add(Status(mContext.resources.getString(R.string.custom_status_open), icons[1]))
        list.add(Status(mContext.resources.getString(R.string.custom_status_in_progress), icons[3]))
        list.add(Status(mContext.resources.getString(R.string.custom_status_pause), icons[5]))
        return list
    }

    // for data showing & should be in language selected by user
    fun all(mContext: Context): Collection<Status> {
        list.add(Status(mContext.resources.getString(R.string.custom_status_open), icons[1]))
        list.add(Status(mContext.resources.getString(R.string.custom_status_in_progress), icons[3]))
        list.add(Status(mContext.resources.getString(R.string.custom_status_pause), icons[5]))
        list.add(Status(mContext.resources.getString(R.string.custom_status_done), icons[6]))
        return list
    }

    companion object {
        // for API hit & should be in english only
        fun getAPIName(mContext: Context, name: String): String {
            val hashMap = HashMap<String, String>()
            hashMap[mContext.resources.getString(R.string.custom_status_requested)] = "requested"
            hashMap[mContext.resources.getString(R.string.custom_status_open)] = "pending"
            hashMap[mContext.resources.getString(R.string.custom_status_cancelled)] = "cancelled"
            hashMap[mContext.resources.getString(R.string.custom_status_in_progress)] = "doing"
            hashMap[mContext.resources.getString(R.string.custom_status_pause)] = "paused"
            hashMap[mContext.resources.getString(R.string.custom_status_archive)] = "archived"
            hashMap[mContext.resources.getString(R.string.custom_status_done)] = "done"
            return hashMap[name]!!
        }
    }
}
