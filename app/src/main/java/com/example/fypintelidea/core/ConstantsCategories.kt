package com.example.fypintelidea.core

import android.content.Context
import com.example.fypintelidea.R
import java.util.*

enum class CustomerSpaces {
    curatedstage,
    stoba,
    aicher,
    qa4,
}

enum class Category {
    inspection,
    maintenance,
    downtime,
    overhaul,
    new_installation,
    retrofitting,
    calibration,
    improvement,
}

class ConstantsCategories {

    private var list: MutableList<String> = ArrayList()

    // for data showing & should be in language selected by user
    fun allCategoriesForSteiner(context: Context): List<String> {
        list.add(context.resources.getString(R.string.category_inspection))
        list.add(context.resources.getString(R.string.category_maintenance))
        list.add(context.resources.getString(R.string.category_downtime))
        list.add(context.resources.getString(R.string.category_overhaul))
        list.add(context.resources.getString(R.string.category_calibration))
        return list
    }

    // for data showing & should be in language selected by user
    fun allCategoriesForStoba(context: Context): List<String> {
        list.add(context.resources.getString(R.string.category_inspection))
        list.add(context.resources.getString(R.string.category_maintenance))
        list.add(context.resources.getString(R.string.category_downtime))
        list.add(context.resources.getString(R.string.category_overhaul))
        list.add(context.resources.getString(R.string.category_new_installation))
        list.add(context.resources.getString(R.string.category_retrofitting))
        return list
    }

    // for data showing & should be in language selected by user
    fun allCategoriesForAicher(context: Context): List<String> {
        list.add(context.resources.getString(R.string.category_inspection))
        list.add(context.resources.getString(R.string.category_maintenance))
        list.add(context.resources.getString(R.string.category_downtime))
        list.add(context.resources.getString(R.string.category_overhaul))
        list.add(context.resources.getString(R.string.category_improvement))
        return list
    }

    // for data showing & should be in language selected by user
    fun allCategoriesForQA4(context: Context): List<String> {
        list.add(context.resources.getString(R.string.category_inspection))
        list.add(context.resources.getString(R.string.category_maintenance))
        list.add(context.resources.getString(R.string.category_downtime))
        list.add(context.resources.getString(R.string.category_overhaul))
        list.add(context.resources.getString(R.string.category_calibration))
        list.add(context.resources.getString(R.string.category_new_installation))
        list.add(context.resources.getString(R.string.category_retrofitting))
        list.add(context.resources.getString(R.string.category_improvement))
        return list
    }

    // for data showing & should be in language selected by user
    fun all(context: Context): List<String> {
        list.add(context.resources.getString(R.string.category_inspection))
        list.add(context.resources.getString(R.string.category_maintenance))
        list.add(context.resources.getString(R.string.category_downtime))
        list.add(context.resources.getString(R.string.category_overhaul))
        return list
    }

    companion object {

        // for API hit & should be in english only
        fun getAPIName(context: Context, name: String): String {
            val hashMap = HashMap<String, String>()
            hashMap[context.resources.getString(R.string.category_inspection)] = "inspection"
            hashMap[context.resources.getString(R.string.category_maintenance)] = "maintenance"
            hashMap[context.resources.getString(R.string.category_downtime)] = "downtime"
            hashMap[context.resources.getString(R.string.category_overhaul)] = "overhaul"
            hashMap[context.resources.getString(R.string.category_new_installation)] =
                "new_installation"
            hashMap[context.resources.getString(R.string.category_retrofitting)] = "retrofitting"
            hashMap[context.resources.getString(R.string.category_calibration)] = "calibration"
            hashMap[context.resources.getString(R.string.category_improvement)] = "improvement"
            return hashMap[name]!!
        }

        // for API hit & should be in english only
        fun getUserFriendlyName(context: Context, name: String): String? {
            try {
                val hashMap = HashMap<String, String>()
                hashMap["inspection"] = context.resources.getString(R.string.category_inspection)
                hashMap["maintenance"] = context.resources.getString(R.string.category_maintenance)
                hashMap["downtime"] = context.resources.getString(R.string.category_downtime)
                hashMap["overhaul"] = context.resources.getString(R.string.category_overhaul)
                hashMap["new_installation"] =
                    context.resources.getString(R.string.category_new_installation)
                hashMap["retrofitting"] =
                    context.resources.getString(R.string.category_retrofitting)
                hashMap["calibration"] = context.resources.getString(R.string.category_calibration)
                hashMap["improvement"] = context.resources.getString(R.string.category_improvement)
                return hashMap[name]!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}