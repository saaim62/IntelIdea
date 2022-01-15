package com.example.fypintelidea.features.workOrder

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Asset
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.utils.AssetsUtility
import com.example.fypintelidea.core.views.ConstantsWOType
import com.example.fypintelidea.features.activities.CategoriesActivity
import com.example.fypintelidea.features.activities.TagsActivity
import com.example.fypintelidea.features.activities.UsersActivity
import com.example.fypintelidea.features.custom_status.StatusesActivity
import com.example.fypintelidea.core.ConnectavoBaseFragment
import com.example.fypintelidea.core.ConstantsCategories
import com.example.fypintelidea.core.ConstantsStatuses
import com.example.fypintelidea.core.listeners.EndlessRecyclerViewScrollListener
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import com.example.fypintelidea.core.utils.Utils
import com.example.fypintelidea.features.activities.DateRangePickerActivity
import com.example.fypintelidea.features.filters.FiltersWorkOrderActivity
import com.example.fypintelidea.features.workOrder.workOrderDetails.WorkOrderDetailActivity
import com.example.fypintelidea.features.search.SearchActivity
import com.example.fypintelidea.features.workOrder.workOrderCompletion.WorkOrderCompleteActivity
import kotlinx.android.synthetic.main.fragment_workorders.*
import org.koin.android.ext.android.inject
import java.io.Serializable
import java.util.*


class WorkOrdersFragmentTab : ConnectavoBaseFragment() {
    private val workOrdersFragmentTabViewModel: WorkOrdersFragmentTabViewModel by inject()
    private val sessionManager: SessionManager by inject()
    private var workOrders = ArrayList<Workorder>()
    private var wos: Workorder? = null
    private val totalAssets = ArrayList<Asset>()
    private var adapter: WorkOrderAdapter? = null
    private var mContext: Context? = null
    private var selectedMyWorkOrders: String? = null
    private var selectedDueDateGreaterThanEqualToToday: String? = null
    private var selectedDueDateLessThanEqualToToday: String? = null
    private var selectedDueDateLessThanEqualToOverDue: String? = null
    private var selectedIncludeChildrenAssets: String? = null
    private var selectedIncludeChklistAssets: String? = null
    private var sortby: String? = null
    private val selectedTypeList = ArrayList<String>()
    private val selectedStatusList = ArrayList<String>()
    private val selectedPriorityList = ArrayList<String>()
    private val selectedUsersList = ArrayList<String>()
    private val selectedTeamsList = ArrayList<String>()
    private val selectedAssetsList = ArrayList<String>()
    private val selectedCategoryList = ArrayList<String>()
    private val selectedTagsList = ArrayList<String>()
    private var selectedDueDateGreaterThanEqualToDatePicker: String? = null
    private var selectedDueDateLessThanEqualToDatePicker: String? = null
    private val statuses = ArrayList<String>()
    private val categories = ArrayList<String>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workorders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sortby = "due_date desc"
        clFilterHeader.setBackgroundColor(resources.getColor(R.color.md_white))

        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        mRecyclerView.layoutManager = linearLayoutManager
        adapter = WorkOrderAdapter(workOrders, totalAssets,

            { changeWOStatusToOpen ->
                changeWOStatusToOpen(
                    changeWOStatusToOpen
                )
            },
            { changeWOStatusToInProgress ->
                changeWOStatusToInProgress(
                    changeWOStatusToInProgress
                )
            },
            { changeWOStatusToPauseState ->
                changeWOStatusToPause(
                    changeWOStatusToPauseState
                )
            },
            { changeWOStatusToDone ->
                changeWOStatusToDone(
                    changeWOStatusToDone
                )
            }
        )


