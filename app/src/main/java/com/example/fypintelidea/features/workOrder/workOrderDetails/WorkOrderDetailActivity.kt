package com.example.fypintelidea.features.workOrder.workOrderDetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.example.fypintelidea.core.providers.models.Status
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.core.views.ConstantsWOType
import com.example.fypintelidea.features.custom_status.CustomStatusAdapter
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.ConstantsCategories
import com.example.fypintelidea.core.ConstantsStatuses
import com.example.fypintelidea.core.permissions.PermissionsHelper
import com.example.fypintelidea.core.utils.Utils
import com.example.fypintelidea.core.views.CustomViewPager
import com.example.fypintelidea.features.workOrder.workOrderCompletion.WorkOrderCompleteActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_workorder_detail.*
import org.koin.android.ext.android.inject
import java.io.Serializable

class WorkOrderDetailActivity : ConnectavoBaseActivity() {
    private val workOrderDetailActivityViewModel: WorkOrderDetailActivityViewModel by inject()
    private var workOrder: Workorder? = null
    internal var mContext: Context = this
    private var selectedStatus: String? = null
    private var check = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workorder_detail)

        imageView_backArrow.setOnClickListener { onBackPressed() }
        ivBack.setOnClickListener { finish() }

        workOrder = intent.getSerializableExtra(SELECTED_WORK_ORDER) as Workorder?
        val isForceRefresh = intent.getBooleanExtra(IS_FORCE_REFRESH, false)
        if (isForceRefresh) {
            progressBarWorkOrderDetail?.visibility = View.VISIBLE
            workOrder?.search_id?.let { workOrderDetailActivityViewModel.getSingleWorkOrder(it) }
        } else {
            populateData()
        }
        setObservers()
    }

    private fun populateData() {
        coordinatorLayoutWorkOrderDetail?.visibility = View.VISIBLE
        workOrder?.let { workOrder ->
            //  status dropdown
            addItemsOnSpinnerStatus()

            //  workorder priority
            when (this.workOrder?.priority) {
                Workorder.WORKORDER_PRIORITY_MEDIUM -> {
                    imageView_priority?.visibility = View.VISIBLE
                    imageView_priority?.text = "!"
                    imageView_priority?.setBackgroundResource(R.drawable.shape_circle_dark_gray_bg)
                }
                Workorder.WORKORDER_PRIORITY_HIGH -> {
                    imageView_priority?.visibility = View.VISIBLE
                    imageView_priority?.text = "!!"
                    imageView_priority?.setBackgroundResource(R.drawable.shape_circle_red_bg)
                }
                else -> {
                    imageView_priority?.visibility = View.GONE
                }
            }

            tvName?.text = this.workOrder?.name
            if (this.workOrder?.category.isNullOrBlank()) {
                tvCategory?.visibility = View.GONE
            } else {
                tvCategory?.visibility = View.VISIBLE
                tvCategory?.text =
                    workOrder.category?.let { ConstantsCategories.getUserFriendlyName(this, it) }
            }

            if (workOrder.a_type != null) {
                tvType.text = ConstantsWOType.getUserFriendlyName(this, workOrder.a_type!!)
                when (workOrder.a_type) {
                    Workorder.WORKORDER_TYPE_PLANNED -> {
                        tvType.setBackgroundResource(R.drawable.shape_8dp_round_grey)
                    }
                    Workorder.WORKORDER_TYPE_UNPLANNED -> {
                        tvType.setBackgroundResource(R.drawable.shape_8dp_round_light_blue)
                    }
                    Workorder.WORKORDER_TYPE_PREDICTIVE -> {
                        tvType.setBackgroundResource(R.drawable.shape_8dp_round_light_blue)
                    }
                }
            }

            val viewPager = findViewById<CustomViewPager>(R.id.viewpager)
            val contactDetailsFragmentPagerAdapter = WorkOrderDetailFragmentPagerAdapter(
                supportFragmentManager,
                mContext,
                workOrder
            )
            viewPager.adapter = contactDetailsFragmentPagerAdapter

            // Give the TabLayout the ViewPager
            val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
            tabLayout?.tabGravity = TabLayout.GRAVITY_FILL
            tabLayout?.setupWithViewPager(viewPager)

            viewPager?.isPagingEnabled = false
            tabLayout?.visibility = View.GONE
            if (workOrder.chklistSections != null && workOrder.chklistSections?.isNotEmpty() == true) {
                viewPager?.isPagingEnabled = true
                tabLayout?.visibility = View.VISIBLE
            }
        }
    }

    private fun addItemsOnSpinnerStatus() {
        val list = ArrayList<Status>()
        val constantsStatuses = ConstantsStatuses()
        list.addAll(constantsStatuses.all(this))
        spStatus?.adapter = CustomStatusAdapter(this, list)
        spStatus.setBackgroundResource(R.drawable.spinner_background_bottom_edges_round)
        spStatus.onItemSelectedListener = SpinnerStatusOnItemSelectedListener()

        workOrder?.let { workOrder ->
            when {
                workOrder.status.equals(
                    Workorder.WORKORDER_STATUS_OPEN,
                    ignoreCase = true
                ) -> spStatus.setSelection(0)
                workOrder.status.equals(
                    Workorder.WORKORDER_STATUS_INPROGRESS,
                    ignoreCase = true
                ) -> spStatus.setSelection(1)
                workOrder.status.equals(
                    Workorder.WORKORDER_STATUS_PAUSED,
                    ignoreCase = true
                ) -> spStatus.setSelection(2)
                workOrder.status.equals(
                    Workorder.WORKORDER_STATUS_DONE,
                    ignoreCase = true
                ) -> spStatus.setSelection(3)
            }
        }
    }

    private inner class SpinnerStatusOnItemSelectedListener : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            workOrder?.let { workOrder ->
                if (++check > 1) {
                    when (position) {
                        0 -> selectedStatus = ConstantsStatuses.getAPIName(
                            this@WorkOrderDetailActivity,
                            resources.getString(R.string.custom_status_open)
                        )
                        1 -> selectedStatus = ConstantsStatuses.getAPIName(
                            this@WorkOrderDetailActivity,
                            resources.getString(R.string.custom_status_in_progress)
                        )
                        2 -> selectedStatus = ConstantsStatuses.getAPIName(
                            this@WorkOrderDetailActivity,
                            resources.getString(R.string.custom_status_pause)
                        )
                        3 -> selectedStatus = ConstantsStatuses.getAPIName(
                            this@WorkOrderDetailActivity,
                            resources.getString(R.string.custom_status_done)
                        )
                    }

                    if (selectedStatus == Workorder.WORKORDER_STATUS_OPEN) {
                        Toast.makeText(
                            this@WorkOrderDetailActivity,
                            getString(R.string.changing_status_to_open),
                            Toast.LENGTH_SHORT
                        ).show()
                        changeWOStatusToOpen(workOrder)
                    } else if (selectedStatus == Workorder.WORKORDER_STATUS_INPROGRESS) {
                        Toast.makeText(
                            this@WorkOrderDetailActivity,
                            getString(R.string.changing_status_to_inprogress),
                            Toast.LENGTH_SHORT
                        ).show()
                        if (PermissionsHelper.canInProgressWorkOrder(
                                this@WorkOrderDetailActivity,
                                this@WorkOrderDetailActivity.workOrder
                            )
                        ) {
                            changeWOStatusToInProgress(workOrder)
                        } else {
                            Toast.makeText(
                                this@WorkOrderDetailActivity,
                                R.string.access_denied,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (selectedStatus == Workorder.WORKORDER_STATUS_PAUSED) {
                        Toast.makeText(
                            this@WorkOrderDetailActivity,
                            getString(R.string.changing_status_to_pause),
                            Toast.LENGTH_SHORT
                        ).show()
                        changeWOStatusToPaused(workOrder)
                    } else if (selectedStatus == Workorder.WORKORDER_STATUS_DONE) {
                        Toast.makeText(
                            this@WorkOrderDetailActivity,
                            getString(R.string.changing_status_to_done),
                            Toast.LENGTH_SHORT
                        ).show()
                        if (PermissionsHelper.canDoneWorkOrder(
                                this@WorkOrderDetailActivity,
                                this@WorkOrderDetailActivity.workOrder
                            )
                        ) {
                            if (workOrder.chklistSections != null && workOrder.chklistSections?.isNotEmpty() == true) {
                                if (Utils.areRequiredChklistFieldsFilled(workOrder)) {
                                    val mapChecklist = Utils.submitChklistDataToServer(workOrder)
                                    progressBarWorkOrderDetail?.visibility = View.VISIBLE
                                    workOrder.chklist_id?.let {
                                        workOrderDetailActivityViewModel.submitChklistMultipart(
                                            it,
                                            mapChecklist
                                        )
                                    }
                                } else {
                                    // TODO: 3/17/2019 navigate to checklist tab as well and highlight inline error.
                                    Toast.makeText(
                                        this@WorkOrderDetailActivity,
                                        resources.getString(R.string.cannot_complete_workorder) + "\n" + resources.getString(
                                            R.string.please_fill_out_required_checklsit_fields
                                        ),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@WorkOrderDetailActivity,
                                R.string.access_denied,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        override fun onNothingSelected(adapterView: AdapterView<*>) {

        }
    }

    private fun changeWOStatusToOpen(wo: Workorder) {
        progressBarWorkOrderDetail?.visibility = View.VISIBLE
        workOrder = wo
        workOrderDetailActivityViewModel.updateWorkOrderToOpenState(wo)
    }

    private fun changeWOStatusToInProgress(wo: Workorder) {
        progressBarWorkOrderDetail?.visibility = View.VISIBLE
        workOrder = wo
        workOrderDetailActivityViewModel.updateWorkOrderToInProgress(wo)
    }

    private fun changeWOStatusToPaused(wo: Workorder) {
        progressBarWorkOrderDetail?.visibility = View.VISIBLE
        workOrder = wo
        workOrderDetailActivityViewModel.updateWorkOrderToPauseState(wo)
    }

    private fun setObservers() {
        workOrderDetailActivityViewModel.workOrderObservable.observe(this) {
            if (it.error != null) {
                clAccessDeniedWorkOrder.visibility = View.VISIBLE
                coordinatorLayoutWorkOrderDetail.visibility = View.GONE
            } else {
                val apiResponse = it.value
                progressBarWorkOrderDetail?.visibility = View.GONE
                val isForceRefresh = intent.getBooleanExtra(IS_FORCE_REFRESH, false)
                if (isForceRefresh) {
                    workOrder = apiResponse
                    populateData()
                    clAccessDeniedWorkOrder?.visibility = View.GONE
                    coordinatorLayoutWorkOrderDetail?.visibility = View.VISIBLE
                } else {
                    apiResponse.chklistSections?.forEachIndexed { _, oneSectionTemp ->
                        oneSectionTemp.chklistItem?.forEachIndexed { _, oneChklistItemTemp ->
                            workOrder?.chklistSections?.forEachIndexed { _, oneSection ->
                                oneSection.chklistItem?.forEachIndexed { _, oneChklistItem ->
                                    if (oneChklistItemTemp.id == oneChklistItem.id) {
                                        oneChklistItem.images =
                                            oneChklistItemTemp.images
                                        // set them to null so that it does not re sync on pressing save button again.
                                        oneChklistItem.docs_attributes_base64 = null
                                        oneChklistItem.photoPaths = null
                                    }
                                }
                            }
                        }
                    }

                    if (apiResponse.status != "500") {
                        progressBarWorkOrderDetail.visibility = View.GONE
                    }
                }
            }
        }

        workOrderDetailActivityViewModel.wosObservable.observe(this) {
            if (it.error != null) {
                clAccessDeniedWorkOrder.visibility = View.VISIBLE
                coordinatorLayoutWorkOrderDetail.visibility = View.GONE
            } else {
                val apiResponse = it.value
                progressBarWorkOrderDetail?.visibility = View.GONE
                workOrder?.status = apiResponse?.status
                Toast.makeText(
                    this,
                    this.resources?.getString(R.string.successfully_updated),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun moveToWOCompletionScreen(workOrder: Workorder) {
        val intent = Intent(this@WorkOrderDetailActivity, WorkOrderCompleteActivity::class.java)
        intent.putExtra(SELECTED_WORK_ORDER, workOrder as Serializable)
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@WorkOrderDetailActivity as Activity,
            tvName,
            "workOrderName"
        ).toBundle()
        startActivity(intent, bundle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val SELECTED_WORK_ORDER = "selected_work_order"
        const val IS_FORCE_REFRESH = "is_force_refresh"
        const val SELECTED_WORK_ORDER_RECOMMENDATIONS = "selected_work_order_recommendations"
    }
}
