package com.example.fypintelidea.features.filters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import com.example.fypintelidea.features.custom_status.StatusesActivity
import com.example.fypintelidea.features.activities.CategoriesActivity
import com.example.fypintelidea.features.activities.DateRangePickerActivity
import com.example.fypintelidea.features.activities.TagsActivity
import com.example.fypintelidea.features.activities.UsersActivity
import kotlinx.android.synthetic.main.activity_workorder_filters.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class FiltersWorkOrderActivity : AppCompatActivity() {
    val sessionManager: SessionManager by inject()
    internal var mContext: Context = this

    //Filters
    private var selectedMyWorkOrders: String? = null
    private var selectedDueDateGreaterThanEqualToToday: String? = null
    private var selectedDueDateLessThanEqualToToday: String? = null
    private var selectedDueDateLessThanEqualToOverDue: String? = null
    private var selectedTypeList: MutableList<String> = ArrayList()
    private var selectedPriorityList: MutableList<String> = ArrayList()
    private var selectedUsersList: MutableList<String>? = ArrayList()
    private var selectedTeamsList: MutableList<String>? = ArrayList()

    private var selectedAssetsList: MutableList<String>? = ArrayList()
    private var selectedStatusesList: MutableList<String>? = ArrayList()
    private var selectedCategoriesList: MutableList<String>? = ArrayList()
    private var selectedTagsList: MutableList<String> = ArrayList()
    private var selectedDueDateGreaterThanEqualToDatePicker: String? = null
    private var selectedDueDateLessThanEqualToDatePicker: String? = null
    private var selectedIncludeChildrenAssets: String? = null
    private var selectedIncludeChklistAssets: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workorder_filters)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = resources.getString(R.string.filters)
            actionBar.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.elevation = 0f
        }
        val i = intent
        val bundle = i.extras
        if (bundle != null) {

            //set initial state of button "My Work Order"
            val selectedMyWorkorderTemp = bundle.getString(KEY_SELECTED_MY_WORDORDER)
            if (selectedMyWorkorderTemp != null) {
                bMyWorkorders?.isSelected = true
                bMyWorkorders?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_myworkorder_button_selected
                )
                bMyWorkorders?.setTextColor(Color.WHITE)
                selectedMyWorkOrders = selectedMyWorkorderTemp
            }

            //set initial state of button "OverDue"
            val selectedDueDateGreaterThanEqualToTodayTemp =
                bundle.getString(KEY_SELECTED_DUE_DATE_GREATER_THAN_EQUAL_TO_TODAY)
            val selectedDueDateLessThanEqualToTodayTemp =
                bundle.getString(KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_TODAY)
            if (selectedDueDateGreaterThanEqualToTodayTemp != null && selectedDueDateLessThanEqualToTodayTemp != null) {
                bToday?.isSelected = true
                bToday?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_today_button_selected)
                bToday?.setTextColor(Color.WHITE)
                selectedDueDateGreaterThanEqualToToday = selectedDueDateGreaterThanEqualToTodayTemp
                selectedDueDateLessThanEqualToToday = selectedDueDateLessThanEqualToTodayTemp
            }

            //set initial state of button "OverDue"
            val selectedDueDateLessThanEqualToOverDueTemp =
                bundle.getString(KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_OVERDUE)
            if (selectedDueDateLessThanEqualToOverDueTemp != null) {
                bOverDue?.isSelected = true
                bOverDue?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_overdue_button_selected)
                bOverDue?.setTextColor(Color.WHITE)
                selectedDueDateLessThanEqualToOverDue = selectedDueDateLessThanEqualToOverDueTemp
            }

            //set initial state of type buttons
            val selectedTypeListTemp = bundle.getStringArrayList(KEY_SELECTED_TYPE_LIST)
            if (selectedTypeListTemp != null && selectedTypeListTemp.size > 0) {
                selectedTypeList = selectedTypeListTemp
                if (selectedTypeList.contains(Workorder.WORKORDER_TYPE_UNPLANNED)) {
                    bUnplanned?.isSelected = true
                    bUnplanned?.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.selector_unplanned_button_selected
                    )
                    bUnplanned?.setTextColor(Color.WHITE)
                }
                if (selectedTypeList.contains(Workorder.WORKORDER_TYPE_PLANNED)) {
                    bPlanned?.isSelected = true
                    bPlanned?.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.selector_planned_button_selected
                    )
                    bPlanned?.setTextColor(Color.WHITE)
                }
                if (selectedTypeList.contains(Workorder.WORKORDER_TYPE_PREDICTIVE)) {
                    bPredictive?.isSelected = true
                    bPredictive?.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.selector_predictive_button_selected
                    )
                    bPredictive?.setTextColor(Color.WHITE)
                }
            }

            //set initial state of priority buttons
            val selectedPriorityListTemp = bundle.getStringArrayList(KEY_SELECTED_PRIORITY_LIST)
            if (selectedPriorityListTemp != null && selectedPriorityListTemp.size > 0) {
                selectedPriorityList = selectedPriorityListTemp
                if (selectedPriorityList.contains(Workorder.WORKORDER_PRIORITY_LOW)) {
                    bLow?.isSelected = true
                    bLow?.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.selector_prioritylow_button_selected
                    )
                    bLow?.setTextColor(Color.WHITE)
                }
                if (selectedPriorityList.contains(Workorder.WORKORDER_PRIORITY_MEDIUM)) {
                    bMedium?.isSelected = true
                    bMedium?.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.selector_prioritymedium_button_selected
                    )
                    bMedium?.setTextColor(Color.WHITE)
                }
                if (selectedPriorityList.contains(Workorder.WORKORDER_PRIORITY_HIGH)) {
                    bHigh?.isSelected = true
                    bHigh?.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.selector_priorityhigh_button_selected
                    )
                    bHigh?.setTextColor(Color.WHITE)
                }
            }

            //set initial state of employees
            val selectedUsersListTemp = bundle.getStringArrayList(UsersActivity.KEY_SELECTED_USERS)
            if (selectedUsersListTemp != null && selectedUsersListTemp.size > 0) {
                selectedUsersList = selectedUsersListTemp
                tvEmploySelected?.text =
                    String.format("%d", selectedUsersList?.size) + " selected"
            } else {
                tvEmploySelected?.text = null
                tvEmploySelected?.setHint(R.string.select_one_or_more)
            }

            //set initial state of teams
