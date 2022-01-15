package com.example.fypintelidea.features.workOrder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConstantsCategories
import com.example.fypintelidea.core.ConstantsStatuses
import com.example.fypintelidea.core.permissions.PermissionsHelper
import com.example.fypintelidea.core.providers.models.Asset
import com.example.fypintelidea.core.providers.models.Status
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import com.example.fypintelidea.core.utils.Utils
import com.example.fypintelidea.core.views.ConstantsWOType
import com.example.fypintelidea.features.custom_status.CustomStatusAdapter
import com.example.fypintelidea.features.workOrder.workOrderCompletion.WorkOrderCompleteActivity
import com.example.fypintelidea.features.workOrder.workOrderDetails.WorkOrderDetailActivity
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class WorkOrderAdapter(
    private var wos: List<Workorder>,
    private val totalAssets: List<Asset>?,
    private var changeWOStatusToOpenCallBack: (Workorder) -> Unit,
    private var changeWOStatusToInProgressCallBack: (Workorder) -> Unit,
    private var changeWOStatusToPausedCallBack: (Workorder) -> Unit,
    private var changeWOStatusToDone: (Workorder) -> Unit,
) : RecyclerView.Adapter<WorkOrderAdapter.Holder>() {

    private var assetNames = ArrayList<String>()
    private var prevPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.workorder_row, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        setAnimation(holder.cvItem, position)
        val workOrder = getItem(position)
        holder.tvAssignee.text = workOrder.assignee
        holder.tvName.text = workOrder.name

        if (!workOrder.description.isNullOrEmpty()) {
            holder.textViewAssetHierarchy.text = workOrder.description
        } else {
            holder.ivAsset.visibility = View.GONE
            holder.textViewAssetHierarchy.visibility = View.GONE
        }

        workOrder.category?.let {
            holder.tvCategory.text = ConstantsCategories.getUserFriendlyName(
                holder.tvCategory.context,
                workOrder.category!!
            )
            holder.tvCategory.visibility = View.VISIBLE
        } ?: kotlin.run {
            holder.tvCategory.text = ""
            holder.tvCategory.visibility = View.GONE
        }

        holder.tvType.text =
            workOrder.a_type?.let { ConstantsWOType.getUserFriendlyName(holder.tvType.context, it) }
        when (workOrder.a_type) {
            Workorder.WORKORDER_TYPE_PLANNED -> {
                holder.tvType.setBackgroundResource(R.drawable.shape_bottom_left_round_grey)
            }
            Workorder.WORKORDER_TYPE_UNPLANNED -> {
                holder.tvType.setBackgroundResource(R.drawable.shape_bottom_left_round_light_blue)
            }
            Workorder.WORKORDER_TYPE_PREDICTIVE -> {
                holder.tvType.setBackgroundResource(R.drawable.shape_bottom_left_round_light_blue)
            }
        }

        when {
            workOrder.priority.equals(
                Workorder.WORKORDER_PRIORITY_MEDIUM,
                ignoreCase = true
            ) -> {
                holder.llPriority.visibility = View.VISIBLE
                holder.tvPriority.visibility = View.VISIBLE
                holder.tvPriority.text = "!"
                holder.llPriority.setBackgroundResource(R.drawable.shape_bottom_right_round_grey)
            }
            workOrder.priority.equals(Workorder.WORKORDER_PRIORITY_HIGH, ignoreCase = true) -> {
                holder.llPriority.visibility = View.VISIBLE
                holder.tvPriority.visibility = View.VISIBLE
                holder.tvPriority.text = "!!"
                holder.llPriority.setBackgroundResource(R.drawable.shape_bottom_right_round_red)
            }
            else -> {
                holder.llPriority.visibility = View.GONE
                holder.tvPriority.visibility = View.GONE
            }
        }

        holder.tvDueDate.setTextColor(
            ContextCompat.getColor(
                holder.tvDueDate.context,
                R.color.Color_Default
            )
        )
        if (workOrder.created_at != null) {
            if (!workOrder.created_at.equals("0", ignoreCase = true)) {
                val dueDateFormatted =
                    MyDateTimeStamp.getDateTimeFormattedStringFromMilliseconds(
                        java.lang.Long.valueOf(workOrder.created_at!!)
                    )
                holder.tvDueDate.text = dueDateFormatted
                if (Calendar.getInstance().time.after(
                        MyDateTimeStamp.dateTimeStringToDate(
                            dueDateFormatted
                        )
                    )
                ) {
                    holder.tvDueDate.setTextColor(
                        ContextCompat.getColor(
                            holder.tvDueDate.context,
                            R.color.Color_Red
                        )
                    )
                }
            } else {
                holder.tvDueDate.text = "-"
            }
        } else {
            holder.tvDueDate.text = "-"
        }

        val list = ArrayList<Status>()
        val constantsStatuses = ConstantsStatuses()
        list.addAll(constantsStatuses.all(holder.spStatus.context))
        holder.spStatus.setBackgroundResource(R.drawable.spinner_background_bottom_edges_round)
        holder.spStatus.adapter = CustomStatusAdapter(holder.spStatus.context, list)
        when {
            workOrder.status.equals(
                Workorder.WORKORDER_STATUS_OPEN,
                ignoreCase = true,
            ) -> holder.spStatus.setSelection(0, false)
            workOrder.status.equals(
                Workorder.WORKORDER_STATUS_INPROGRESS,
                ignoreCase = true
            ) -> holder.spStatus.setSelection(1, false)
            workOrder.status.equals(
                Workorder.WORKORDER_STATUS_PAUSED,
                ignoreCase = true
            ) -> holder.spStatus.setSelection(2, false)
            workOrder.status.equals(
                Workorder.WORKORDER_STATUS_DONE,
                ignoreCase = true
            ) -> holder.spStatus.setSelection(3, false)
        }

        val onSpinnerInteractionListener =
            object : AdapterView.OnItemSelectedListener, View.OnTouchListener {
                var isTouched = false

                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    isTouched = true
                    return false
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (isTouched) {
                        var selectedStatus: String? = null
                        when (position) {
                            0 -> selectedStatus = ConstantsStatuses.getAPIName(
                                view.context,
                                view.context.resources.getString(R.string.custom_status_open)
                            )
                            1 -> selectedStatus = ConstantsStatuses.getAPIName(
                                view.context,
                                view.context.resources.getString(R.string.custom_status_in_progress)
                            )
                            2 -> selectedStatus = ConstantsStatuses.getAPIName(
                                view.context,
                                view.context.resources.getString(R.string.custom_status_pause)
                            )
                            3 -> selectedStatus = ConstantsStatuses.getAPIName(
                                view.context,
                                view.context.resources.getString(R.string.custom_status_done)
                            )
                        }

                        if (selectedStatus == Workorder.WORKORDER_STATUS_OPEN) {
                            Toast.makeText(
                                view.context,
                                "Changing status to open",
                                Toast.LENGTH_SHORT
                            ).show()
                            changeWOStatusToOpen(workOrder)
                        } else if (selectedStatus == Workorder.WORKORDER_STATUS_INPROGRESS) {
                            Toast.makeText(
                                view.context,
                                "Changing status to in-progress",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (PermissionsHelper.canInProgressWorkOrder(
                                    view.context,
                                    workOrder
                                )
                            ) {
                                changeWOStatusToInProgress(workOrder)
                            } else {
                                Toast.makeText(
                                    view.context,
                                    R.string.access_denied,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (selectedStatus == Workorder.WORKORDER_STATUS_PAUSED) {
                            Toast.makeText(
                                view.context,
                                "Changing status to paused",
                                Toast.LENGTH_SHORT
                            ).show()
                            changeWOStatusToPaused(workOrder)
                        } else if (selectedStatus == Workorder.WORKORDER_STATUS_DONE) {
                            Toast.makeText(
                                view.context,
                                "Changing status to done",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (PermissionsHelper.canDoneWorkOrder(view.context, workOrder)) {
                                if (workOrder.chklistSections != null && workOrder.chklistSections?.isNotEmpty() == true) {
                                    if (Utils.areRequiredChklistFieldsFilled(workOrder)) {
                                        changeWOStatusToDone.invoke(workOrder)
                                    } else {
                                        // TODO: 3/17/2019 navigate to checklist tab as well and highlight inline error.
                                        Toast.makeText(
                                            view.context,
                                            view.context.resources.getString(R.string.cannot_complete_workorder) + "\n" + view.context.resources.getString(
                                                R.string.please_fill_out_required_checklsit_fields
                                            ),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    view.context,
                                    R.string.access_denied,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        holder.spStatus.setOnTouchListener(onSpinnerInteractionListener)
        holder.spStatus.onItemSelectedListener = onSpinnerInteractionListener
        holder.cl.setOnClickListener()
        {
            val i = Intent(it.context, WorkOrderDetailActivity::class.java)
            i.putExtra(WorkOrderDetailActivity.SELECTED_WORK_ORDER, workOrder as Serializable?)
            it.context.startActivity(i)
        }

    }

    private fun getItem(position: Int): Workorder {
        return wos[position]
    }

    override fun getItemCount(): Int {
        return wos.size
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > prevPos) {
            val animation = AnimationUtils.loadAnimation(
                viewToAnimate.context,
                android.R.anim.slide_in_left
            )
            viewToAnimate.startAnimation(animation)
            prevPos = position
        }
    }

    private fun moveToWOCompletionScreen(context: Context, wo: Workorder?, holder: Holder) {
        val i = Intent(context, WorkOrderCompleteActivity::class.java)
        i.putExtra(WorkOrderDetailActivity.SELECTED_WORK_ORDER, wo as Serializable?)
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
            context as Activity,
            holder.tvName,
            "workorderName"
        ).toBundle()
        context.startActivity(i, bundle)
    }

    private fun changeWOStatusToOpen(wo: Workorder) {
        changeWOStatusToOpenCallBack(wo)
    }

    private fun changeWOStatusToInProgress(wo: Workorder) {
        changeWOStatusToInProgressCallBack(wo)
    }

    private fun changeWOStatusToPaused(wo: Workorder) {
        changeWOStatusToPausedCallBack(wo)
    }

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        internal val spStatus: Spinner = v.findViewById(R.id.spStatus)
        internal val tvAssignee: TextView = v.findViewById(R.id.tvAssignee)
        internal val tvName: TextView = v.findViewById(R.id.tvName)
        internal val tvType: TextView = v.findViewById(R.id.tvType)
        internal var tvCategory: TextView = v.findViewById(R.id.tvCategory)
        internal val textViewAssetHierarchy: TextView = v.findViewById(R.id.textViewAssetHierarchy)
        internal val ivAsset: ImageView = v.findViewById(R.id.imageView2)
        internal val tvDueDate: TextView = v.findViewById(R.id.tvDueDate)
        internal val llPriority: LinearLayout = v.findViewById(R.id.llPriority)
        internal val tvPriority: TextView = v.findViewById(R.id.tvPriority)
        var cl: ConstraintLayout = v.findViewById(R.id.cl)
        var cvItem: CardView = v.findViewById(R.id.cv_item)
    }

    private fun findTopLevelParent(
        totalAssets: List<Asset>,
        assetId: String?
    ): List<String>? {
        for (oneAsset in totalAssets) {
            if (oneAsset.id == assetId) {
                if (oneAsset.parent_id != null) {
                    assetNames.add(oneAsset.name ?: "")
                    findTopLevelParent(totalAssets, oneAsset.parent_id)
                } else {
                    assetNames.add(oneAsset.name ?: "")
                    break
                }
            }
        }
        return if (assetNames.size > 0) {
            assetNames.reversed()
        } else {
            null
        }
    }

    fun updateList(list: ArrayList<Workorder>) {
        this.wos = list
        notifyDataSetChanged()
    }
}