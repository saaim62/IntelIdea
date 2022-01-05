package com.example.fypintelidea.features.workOrder.workorderguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseFragment
import com.example.fypintelidea.core.providers.models.RecommendationFeedbackRequest
import com.example.fypintelidea.core.providers.models.WorkOrderRecommendation
import kotlinx.android.synthetic.main.fragment_workorder_guide.*
import org.koin.android.ext.android.inject

class WorkOrderGuideFragment : ConnectavoBaseFragment() {
    val workOrderGuideFragmentViewModel: WorkOrderGuideFragmentViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workorder_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val selectedWorkOrderId = bundle?.getString(SELECTED_WORK_ORDER_ID)
        val recommendationList =
            bundle?.getSerializable(SELECTED_WORK_ORDER_RECOMMENDATIONS) as ArrayList<WorkOrderRecommendation.Recommendation>
        if (selectedWorkOrderId != null && !recommendationList.isNullOrEmpty()) {
            recyclerView_recommendationsList?.adapter =
                WorkOrderRecommendationsAdapter(selectedWorkOrderId, recommendationList)
        }
        val recommendationFeedbackRequest: RecommendationFeedbackRequest? = null
        if (recommendationFeedbackRequest != null) {
            progressBarWorkOrderGuide.visibility = View.VISIBLE
            workOrderGuideFragmentViewModel.postRecommendationFeedback(
                recommendationFeedbackRequest
            )
            setObservers()
        }

    }

    private fun setObservers() {
        activity?.let { activity ->
            workOrderGuideFragmentViewModel.workOrderGuideObservable.observe(activity) {
                val apiResponse = it.value
                //            recommendationFeedbackRequest.currentFeedback.let {
                //                tempSelectedFeedback.invoke(it.toInt())
                //                updateFeedbackButtonsUI(view, it)
                //            }

                Toast.makeText(
                    context,
                    context?.resources?.getString(R.string.successfully_updated),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        fun newInstance(
            workOrderId: String,
            recommendationList: ArrayList<WorkOrderRecommendation.Recommendation>
        ): WorkOrderGuideFragment {
            val fragment = WorkOrderGuideFragment()
            val args = Bundle()
            args.putString(SELECTED_WORK_ORDER_ID, workOrderId)
            args.putSerializable(SELECTED_WORK_ORDER_RECOMMENDATIONS, recommendationList)
            fragment.arguments = args
            return fragment
        }

        private const val SELECTED_WORK_ORDER_ID = "selected_work_order_id"
        private const val SELECTED_WORK_ORDER_RECOMMENDATIONS =
            "selected_work_order_recommendations"
    }
}