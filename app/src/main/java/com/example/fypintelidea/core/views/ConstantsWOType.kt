package com.example.fypintelidea.core.views

import android.content.Context
import com.example.fypintelidea.R
import java.util.*

class ConstantsWOType {

    companion object {

        // for API hit & should be in english only
        fun getAPIName(context: Context, name: String): String {
            val hashMap = HashMap<String, String>()
            hashMap[context.resources.getString(R.string.planned)] = "planned"
            hashMap[context.resources.getString(R.string.unplanned)] = "unplanned"
            hashMap[context.resources.getString(R.string.predictive)] = "predictive"
            return hashMap[name]!!
        }

        // for API hit & should be in english only
        fun getUserFriendlyName(context: Context, name: String): String {
            val hashMap = HashMap<String, String>()
            hashMap["planned"] = context.resources.getString(R.string.planned)
            hashMap["unplanned"] = context.resources.getString(R.string.unplanned)
            hashMap["predictive"] = context.resources.getString(R.string.predictive)
            return hashMap[name]!!
        }

        fun getDialogFriendlyName(context: Context, name: String): String {
            val hashMap = HashMap<String, String>()
            hashMap["due_date_dialog"] = context.resources.getString(R.string.due_date_dialog)
            hashMap["name"] = context.resources.getString(R.string.name)
            hashMap["assets"] = context.resources.getString(R.string.assets)
            hashMap["assignee"] = context.resources.getString(R.string.assignee)
            hashMap["sort_by"] = context.resources.getString(R.string.sort_by)
            hashMap["date_created"] = context.resources.getString(R.string.date_created)
            return hashMap[name]!!
        }
    }
}