//            val selectedTeamsListTemp = bundle.getStringArrayList(TeamsActivity.KEY_SELECTED_TEAMS)
//            if (selectedTeamsListTemp != null && selectedTeamsListTemp.size > 0) {
//                selectedTeamsList = selectedTeamsListTemp
//                tvTeamSelected?.text = String.format("%d", selectedTeamsList?.size) + " selected"
//            } else {
//                tvTeamSelected?.text = null
//                tvTeamSelected?.setHint(R.string.select_one_or_more)
//            }

            //set initial state of assets
//            val selectedAssetsListTemp =
//                bundle.getStringArrayList(AssetsMultiSelectActivity.KEY_SELECTED_ASSETS)
//            if (selectedAssetsListTemp != null && selectedAssetsListTemp.size > 0) {
//                selectedAssetsList = selectedAssetsListTemp
//                tvAssetSelected?.text = String.format("%d", selectedAssetsList?.size) + " selected"
//            } else {
//                tvAssetSelected?.text = null
//                tvAssetSelected?.setHint(R.string.select_one_or_more)
//            }

            //set initial state include children assets
            val selectedIncludeChildrenAssetsTemp =
                bundle.getString(KEY_SELECTED_INCLUDE_CHILDREN_ASSETS)
            if (selectedIncludeChildrenAssetsTemp != null && !selectedIncludeChildrenAssetsTemp.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                this.selectedIncludeChildrenAssets = selectedIncludeChildrenAssetsTemp
                //set switch checked
                swIncludeChildrenAssets?.isChecked = true
            }

            //set initial state include chklist assets
            val selectedIncludeChklistAssetsTemp =
                bundle.getString(KEY_SELECTED_INCLUDE_CHKLIST_ASSETS)
            if (selectedIncludeChklistAssetsTemp != null && !selectedIncludeChklistAssetsTemp.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                this.selectedIncludeChklistAssets = selectedIncludeChklistAssetsTemp
                //set switch checked
                swIncludeChklistAssets?.isChecked = true
            }

            //set initial state of statuses
            val selectedStatusesListTemp =
                bundle.getStringArrayList(StatusesActivity.KEY_SELECTED_STATUSES)
            if (selectedStatusesListTemp != null && selectedStatusesListTemp.size > 0) {
                selectedStatusesList = selectedStatusesListTemp
                tvStatusSelected?.text =
                    String.format("%d", selectedStatusesList?.size) + " selected"
            } else {
                tvStatusSelected?.text = null
                tvStatusSelected?.setHint(R.string.select_one_or_more)
            }

            //set initial state of categories
            val selectedCategoriesListTemp =
                bundle.getStringArrayList(CategoriesActivity.KEY_SELECTED_CATEGORIES)
            if (selectedCategoriesListTemp != null && selectedCategoriesListTemp.size > 0) {
                selectedCategoriesList = selectedCategoriesListTemp
                tvCategorySelected?.text =
                    String.format("%d", selectedCategoriesList?.size) + " selected"
            } else {
                tvCategorySelected?.text = null
                tvCategorySelected?.setHint(R.string.select_one_or_more)
            }

            //set initial state of tags
            val selectedTagsListTemp = bundle.getStringArrayList(TagsActivity.KEY_SELECTED_TAGS)
            if (selectedTagsListTemp != null && selectedTagsListTemp.size > 0) {
                selectedTagsList = selectedTagsListTemp
                tvTagsSelected?.text = String.format("%d", selectedTagsList.size) + " selected"
            } else {
                tvTagsSelected?.text = null
                tvTagsSelected?.setHint(R.string.select_one_or_more)
            }
            //set initial state of date range picker
            val selectedDueDateGreaterThanEqualToDatePickerTemp =
                bundle.getString(DateRangePickerActivity.KEY_SELECTED_DUEDATE_GREATER_THAN_EQUAL_TO_DATE_PICKER)
            val selectedDueDateLessThanEqualToDatePickerTemp =
                bundle.getString(DateRangePickerActivity.KEY_SELECTED_DUEDATE_LESS_THAN_EQUAL_TO_DATE_PICKER)
            if (selectedDueDateGreaterThanEqualToDatePickerTemp != null && selectedDueDateLessThanEqualToDatePickerTemp != null) {
                selectedDueDateGreaterThanEqualToDatePicker =
                    selectedDueDateGreaterThanEqualToDatePickerTemp
                selectedDueDateLessThanEqualToDatePicker =
                    selectedDueDateLessThanEqualToDatePickerTemp
                tvDatesSelected?.text =
                    selectedDueDateGreaterThanEqualToDatePicker?.length?.minus(5)?.let {
                        selectedDueDateGreaterThanEqualToDatePicker?.substring(
                            0,
                            it
                        )
                    } + " - " + selectedDueDateLessThanEqualToDatePicker?.length?.minus(5)?.let {
                        selectedDueDateLessThanEqualToDatePicker?.substring(
                            0,
                            it
                        )
                    }
            }
        }

        bMyWorkorders?.setOnClickListener {
            bMyWorkorders?.isSelected = !bMyWorkorders?.isSelected!!
            if (bMyWorkorders?.isSelected == true) {
                bMyWorkorders?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_myworkorder_button_selected
                )
                bMyWorkorders?.setTextColor(Color.WHITE)
                selectedMyWorkOrders = sessionManager.getString(SessionManager.KEY_LOGIN_ID)
            } else {
                bMyWorkorders?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_myworkorder_button_normal
                )
                bMyWorkorders?.setTextColor(ContextCompat.getColor(this, R.color.Color_Default))
                selectedMyWorkOrders = null
            }
        }
        bToday?.setOnClickListener {
            bToday?.isSelected = !bToday?.isSelected!!
            if (bToday?.isSelected == true) {
                bToday?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_today_button_selected)
                bToday?.setTextColor(Color.WHITE)
                //concatenate 00:00 as time with current 'date'
                selectedDueDateGreaterThanEqualToToday =
                    MyDateTimeStamp.getDateFormattedStringFromMilliseconds(Calendar.getInstance().timeInMillis) + " 00:00"
                //concatenate 12:59 as time with current 'date'
                selectedDueDateLessThanEqualToToday =
                    MyDateTimeStamp.getDateFormattedStringFromMilliseconds(Calendar.getInstance().timeInMillis) + " 23:59"
            } else {
                bToday?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_today_button_normal)
                bToday?.setTextColor(ContextCompat.getColor(this, R.color.Color_Default))
                selectedDueDateGreaterThanEqualToToday = null
                selectedDueDateLessThanEqualToToday = null
            }
        }
        bOverDue?.setOnClickListener {
            bOverDue?.isSelected = !bOverDue?.isSelected!!
            if (bOverDue?.isSelected == true) {
                bOverDue?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_overdue_button_selected)
                bOverDue?.setTextColor(Color.WHITE)
                //concatenate 23:59 with current 'date'
                selectedDueDateLessThanEqualToOverDue =
                    MyDateTimeStamp.getDateFormattedStringFromMilliseconds(Calendar.getInstance().timeInMillis) + " 23:59"
            } else {
                bOverDue?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_overdue_button_normal)
                bOverDue?.setTextColor(ContextCompat.getColor(this, R.color.Color_Default))
                selectedDueDateLessThanEqualToOverDue = null
            }
        }
        bUnplanned?.setOnClickListener {
            bUnplanned?.isSelected = !bUnplanned?.isSelected!!
            if (bUnplanned?.isSelected == true) {
                bUnplanned?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_unplanned_button_selected
                )
                bUnplanned?.setTextColor(Color.WHITE)
                selectedTypeList.add(Workorder.WORKORDER_TYPE_UNPLANNED)
            } else {
                bUnplanned?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_unplanned_button_normal)
                bUnplanned?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedTypeList.remove(Workorder.WORKORDER_TYPE_UNPLANNED)
            }
        }
        bPlanned?.setOnClickListener {
            bPlanned?.isSelected = !bPlanned?.isSelected!!
            if (bPlanned?.isSelected == true) {
                bPlanned?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_planned_button_selected)
                bPlanned?.setTextColor(Color.WHITE)
                selectedTypeList.add(Workorder.WORKORDER_TYPE_PLANNED)
            } else {
                bPlanned?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_planned_button_normal)
                bPlanned?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedTypeList.remove(Workorder.WORKORDER_TYPE_PLANNED)
            }
        }
        bPredictive?.setOnClickListener {
            bPredictive?.isSelected = !bPredictive?.isSelected!!
            if (bPredictive?.isSelected == true) {
                bPredictive?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_predictive_button_selected
                )
                bPredictive?.setTextColor(Color.WHITE)
                selectedTypeList.add(Workorder.WORKORDER_TYPE_PREDICTIVE)
            } else {
                bPredictive?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_predictive_button_normal
                )
                bPredictive?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedTypeList.remove(Workorder.WORKORDER_TYPE_PREDICTIVE)
            }
        }


        swIncludeChildrenAssets?.setOnCheckedChangeListener { _, b ->
            selectedIncludeChildrenAssets = if (b) {
                "yes"
            } else {
                ""
            }
        }

        swIncludeChklistAssets?.setOnCheckedChangeListener { _, b ->
            selectedIncludeChklistAssets = if (b) {
                "yes"
            } else {
                ""
            }
        }
        bLow?.setOnClickListener {
            bLow?.isSelected = !bLow?.isSelected!!
            if (bLow?.isSelected == true) {
                bLow?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_prioritylow_button_selected
                )
                bLow?.setTextColor(Color.WHITE)
                selectedPriorityList.add(Workorder.WORKORDER_PRIORITY_LOW)
            } else {
                bLow?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_prioritylow_button_normal
                )
                bLow?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedPriorityList.remove(Workorder.WORKORDER_PRIORITY_LOW)
            }
        }
        bMedium?.setOnClickListener {
            bMedium?.isSelected = !bMedium?.isSelected!!
            if (bMedium?.isSelected == true) {
                bMedium?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_prioritymedium_button_selected
                )
                bMedium?.setTextColor(Color.WHITE)
                selectedPriorityList.add(Workorder.WORKORDER_PRIORITY_MEDIUM)
            } else {
                bMedium?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_prioritymedium_button_normal
                )
                bMedium?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedPriorityList.remove(Workorder.WORKORDER_PRIORITY_MEDIUM)
            }
        }
        bHigh?.setOnClickListener {
            bHigh?.isSelected = !bHigh?.isSelected!!
            if (bHigh?.isSelected == true) {
                bHigh?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_priorityhigh_button_selected
                )
                bHigh?.setTextColor(Color.WHITE)
                selectedPriorityList.add(Workorder.WORKORDER_PRIORITY_HIGH)
            } else {
                bHigh?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_priorityhigh_button_normal
                )
                bHigh?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedPriorityList.remove(Workorder.WORKORDER_PRIORITY_HIGH)
            }
        }
        clEmployee.setOnClickListener {
            val intentUsers = Intent(mContext, UsersActivity::class.java)
            intentUsers.putStringArrayListExtra(
                UsersActivity.KEY_SELECTED_USERS,
                selectedUsersList as ArrayList<String>?
            )
            startActivityForResult(intentUsers, USERS_REQUEST_CODE)

        }
