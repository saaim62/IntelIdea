package com.example.fypintelidea.features.workOrder.workorderguide

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.core.providers.models.RecommendationFeedbackRequest
import com.example.fypintelidea.core.providers.models.RecommendationFeedbackResponse
import com.example.fypintelidea.core.providers.models.WorkOrderRecommendation
import com.example.fypintelidea.core.services.ApiClient
import com.example.fypintelidea.core.services.ApiInterface
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.R
import com.example.fypintelidea.core.utils.LargeImageActivity
import kotlinx.android.synthetic.main.item_work_order_recommendations.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class WorkOrderRecommendationsAdapter(
    private val selectedWOId: String,
    private val recommendationsList: List<WorkOrderRecommendation.Recommendation>,
) : RecyclerView.Adapter<WorkOrderRecommendationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_work_order_recommendations, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recommendationsList[position])
    }

    override fun getItemCount(): Int {
        return recommendationsList.size
    }

    fun getItem(id: Int): WorkOrderRecommendation.Recommendation {
        return recommendationsList[id]
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(recommendationItem: WorkOrderRecommendation.Recommendation) {
            itemView.tvRecommendationName.text = recommendationItem.name
            itemView.tvAssetName.text = recommendationItem.assetName
            addDotsToLinearLayout(recommendationItem.score.roundToInt(), itemView.llDottedView)

            setFeedbackButtonListeners(itemView, recommendationItem) { tempSelectedFeedback ->
                recommendationItem.tempSelectedFeedback = tempSelectedFeedback
            }

            if (recommendationItem.type.equals(RecommendationType.asset_doc.name)) {

                itemView.tvRecommendationName.setOnClickListener {
                    val intent = Intent(itemView.context, LargeImageActivity::class.java)
                    intent.putExtra(LargeImageActivity.DOC_URL, recommendationItem.url)
                    intent.putExtra(LargeImageActivity.DOC_NAME, recommendationItem.name)
                    itemView.context.startActivity(intent)
                }

                hideViews(this)
                itemView.tvRecommendationHeading.text = "Page ${recommendationItem.page_no}"
                itemView.imageView_tick.setImageResource(R.drawable.ic_document_light_small)
                recommendationItem.excerpt?.let {
                    itemView.tvRecommendationDescription.text =
                        HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                            .trim()
                }

            } else {

                itemView.imageView_tick.setImageResource(R.drawable.ic_workorder_small_svg)
                if (recommendationItem.comment.isNullOrBlank()) {
                    if (recommendationItem.problem.isNullOrBlank()) {
                        itemView.tvRecommendationDescription.text =
                            itemView.context.getString(R.string.no_problem_provided)
                    } else {
                        itemView.tvRecommendationDescription.text = recommendationItem.problem
                    }

                    if (recommendationItem.solution.isNullOrBlank()) {
                        itemView.tvRecommendationDescriptionSecond.text =
                            itemView.context.getString(R.string.no_solution_provided)
                    } else {
                        itemView.tvRecommendationDescriptionSecond.text =
                            recommendationItem.solution
                    }
                } else {
                    itemView.tvRecommendationHeading.text =
                        itemView.context.getString(R.string.comment)
                    itemView.tvRecommendationDescription.text = recommendationItem.comment
                    itemView.tvRecommendationHeading.visibility = View.VISIBLE
                    itemView.tvRecommendationDescription.visibility = View.VISIBLE
                    itemView.tvRecommendationHeadingSecond.visibility = View.GONE
                    itemView.tvRecommendationDescriptionSecond.visibility = View.GONE
                }

            }
        }
    }

    private fun setFeedbackButtonListeners(
        itemView: View,
        recommendationItem: WorkOrderRecommendation.Recommendation,
        tempSelectedFeedback: (Int) -> Unit
    ) {
        itemView.ivThumbUp.setOnClickListener {
            if (it.ivThumbUp.drawable.constantState == ContextCompat.getDrawable(
                    itemView.context, R.drawable.ic_thumbs_up
                )!!.constantState
            ) {
                prepareFeedbackRequest(
                    itemView,
                    recommendationItem,
                    "1"
                ) { result ->
                    tempSelectedFeedback.invoke(result)
                }
            } else {
                prepareFeedbackRequest(
                    itemView,
                    recommendationItem,
                    "0"
                ) { result ->
                    tempSelectedFeedback.invoke(result)
                }
            }
        }

        itemView.ivThumbDown.setOnClickListener {
            if (it.ivThumbDown.drawable.constantState == ContextCompat.getDrawable(
                    itemView.context, R.drawable.ic_thumbs_down
                )!!.constantState
            ) {
                prepareFeedbackRequest(
                    itemView,
                    recommendationItem,
                    "-1"
                ) { result ->
                    tempSelectedFeedback.invoke(result)
                }
            } else {
                prepareFeedbackRequest(
                    itemView,
                    recommendationItem,
                    "0"
                ) { result ->
                    tempSelectedFeedback.invoke(result)
                }
            }
        }
    }

    private fun prepareFeedbackRequest(
        view: View,
        recommendationItem: WorkOrderRecommendation.Recommendation,
        currentFeedback: String,
        tempSelectedFeedback: (Int) -> Unit
    ) {
        val recommendationFeedbackRequest = RecommendationFeedbackRequest()
        recommendationFeedbackRequest.parentDocID = selectedWOId
        recommendationFeedbackRequest.recommendedDocID = recommendationItem.documentStoreId
        recommendationFeedbackRequest.recommendedDocType = recommendationItem.type
        recommendationFeedbackRequest.score = recommendationItem.score.toString()
        recommendationFeedbackRequest.currentFeedback = currentFeedback
        recommendationFeedbackRequest.previousFeedback =
            recommendationItem.tempSelectedFeedback.toString()

        postRecommendationFeedBacks(
            view,
            recommendationFeedbackRequest,
        ) { success ->
            when (success) {
                true -> {
                    recommendationFeedbackRequest.currentFeedback?.let {
                        tempSelectedFeedback.invoke(it.toInt())
                        updateFeedbackButtonsUI(view, it)
                    }
                    Toast.makeText(
                        view.context,
                        view.context.resources.getString(R.string.successfully_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                false -> {
                    Toast.makeText(
                        view.context,
                        view.context.resources.getString(R.string.check_internet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun postRecommendationFeedBacks(
        itemView: View,
        recommendationFeedbackRequest: RecommendationFeedbackRequest,
        result: (Boolean) -> Unit
    ) {
        SessionManager(itemView.context).getString(SessionManager.KEY_LOGIN_EMAIL)?.let {
            SessionManager(itemView.context).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it1 ->
                ApiClient.getClient(itemView.context)?.create(ApiInterface::class.java)
                    ?.postRecommendationFeedback(
                        it,
                        it1,
                        recommendationFeedbackRequest
                    )?.enqueue(object : Callback<RecommendationFeedbackResponse> {
                        override fun onResponse(
                            call: Call<RecommendationFeedbackResponse>,
                            response: Response<RecommendationFeedbackResponse>
                        ) {
                            try {
                                val statusCode = response.code()
                                if (response.isSuccessful) {
                                    if (response.body() != null && statusCode == 200) {
                                        result.invoke(true)
                                    }
                                }
                            } catch (e: Exception) {
                            }
                        }

                        override fun onFailure(
                            call: Call<RecommendationFeedbackResponse>,
                            t: Throwable
                        ) {
                            result.invoke(false)
                        }
                    })
            }
        }
    }

    private fun updateFeedbackButtonsUI(itemView: View, currentFeedback: String) {
        when (currentFeedback) {
            "-1" -> {
                itemView.ivThumbUp.setImageResource(R.drawable.ic_thumbs_up)
                itemView.ivThumbDown.setImageResource(R.drawable.ic_thumbs_down_filled)
            }
            "0" -> {
                itemView.ivThumbUp.setImageResource(R.drawable.ic_thumbs_up)
                itemView.ivThumbDown.setImageResource(R.drawable.ic_thumbs_down)
            }
            "1" -> {
                itemView.ivThumbUp.setImageResource(R.drawable.ic_thumbs_up_filled)
                itemView.ivThumbDown.setImageResource(R.drawable.ic_thumbs_down)
            }
        }
    }

    private fun hideViews(viewHolder: ViewHolder) {
        viewHolder.itemView.tvRecommendationHeadingSecond.visibility = View.GONE
        viewHolder.itemView.tvRecommendationDescriptionSecond.visibility = View.GONE
    }

    private fun addDotsToLinearLayout(score: Int, layout: LinearLayout) {
        layout.removeAllViews()

        for (i in 1..5) {
            val imageView = ImageView(layout.context)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(11, 0, 0, 0)
            imageView.layoutParams = layoutParams

            if (i <= score) {
                imageView.setBackgroundResource(R.drawable.shape_dot_black)
            } else {
                imageView.setBackgroundResource(R.drawable.shape_dot_gray)
            }
            layout.addView(imageView)
        }
    }
}