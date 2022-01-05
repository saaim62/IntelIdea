package com.example.fypintelidea.features.workOrder.workOrderDetails

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.features.workOrder.workOrderDetails.checklistTab.WorkorderChecklistFragment
import com.example.fypintelidea.features.workOrder.workOrderDetails.detailTab.WorkOrderDetailsFragment

class WorkOrderDetailFragmentPagerAdapter(
    fm: FragmentManager, private val mContext: Context, private val workOrder: Workorder
) : FragmentPagerAdapter(
    fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WorkOrderDetailsFragment.newInstance(workOrder)
            1 -> WorkorderChecklistFragment.newInstance(workOrder)
            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> mContext.resources.getString(R.string.details_all_caps)
            1 -> mContext.resources.getString(R.string.checklist_all_caps)
            else -> null
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}

