package com.example.fypintelidea.features.workOrder.workorderguide

import android.os.Bundle
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.providers.models.WorkOrderRecommendation
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.features.workOrder.workOrderDetails.WorkOrderDetailActivity
import kotlinx.android.synthetic.main.activity_work_order_guide.*


class WorkOrderGuideActivity : ConnectavoBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_order_guide)

        imageView_backArrow.setOnClickListener { onBackPressed() }

        val workOrder =
            intent.getSerializableExtra(WorkOrderDetailActivity.SELECTED_WORK_ORDER) as Workorder
        val recommendationList =
            intent.getSerializableExtra(WorkOrderDetailActivity.SELECTED_WORK_ORDER_RECOMMENDATIONS) as ArrayList<WorkOrderRecommendation.Recommendation>
        val transaction = supportFragmentManager.beginTransaction()
        workOrder.id?.let { WorkOrderGuideFragment.newInstance(it, recommendationList) }?.let {
            transaction.replace(
                R.id.flFragmentContainer,
                it
            )
        }
        transaction.commit()

    }
}