//        clTeam.setOnClickListener {
//            val intentTeams = Intent(mContext, TeamsActivity::class.java)
//            intentTeams.putStringArrayListExtra(
//                TeamsActivity.KEY_SELECTED_TEAMS,
//                selectedTeamsList as ArrayList<String>?
//            )
//            startActivityForResult(intentTeams, TEAMS_REQUEST_CODE)
//
//        }
//        clAsset.setOnClickListener {
//            val intent = Intent(mContext, AssetsMultiSelectActivity::class.java)
//            intent.putExtra(
//                AssetsMultiSelectActivity.KEY_SELECTED_ASSETS,
//                selectedAssetsList as Serializable?
//            )
//            startActivityForResult(intent, ASSET_REQUEST_CODE)
//        }

        clStatus.setOnClickListener {
            val intentStatuses = Intent(mContext, StatusesActivity::class.java)
            intentStatuses.putStringArrayListExtra(
                StatusesActivity.KEY_SELECTED_STATUSES,
                selectedStatusesList as ArrayList<String>?
            )
            startActivityForResult(intentStatuses, STATUSES_REQUEST_CODE)

        }
        clCategory.setOnClickListener {
            val intentCategories = Intent(mContext, CategoriesActivity::class.java)
            intentCategories.putStringArrayListExtra(
                CategoriesActivity.KEY_SELECTED_CATEGORIES,
                selectedCategoriesList as ArrayList<String>?
            )
            startActivityForResult(intentCategories, CATEGORIES_REQUEST_CODE)

        }
        clTags.setOnClickListener {
            val intentTags = Intent(mContext, TagsActivity::class.java)
            intentTags.putStringArrayListExtra(
                TagsActivity.KEY_SELECTED_TAGS,
                selectedTagsList as ArrayList<String>
            )
            startActivityForResult(intentTags, TAGS_REQUEST_CODE)
        }
        clDate.setOnClickListener {
            val intentDateRange = Intent(mContext, DateRangePickerActivity::class.java)
            intentDateRange.putExtra(
                DateRangePickerActivity.KEY_SELECTED_DUEDATE_GREATER_THAN_EQUAL_TO_DATE_PICKER,
                selectedDueDateGreaterThanEqualToDatePicker
            )
            intentDateRange.putExtra(
                DateRangePickerActivity.KEY_SELECTED_DUEDATE_LESS_THAN_EQUAL_TO_DATE_PICKER,
                selectedDueDateLessThanEqualToDatePicker
            )
            startActivityForResult(intentDateRange, DATES_REQUEST_CODE)
        }

        bApplyFilters.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(KEY_SELECTED_MY_WORDORDER, this.selectedMyWorkOrders)
            returnIntent.putExtra(
                KEY_SELECTED_DUE_DATE_GREATER_THAN_EQUAL_TO_TODAY,
                this.selectedDueDateGreaterThanEqualToToday
            )
            returnIntent.putExtra(
                KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_TODAY,
                this.selectedDueDateLessThanEqualToToday
            )
            returnIntent.putExtra(
                KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_OVERDUE,
                this.selectedDueDateLessThanEqualToOverDue
            )
            returnIntent.putExtra(
                KEY_SELECTED_TYPE_LIST,
                this.selectedTypeList as ArrayList<String>
            )
            returnIntent.putStringArrayListExtra(
                KEY_SELECTED_PRIORITY_LIST,
                this.selectedPriorityList as ArrayList<String>
            )
            returnIntent.putStringArrayListExtra(
                UsersActivity.KEY_SELECTED_USERS,
                this.selectedUsersList as ArrayList<String>?
            )