        mRecyclerView?.adapter = adapter
        // Retain an instance so that you can call `resetState()` for fresh searches
        val scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: androidx.recyclerview.widget.RecyclerView
            ) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                progressBarWorkOrder?.visibility = View.VISIBLE
                workOrdersFragmentTabViewModel.fetchMore(
                    selectedMyWorkOrders,
                    selectedDueDateGreaterThanEqualToToday,
                    selectedDueDateLessThanEqualToToday,
                    selectedDueDateLessThanEqualToOverDue,
                    selectedTypeList,
                    statuses,
                    selectedPriorityList,
                    selectedUsersList,
                    selectedTeamsList,
                    selectedAssetsList,
                    selectedIncludeChildrenAssets,
                    selectedIncludeChklistAssets,
                    categories,
                    selectedTagsList,
                    selectedDueDateGreaterThanEqualToDatePicker,
                    selectedDueDateLessThanEqualToDatePicker,
                    sortby
                )
            }
        }
        mRecyclerView?.addOnScrollListener(scrollListener)
        defaultItems()
        addSortBy()
        setObservers()

        MyDateTimeStamp.setFrescoImage(
            profileImageView,
            "https:" + sessionManager.getString(SessionManager.KEY_LOGIN_IMAGEPATH)
        )

        ivSearch.setOnClickListener {
            activity?.startActivity(Intent(context, SearchActivity::class.java))
            activity?.overridePendingTransition(
                R.anim.slide_up,
                R.anim.slide_down
            )
        }

        bFilters?.setOnClickListener {
            val i = Intent(mContext, FiltersWorkOrderActivity::class.java)
            i.putExtra(FiltersWorkOrderActivity.KEY_SELECTED_MY_WORDORDER, selectedMyWorkOrders)
            i.putExtra(
                FiltersWorkOrderActivity.KEY_SELECTED_DUE_DATE_GREATER_THAN_EQUAL_TO_TODAY,
                selectedDueDateGreaterThanEqualToToday
            )
            i.putExtra(
                FiltersWorkOrderActivity.KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_TODAY,
                selectedDueDateLessThanEqualToToday
            )
            i.putExtra(
                FiltersWorkOrderActivity.KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_OVERDUE,
                selectedDueDateLessThanEqualToOverDue
            )
            i.putStringArrayListExtra(
                FiltersWorkOrderActivity.KEY_SELECTED_TYPE_LIST,
                selectedTypeList
            )
            i.putStringArrayListExtra(
                FiltersWorkOrderActivity.KEY_SELECTED_PRIORITY_LIST,
                selectedPriorityList
            )
            i.putStringArrayListExtra(
                UsersActivity.KEY_SELECTED_USERS,
                selectedUsersList
            )
//            i.putStringArrayListExtra(
//                TeamsActivity.KEY_SELECTED_TEAMS,
//                selectedTeamsList
//            )
//            i.putStringArrayListExtra(
//                AssetsMultiSelectActivity.KEY_SELECTED_ASSETS,
//                selectedAssetsList
//            )
            i.putExtra(
                FiltersWorkOrderActivity.KEY_SELECTED_INCLUDE_CHILDREN_ASSETS,
                selectedIncludeChildrenAssets
            )
            i.putExtra(
                FiltersWorkOrderActivity.KEY_SELECTED_INCLUDE_CHKLIST_ASSETS,
                selectedIncludeChklistAssets
            )
            i.putStringArrayListExtra(
                StatusesActivity.KEY_SELECTED_STATUSES,
                selectedStatusList
            )
            i.putStringArrayListExtra(
                CategoriesActivity.KEY_SELECTED_CATEGORIES,
                selectedCategoryList
            )
            i.putStringArrayListExtra(
                TagsActivity.KEY_SELECTED_TAGS,
                selectedTagsList
            )
            i.putExtra(
                DateRangePickerActivity.KEY_SELECTED_DUEDATE_GREATER_THAN_EQUAL_TO_DATE_PICKER,
                selectedDueDateGreaterThanEqualToDatePicker
            )
            i.putExtra(
                DateRangePickerActivity.KEY_SELECTED_DUEDATE_LESS_THAN_EQUAL_TO_DATE_PICKER,
                selectedDueDateLessThanEqualToDatePicker
            )
            startActivityForResult(i, FILTERS_REQUEST_CODE)
        }

        profileImageView?.setOnClickListener {
            goToProfile()
        }

        mySwipeRefreshLayout?.setOnRefreshListener {
            loadDataFromApi()
            mySwipeRefreshLayout?.isRefreshing = false
        }
        fetchLowAssetsFromServer()
        loadDataFromApi()
    }

    private fun defaultItems() {
        sortby = "due_date desc"
        textViewSortValue?.text = ConstantsWOType.getDialogFriendlyName(
            activity as Context,
            "due_date_dialog"
        )
    }

    private fun addSortBy() {
        //  Adds the dialog with radio button
        val itemsKeys = arrayOf<CharSequence>(
            "due_date desc",
            "assigned_to desc",
            "name desc"
        )

        val itemNames = arrayOf<CharSequence>(
            ConstantsWOType.getDialogFriendlyName(
                activity as Context,
                "due_date_dialog"
            ),
            ConstantsWOType.getDialogFriendlyName(
                activity as Context,
                "assignee"
            ),
            ConstantsWOType.getDialogFriendlyName(
                activity as Context,
                "name"
            )
        )
        var selectedItem: String? = null
        var selectedIndex: Int? = null
        var selectedName: String? = null
        textViewSortValue?.setOnClickListener {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
            builder.setSingleChoiceItems(itemNames, 0) { _, i ->
                selectedItem = itemsKeys[i] as String
                selectedName = itemNames[i] as String
                selectedIndex = i
            }
            builder.setTitle(R.string.sort_by)
            builder.setPositiveButton(R.string.apply) { _, _ ->
                if (selectedItem == null) {
                    sortby = "due_date desc"
                    textViewSortValue?.text = ConstantsWOType.getDialogFriendlyName(
                        activity as Context,
                        "due_date_dialog"
                    )
                    loadDataFromApi()
                } else {
                    this.sortby = selectedItem
                    textViewSortValue?.text = selectedName.toString()
                    loadDataFromApi()
                }
            }
            builder.setNegativeButton(
                R.string.cancel
            ) { dialog, _ ->
                dialog.cancel()
            }
            activity?.let { activity ->
                val dialog = builder.create()
                dialog.show()
                selectedIndex?.let { it1 -> dialog.listView.setItemChecked(it1, true) }
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(activity, R.color.md_grey_700))
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(activity, R.color.md_light_blue_500))
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).textSize = 16.0F
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).textSize = 16.0F
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILTERS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                var count = 0
                this.selectedMyWorkOrders =
                    data.getStringExtra(FiltersWorkOrderActivity.KEY_SELECTED_MY_WORDORDER)
                if (this.selectedMyWorkOrders != null) {
                    count++
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedDueDateGreaterThanEqualToToday =
                    data.getStringExtra(FiltersWorkOrderActivity.KEY_SELECTED_DUE_DATE_GREATER_THAN_EQUAL_TO_TODAY)
                if (this.selectedDueDateGreaterThanEqualToToday != null) {
                    count++
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedDueDateLessThanEqualToToday =
                    data.getStringExtra(FiltersWorkOrderActivity.KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_TODAY)
//                if (this.selectedDueDateLessThanEqualToToday != null) {
//                    count++
//                }

                this.selectedDueDateLessThanEqualToOverDue =
                    data.getStringExtra(FiltersWorkOrderActivity.KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_OVERDUE)
                if (this.selectedDueDateLessThanEqualToOverDue != null) {
                    count++
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedTypeList.clear()
                val selectedTypeList =
                    data.getStringArrayListExtra(FiltersWorkOrderActivity.KEY_SELECTED_TYPE_LIST)
                if (selectedTypeList != null) {
                    this.selectedTypeList.addAll(selectedTypeList)
                    if (selectedTypeList.size > 0) {
                        count += selectedTypeList.size
                    }
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedStatusList.clear()
                val selectedStatusList =
                    data.getStringArrayListExtra(StatusesActivity.KEY_SELECTED_STATUSES)
                if (selectedStatusList != null) {
                    this.selectedStatusList.addAll(selectedStatusList)
                    if (selectedStatusList.size > 0) {
                        count += selectedStatusList.size
                    }
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedPriorityList.clear()
                val selectedPriorityList =
                    data.getStringArrayListExtra(FiltersWorkOrderActivity.KEY_SELECTED_PRIORITY_LIST)
                if (selectedPriorityList != null) {
                    this.selectedPriorityList.addAll(selectedPriorityList)
                    if (selectedPriorityList.size > 0) {
                        count += selectedPriorityList.size
                    }
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedUsersList.clear()
                val selectedUsersList =
                    data.getStringArrayListExtra(UsersActivity.KEY_SELECTED_USERS)
                if (selectedUsersList != null) {
                    this.selectedUsersList.addAll(selectedUsersList)
                    if (selectedUsersList.size > 0) {
                        count += selectedUsersList.size
                    }
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

//                this.selectedTeamsList.clear()
//                val selectedTeamsList =
//                    data.getStringArrayListExtra(TeamsActivity.KEY_SELECTED_TEAMS)
//                if (selectedTeamsList != null) {
//                    this.selectedTeamsList.addAll(selectedTeamsList)
//                    if (selectedTeamsList.size > 0) {
//                        count += selectedTeamsList.size
//                    }
//                } else {
//                    workOrders.clear()
//                    tvWorkorderCount?.text =
//                        "0 " + activity?.resources?.getString(R.string.ideas)
//                }

//                this.selectedAssetsList.clear()
//                val selectedAssetsList =
//                    data.getStringArrayListExtra(AssetsMultiSelectActivity.KEY_SELECTED_ASSETS)
//                if (selectedAssetsList != null) {
//                    this.selectedAssetsList.addAll(selectedAssetsList)
//                    if (selectedAssetsList.size > 0) {
//                        count += selectedAssetsList.size
//                    }
//                } else {
//                    workOrders.clear()
//                    tvWorkorderCount?.text =
//                        "0 " + activity?.resources?.getString(R.string.ideas)
//                }

                this.selectedIncludeChildrenAssets =
                    data.getStringExtra(FiltersWorkOrderActivity.KEY_SELECTED_INCLUDE_CHILDREN_ASSETS)
                if (this.selectedIncludeChildrenAssets != null) {
                    count++
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedIncludeChklistAssets =
                    data.getStringExtra(FiltersWorkOrderActivity.KEY_SELECTED_INCLUDE_CHKLIST_ASSETS)
                if (this.selectedIncludeChklistAssets != null) {
                    count++
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedCategoryList.clear()
                val selectedCategoryList =
                    data.getStringArrayListExtra(CategoriesActivity.KEY_SELECTED_CATEGORIES)
                if (selectedCategoryList != null) {
                    this.selectedCategoryList.addAll(selectedCategoryList)
                    if (selectedCategoryList.size > 0) {
                        count += selectedCategoryList.size
                    }
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedTagsList.clear()
                val selectedTagsList = data.getStringArrayListExtra(TagsActivity.KEY_SELECTED_TAGS)
                if (selectedTagsList != null) {
                    this.selectedTagsList.addAll(selectedTagsList)
                    if (selectedTagsList.size > 0) {
                        count += selectedTagsList.size
                    }
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }

                this.selectedDueDateGreaterThanEqualToDatePicker =
                    data.getStringExtra(DateRangePickerActivity.KEY_SELECTED_DUEDATE_GREATER_THAN_EQUAL_TO_DATE_PICKER)
                if (this.selectedDueDateGreaterThanEqualToDatePicker != null) {
                    count++
                } else {
                    workOrders.clear()
                    tvWorkorderCount?.text =
                        "0 " + activity?.resources?.getString(R.string.ideas)
                }
                this.selectedDueDateLessThanEqualToDatePicker =
                    data.getStringExtra(DateRangePickerActivity.KEY_SELECTED_DUEDATE_LESS_THAN_EQUAL_TO_DATE_PICKER)
//                if (this.selectedDueDateLessThanEqualToDatePicker != null) {
//                    count++
//                } /

                tvFilterCount.text = count.toString()
                if (count == 0) {
                    tvFilterCount.text = "0"
//                    tvFilterCount.setBackgroundDrawable(null)
                } else if (count == 3) {
                    if (this.selectedStatusList.size > 0) {
                        if (this.selectedStatusList.contains(Workorder.WORKORDER_STATUS_OPEN) && this.selectedStatusList.contains(
                                Workorder.WORKORDER_STATUS_INPROGRESS
                            ) && this.selectedStatusList.contains(Workorder.WORKORDER_STATUS_PAUSED)
                        ) {
//                            tvFilterCount.setBackgroundDrawable(null)
                        }
                    }
                }
                loadDataFromApi()
                mySwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun loadDataFromApi() {
        activity?.let { activity ->
            // Set up progress before call
//            ProgressDialogProvider.show(activity)

            if (!statuses.isNullOrEmpty()) {
                statuses.clear()
            }
            for (oneStatus in selectedStatusList) {
                statuses.add(ConstantsStatuses.getAPIName(activity, oneStatus))
            }

            if (!categories.isNullOrEmpty()) {
                categories.clear()
            }
            // category conversion
            for (oneCategory in selectedCategoryList) {
                categories.add(ConstantsCategories.getAPIName(activity, oneCategory))
            }

            adapter?.notifyDataSetChanged()
            progressBarWorkOrder?.visibility = View.VISIBLE
            workOrdersFragmentTabViewModel.fetchNewData(
                selectedMyWorkOrders,
                selectedDueDateGreaterThanEqualToToday,
                selectedDueDateLessThanEqualToToday,
                selectedDueDateLessThanEqualToOverDue,
                selectedTypeList,
                statuses,
                selectedPriorityList,
                selectedUsersList,
                selectedTeamsList,
                selectedAssetsList,
                selectedIncludeChildrenAssets,
                selectedIncludeChklistAssets,
                categories,
                selectedTagsList,
                selectedDueDateGreaterThanEqualToDatePicker,
                selectedDueDateLessThanEqualToDatePicker,
                sortby
            )
        }
    }

    private fun changeWOStatusToOpen(wo: Workorder) {
        progressBarWorkOrder?.visibility = View.VISIBLE
        wos = wo
        workOrdersFragmentTabViewModel.updateWorkOrderToOpenState(wo)
    }

    private fun changeWOStatusToInProgress(wo: Workorder) {
        progressBarWorkOrder?.visibility = View.VISIBLE
        wos = wo
        workOrdersFragmentTabViewModel.updateWorkOrderToInProgress(wo)
    }

    private fun changeWOStatusToPause(wo: Workorder) {
        progressBarWorkOrder?.visibility = View.VISIBLE
        wos = wo
        workOrdersFragmentTabViewModel.updateWorkOrderToPauseState(wo)
    }

    private fun changeWOStatusToDone(wo: Workorder) {
        progressBarWorkOrder?.visibility = View.VISIBLE
        wos = wo
        val mapChecklist = Utils.submitChklistDataToServer(wo)
        wo.chklist_id?.let {
            workOrdersFragmentTabViewModel.submitChklistMultipart(
                it,
                mapChecklist
            )
        }
    }

    private fun fetchLowAssetsFromServer() {
        val assets = AssetsUtility.assetListHigh
        totalAssets.clear()
        totalAssets.addAll(assets)
        mRecyclerView?.adapter?.notifyDataSetChanged()
    }

    private fun setObservers() {
        activity?.let { activity ->
            workOrdersFragmentTabViewModel.wosObservable.observe(activity) {
                val apiResponse = it.value
                progressBarWorkOrder?.visibility = View.GONE
                wos?.status = apiResponse.status
                Toast.makeText(
                    activity,
                    activity.resources?.getString(R.string.successfully_updated),
                    Toast.LENGTH_SHORT
                ).show()
            }

            workOrdersFragmentTabViewModel.getAllWorkOrdersObservable.observe(activity) {
                if (it.error != null) {
                    Toast.makeText(activity, "didn't get workorders", Toast.LENGTH_SHORT).show()
                } else {
                    val apiResponse = it.value
                    progressBarWorkOrder?.visibility = View.GONE
                    tvWorkorderCount?.text =
                        workOrdersFragmentTabViewModel.totalWorkOrders + (" " + activity?.resources?.getString(
                            R.string.ideas
                        ))
                    adapter?.updateList(apiResponse)
                }
            }
            workOrdersFragmentTabViewModel.submitCheckListMultiPartsObservable.observe(activity) {
                val apiResponse = it.value
                if (apiResponse is Workorder) {
                    progressBarWorkOrder?.visibility = View.GONE
                    apiResponse.chklistSections?.forEachIndexed { _, oneSectionTemp ->
                        oneSectionTemp.chklistItem?.forEachIndexed { _, oneChklistItemTemp ->
                            wos?.chklistSections?.forEachIndexed { _, oneSection ->
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
                        progressBarWorkOrder?.visibility = View.GONE
                        // check if a required custom field is not filled.
                    }
                }
            }
        }
    }

    companion object {
        private const val FILTERS_REQUEST_CODE = 600
    }
}