//            returnIntent.putStringArrayListExtra(
//                TeamsActivity.KEY_SELECTED_TEAMS,
//                this.selectedTeamsList as ArrayList<String>?
//            )
//            returnIntent.putStringArrayListExtra(
//                AssetsMultiSelectActivity.KEY_SELECTED_ASSETS,
//                this.selectedAssetsList as ArrayList<String>?
//            )
            returnIntent.putExtra(
                KEY_SELECTED_INCLUDE_CHILDREN_ASSETS,
                this.selectedIncludeChildrenAssets
            )
            returnIntent.putExtra(
                KEY_SELECTED_INCLUDE_CHKLIST_ASSETS,
                this.selectedIncludeChklistAssets
            )
            returnIntent.putStringArrayListExtra(
                StatusesActivity.KEY_SELECTED_STATUSES,
                this.selectedStatusesList as ArrayList<String>?
            )
            returnIntent.putStringArrayListExtra(
                CategoriesActivity.KEY_SELECTED_CATEGORIES,
                this.selectedCategoriesList as ArrayList<String>?
            )
            returnIntent.putStringArrayListExtra(
                TagsActivity.KEY_SELECTED_TAGS,
                this.selectedTagsList as ArrayList<String>
            )
            returnIntent.putExtra(
                DateRangePickerActivity.KEY_SELECTED_DUEDATE_GREATER_THAN_EQUAL_TO_DATE_PICKER,
                this.selectedDueDateGreaterThanEqualToDatePicker
            )
            returnIntent.putExtra(
                DateRangePickerActivity.KEY_SELECTED_DUEDATE_LESS_THAN_EQUAL_TO_DATE_PICKER,
                this.selectedDueDateLessThanEqualToDatePicker
            )
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_clear_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_clear -> {

                bMyWorkorders?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_myworkorder_button_normal
                )
                bMyWorkorders?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedMyWorkOrders = null

                bToday?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_today_button_normal)
                bToday?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedDueDateGreaterThanEqualToToday = null
                selectedDueDateLessThanEqualToToday = null

                bOverDue?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_overdue_button_normal)
                bOverDue?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedDueDateLessThanEqualToOverDue = null

                bUnplanned?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_unplanned_button_normal)
                bUnplanned?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedTypeList.remove(Workorder.WORKORDER_TYPE_UNPLANNED)

                bPlanned?.background =
                    ContextCompat.getDrawable(this, R.drawable.selector_planned_button_normal)
                bPlanned?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedTypeList.remove(Workorder.WORKORDER_TYPE_PLANNED)

                bPredictive?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_predictive_button_normal
                )
                bPredictive?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedTypeList.remove(Workorder.WORKORDER_TYPE_PREDICTIVE)

                selectedUsersList?.clear()
                tvEmploySelected?.text = null
                tvEmploySelected?.setHint(R.string.select_one_or_more)

                selectedTeamsList?.clear()
                tvTeamSelected?.text = null
                tvTeamSelected?.setHint(R.string.select_one_or_more)

                selectedAssetsList?.clear()
                tvAssetSelected?.text = null
                tvAssetSelected?.setHint(R.string.select_one_or_more)

                swIncludeChildrenAssets?.isChecked = false
                selectedIncludeChildrenAssets = null

                swIncludeChklistAssets?.isChecked = false
                selectedIncludeChklistAssets = null

                bLow?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_prioritylow_button_normal
                )
                bLow?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedPriorityList.remove(Workorder.WORKORDER_PRIORITY_LOW)

                bMedium?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_prioritymedium_button_normal
                )
                bMedium?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedPriorityList.remove(Workorder.WORKORDER_PRIORITY_MEDIUM)

                bHigh?.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.selector_priorityhigh_button_normal
                )
                bHigh?.setTextColor(ContextCompat.getColor(this, R.color.md_dark_green))
                selectedPriorityList.remove(Workorder.WORKORDER_PRIORITY_HIGH)

                selectedStatusesList?.clear()
                tvStatusSelected?.text = resources.getString(R.string._3_selected)

                selectedCategoriesList?.clear()
                tvCategorySelected?.text = null
                tvCategorySelected?.setHint(R.string.select_one_or_more)

                selectedTagsList.clear()
                tvTagsSelected?.text = null
                tvTagsSelected?.setHint(R.string.select_one_or_more)

                selectedDueDateGreaterThanEqualToDatePicker = null
                selectedDueDateLessThanEqualToDatePicker = null
                tvDatesSelected?.setHint(R.string.select_a_range)

                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ASSET_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK && data != null) {
                if (this.selectedAssetsList != null) {
                    this.selectedAssetsList?.clear()
                }
//                val selectedAssets =
//                    data.getStringArrayListExtra(AssetsMultiSelectActivity.KEY_SELECTED_ASSETS) as ArrayList<String>
//                this.selectedAssetsList = selectedAssets
//                if (!this.selectedAssetsList.isNullOrEmpty()) {
//                    tvAssetSelected?.text =
//                        String.format("%d", selectedAssetsList?.size) + " selected"
//                } else {
//                    tvAssetSelected?.text = null
//                    tvAssetSelected?.setHint(R.string.select_one_or_more)
//                }
            }
            USERS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedUsers =
                        data.getSerializableExtra(UsersActivity.KEY_SELECTED_USERS) as? ArrayList<String>?

                    if (this.selectedUsersList != null) {
                        this.selectedUsersList?.clear()
                    }
                    this.selectedUsersList = selectedUsers
                    if (!this.selectedUsersList.isNullOrEmpty()) {
                        tvEmploySelected?.text = String.format(
                            "%d",
                            selectedUsersList?.size
                        ) + " " + getString(R.string.selected)
                    } else {
                        tvEmploySelected?.text = null
                        tvEmploySelected?.setHint(R.string.select_one_or_more)
                    }
                }
            }
            TEAMS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
//                    val selectedTeams =
//                        data.getSerializableExtra(TeamsActivity.KEY_SELECTED_TEAMS) as? ArrayList<String>?
//                    if (this.selectedTeamsList != null) {
//                        this.selectedTeamsList?.clear()
//                    }
//                    this.selectedTeamsList = selectedTeams
//                    if (!this.selectedTeamsList.isNullOrEmpty()) {
//                        tvTeamSelected?.text = String.format(
//                            "%d",
//                            selectedTeamsList?.size
//                        ) + " " + getString(R.string.selected)
//                    } else {
//                        tvTeamSelected?.text = null
//                        tvTeamSelected?.setHint(R.string.select_one_or_more)
//                    }
                }
            }
            STATUSES_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedStatuses =
                        data.getStringArrayListExtra(StatusesActivity.KEY_SELECTED_STATUSES)
                    if (this.selectedStatusesList != null) {
                        this.selectedStatusesList?.clear()
                    }
                    this.selectedStatusesList = selectedStatuses
                    if (!this.selectedStatusesList.isNullOrEmpty()) {
                        tvStatusSelected?.text = String.format(
                            "%d",
                            selectedStatusesList?.size
                        ) + " " + getString(R.string.selected)
                    } else {
                        tvStatusSelected?.text = null
                        tvStatusSelected?.setHint(R.string.select_one_or_more)
                    }
                }
            }
            CATEGORIES_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedCategories =
                        data.getSerializableExtra(CategoriesActivity.KEY_SELECTED_CATEGORIES) as? ArrayList<String>?
                    if (this.selectedCategoriesList != null) {
                        this.selectedCategoriesList?.clear()
                    }
                    this.selectedCategoriesList = selectedCategories
                    if (!this.selectedCategoriesList.isNullOrEmpty()) {
                        tvCategorySelected?.text = String.format(
                            "%d",
                            selectedCategoriesList?.size
                        ) + " " + getString(R.string.selected)
                    } else {
                        tvCategorySelected?.text = null
                        tvCategorySelected?.setHint(R.string.select_one_or_more)
                    }
                }
            }
            TAGS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedTags =
                        data.getSerializableExtra(TagsActivity.KEY_SELECTED_TAGS) as? ArrayList<String>?
                    this.selectedTagsList.clear()
                    if (selectedTags != null) {
                        this.selectedTagsList = selectedTags
                    }
                    if (!this.selectedTagsList.isNullOrEmpty()) {
                        tvTagsSelected?.text = String.format(
                            "%d",
                            selectedTagsList.size
                        ) + " " + getString(R.string.selected)
                    } else {
                        tvTagsSelected?.text = null
                        tvTagsSelected?.setHint(R.string.select_one_or_more)
                    }
                }
            }
            DATES_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    this.selectedDueDateGreaterThanEqualToDatePicker = null
                    this.selectedDueDateLessThanEqualToDatePicker = null

                    val selectedDueDateGreaterThanEqualToDatePickerTemp =
                        data.extras?.getString(DateRangePickerActivity.KEY_SELECTED_DUEDATE_GREATER_THAN_EQUAL_TO_DATE_PICKER)
                    val selectedDueDateLessThanEqualToDatePickerTemp =
                        data.extras?.getString(DateRangePickerActivity.KEY_SELECTED_DUEDATE_LESS_THAN_EQUAL_TO_DATE_PICKER)

                    // convert date from long(string) to local format (14.11.2018) and store in main list i.e selectedDateList

                    if (selectedDueDateGreaterThanEqualToDatePickerTemp != null && selectedDueDateLessThanEqualToDatePickerTemp != null) {
                        this.selectedDueDateGreaterThanEqualToDatePicker =
                            MyDateTimeStamp.getDateFormattedStringFromMilliseconds(
                                java.lang.Long.parseLong(
                                    selectedDueDateGreaterThanEqualToDatePickerTemp
                                )
                            ) + " 00:00"
                        this.selectedDueDateLessThanEqualToDatePicker =
                            MyDateTimeStamp.getDateFormattedStringFromMilliseconds(
                                java.lang.Long.parseLong(
                                    selectedDueDateLessThanEqualToDatePickerTemp
                                )
                            ) + " 23:59"
                        tvDatesSelected?.text =
                            selectedDueDateGreaterThanEqualToDatePicker?.length?.minus(5)?.let {
                                selectedDueDateGreaterThanEqualToDatePicker?.substring(
                                    0,
                                    it
                                )
                            } + " - " + selectedDueDateLessThanEqualToDatePicker?.length?.minus(5)
                                ?.let {
                                    selectedDueDateLessThanEqualToDatePicker?.substring(
                                        0,
                                        it
                                    )
                                }
                    }
                }
            }
        }
    }

    companion object {
        const val ASSET_REQUEST_CODE = 535
        private const val TAGS_REQUEST_CODE = 533
        private const val STATUSES_REQUEST_CODE = 5361
        private const val CATEGORIES_REQUEST_CODE = 5362
        private const val USERS_REQUEST_CODE = 537
        private const val TEAMS_REQUEST_CODE = 540
        private const val DATES_REQUEST_CODE = 538
        const val KEY_SELECTED_MY_WORDORDER = "selected_my_workorder"
        const val KEY_SELECTED_DUE_DATE_GREATER_THAN_EQUAL_TO_TODAY =
            "selected_due_date_greater_than_equal_to_today"
        const val KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_TODAY =
            "selected_due_date_less_than_equal_to_today"
        const val KEY_SELECTED_DUE_DATE_LESS_THAN_EQUAL_TO_OVERDUE =
            "selected_due_date_less_than_equal_to_overdue"
        const val KEY_SELECTED_TYPE_LIST = "selected_type_list"
        const val KEY_SELECTED_PRIORITY_LIST = "selected_priority_list"
        const val KEY_SELECTED_INCLUDE_CHILDREN_ASSETS = "include_children_assets"
        const val KEY_SELECTED_INCLUDE_CHKLIST_ASSETS = "include_chklist_assets"
    }
}
