package com.example.fypintelidea.features.workOrder.workOrderCompletion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fypintelidea.core.providers.models.SpareParts
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.utils.AssetsUtility
import com.example.fypintelidea.R
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_workorder_complete.*
import org.koin.android.ext.android.inject
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class WorkOrderCompleteActivity : AppCompatActivity() {
    private val workOrderCompleteActivityViewModel: WorkOrderCompleteActivityViewModel by inject()
    private var workorder: Workorder? = null
    private val mContext = this@WorkOrderCompleteActivity
    private val sparePartsListOld = ArrayList<SpareParts>()
    private val SPEECH_REQUEST_CODE_PROBLEM = 100
    private val SPEECH_REQUEST_CODE_SOLUTION = 200
    private val SPEECH_REQUEST_CODE_COMMENT = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workorder_complete)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = resources.getString(R.string.complete_workorder)
            supportActionBar?.elevation = 0f
        }
        activity = this
        etProblem!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
        })
        etSolution!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
        })
        etComment!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
        })
        val i = intent
        workorder = i.getParcelableExtra(SELECTED_WORK_ORDER)
        if (workorder != null) {
            if (workorder!!.asset_name != null) {
                tvAssetName.text = workorder!!.asset_name
            }
            if (workorder!!.name != null) {
                tvName.text = workorder!!.name
            }

            val endTimeFormatted =
                MyDateTimeStamp.getDateTimeFormattedStringFromMilliseconds(Calendar.getInstance().timeInMillis)
            tvEndDate!!.text = endTimeFormatted
            workorder!!.contributors?.let { connectavoContributorAdditionComponent.setListOfUser(it) }
            workorder!!.costs?.let { connectavoCostAdditionComponent.setListOfCosts(it) }
            workorder!!.spareParts?.let { connectavoSparePartsComponent.setListOfSpareParts(it) }

            if (workorder!!.a_type != null) {
                if (workorder!!.a_type.equals(
                        Workorder.WORKORDER_TYPE_UNPLANNED,
                        ignoreCase = true
                    )
                ) {
                    imgMicProblem.visibility = View.VISIBLE
                    imgMicSolution.visibility = View.VISIBLE
                    ivProblem.visibility = View.VISIBLE
                    ivProblem.visibility = View.VISIBLE
                    etProblem.visibility = View.VISIBLE
                    tvProblem.visibility = View.VISIBLE
                    ivSolution.visibility = View.VISIBLE
                    tvSolution.visibility = View.VISIBLE
                    etSolution.visibility = View.VISIBLE

                    ivComment.visibility = View.GONE
                    etComment.visibility = View.GONE
                    imgMicComment.visibility = View.GONE
                    tvComment.visibility = View.GONE
                } else if (workorder!!.a_type.equals(
                        Workorder.WORKORDER_TYPE_PLANNED,
                        ignoreCase = true
                    )
                ) {
                    ivComment.visibility = View.VISIBLE
                    etComment.visibility = View.VISIBLE
                    tvComment.visibility = View.VISIBLE
                    imgMicComment.visibility = View.VISIBLE

                    imgMicProblem.visibility = View.GONE
                    imgMicSolution.visibility = View.GONE
                    tvProblem.visibility = View.GONE
                    ivProblem.visibility = View.GONE
                    etProblem.visibility = View.GONE
                    etProblem.visibility = View.GONE
                    ivSolution.visibility = View.GONE
                    etSolution.visibility = View.GONE
                    tvSolution.visibility = View.GONE
                }
            }
            tvEndDate!!.setOnClickListener {
                val now = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog.newInstance(
                    { _, year, monthOfYear, dayOfMonth ->
                        val now1 = Calendar.getInstance()
                        val timePickerDialog = TimePickerDialog.newInstance(
                            { _, hourOfDay, minute, _ ->
                                val date =
                                    dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year + " " + hourOfDay + ":" + minute
                                tvEndDate!!.text = date
                            },
                            now1.get(Calendar.HOUR_OF_DAY),
                            now1.get(Calendar.MINUTE),
                            false
                        )
                        timePickerDialog.isCancelable = true
                        timePickerDialog.title = resources.getString(R.string.select_end_time)
                        timePickerDialog.show(supportFragmentManager, "Timepickerdialog")
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.isCancelable = true
                datePickerDialog.setTitle(resources.getString(R.string.select_end_date))
                datePickerDialog.show(supportFragmentManager, "Datepickerdialog")
            }

            //SpareParts
            if (workorder!!.spareParts != null) {
                if (workorder!!.spareParts?.isNotEmpty() == true) {
                    workorder!!.spareParts?.let { sparePartsListOld.addAll(it) }
                }
            }


//            ivAddSpareParts.setOnClickListener {
//                val intent = Intent(mContext, SingleSelectComponentActivity::class.java)
//                intent.putParcelableArrayListExtra(
//                    SingleSelectComponentActivity.SELECTED_COMPONENT,
//                    workorder!!.spareParts as ArrayList<SpareParts>
//                )
//                intent.putExtra(
//                    SingleSelectComponentActivity.KEY_ENTITY,
//                    SingleSelectComponentActivity.ENTITY_SPARE_PART
//                )
//                startActivityForResult(intent, SPARE_PARTS_REQUEST_CODE)
//            }
//
//            ivAddContributor.setOnClickListener {
//                val intent = Intent(mContext, SingleSelectComponentActivity::class.java)
//                intent.putParcelableArrayListExtra(
//                    SingleSelectComponentActivity.SELECTED_COMPONENT,
//                    workorder!!.contributors as ArrayList<User>
//                )
//                intent.putExtra(
//                    SingleSelectComponentActivity.KEY_ENTITY,
//                    SingleSelectComponentActivity.ENTITY_CONTRIBUTOR
//                )
//                startActivityForResult(intent, CONTRIBUTOR_REQUEST_CODE)
//            }
        }

        imgMicProblem.setOnClickListener {
            voiceProblem()
        }

        imgMicSolution.setOnClickListener {
            voiceSolution()
        }

        imgMicComment.setOnClickListener {
            voiceComment()
        }
        setObservers()
    }

    private fun setObservers() {
    }

    private fun voiceProblem() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE_PROBLEM)
    }

    private fun voiceSolution() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE_SOLUTION)
    }

    private fun voiceComment() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE_COMMENT)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SPEECH_REQUEST_CODE_PROBLEM -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val spokenText: ArrayList<String>? =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    etProblem!!.setText(spokenText?.get(0))
                }
            }
            SPEECH_REQUEST_CODE_SOLUTION -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val spokenText: ArrayList<String>? =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    etSolution?.setText(spokenText?.get(0))
                }
            }
            SPEECH_REQUEST_CODE_COMMENT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val spokenText: ArrayList<String>? =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    etComment?.setText(spokenText?.get(0))
                }
            }
        }
//        if (requestCode == SPARE_PARTS_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                workorder?.let { workOrder ->
//                    workOrder.spareParts?.clear()
//                    val checkedSparePartsReceived =
//                        data.getParcelableExtra<SpareParts>(SingleSelectComponentActivity.SELECTED_COMPONENT)
//                    checkedSparePartsReceived?.let {
//                        workOrder.spareParts?.add(checkedSparePartsReceived)
//                    }
//                    workOrder.spareParts?.let { connectavoSparePartsComponent.setListOfSpareParts(it) }
//                }
//            }
//        }
//        if (requestCode == CONTRIBUTOR_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                workorder?.let { workOrder ->
//                    workOrder.contributors?.clear()
//                    val clickedUserReceived =
//                        data.getParcelableExtra<User>(SingleSelectComponentActivity.SELECTED_COMPONENT)
//                    clickedUserReceived?.let {
//                        workOrder.contributors?.add(clickedUserReceived)
//                    }
//                    workOrder.contributors?.let {
//                        connectavoContributorAdditionComponent.setListOfUser(
//                            it
//                        )
//                    }
//                }
//            }
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.workorder_complete_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_next -> when {
                tvEndDate!!.text.toString().isEmpty() -> tvEndDate!!.error =
                    resources.getString(R.string.end_date_is_empty)
                etHoursSpent!!.text.toString().isEmpty() -> etHoursSpent!!.error =
                    resources.getString(R.string.field_required)
                else -> {
                    if (connectavoContributorAdditionComponent.checkValidations() && connectavoCostAdditionComponent.checkValidations() && connectavoSparePartsComponent.checkValidations()) {
                        workorder!!.contributors =
                            connectavoContributorAdditionComponent.getListOfUser()
                        workorder!!.costs = connectavoCostAdditionComponent.getListOfCosts()
                        workorder!!.spareParts = connectavoSparePartsComponent.getListOfSpareParts()
                        workorder!!.modified_by =
                            SessionManager(this).getString(SessionManager.KEY_LOGIN_ID)
                        workorder!!.status = Workorder.WORKORDER_STATUS_DONE
                        workorder!!.hours_spent = etHoursSpent!!.text.toString()
                        workorder!!.end_time =
                            MyDateTimeStamp.dateTimeStringToDateInUTC(tvEndDate!!.text.toString())
                        if (workorder!!.a_type != null) {
                            if (workorder!!.a_type.equals(
                                    Workorder.WORKORDER_TYPE_PLANNED,
                                    ignoreCase = true
                                )
                            ) {
                                if (etComment!!.text.toString().isNotEmpty()) {
                                    workorder!!.comment = etComment!!.text.toString()
                                }
                            } else {
                                if (etProblem!!.text.toString().isNotEmpty()) {
                                    workorder!!.problem = etProblem!!.text.toString()
                                }
                                if (etSolution!!.text.toString().isNotEmpty()) {
                                    workorder!!.solution = etSolution!!.text.toString()
                                }
                            }
                        }

                        val sparePartsListNew: ArrayList<SpareParts>? = workorder!!.spareParts
                        for (i in sparePartsListOld.indices) {
                            var matched = false
                            for (j in sparePartsListNew?.indices!!) {
                                if (sparePartsListOld[i].spare_part_id.equals(
                                        sparePartsListNew[j].spare_part_id,
                                        ignoreCase = true
                                    )
                                ) {
                                    sparePartsListNew[j].work_order_spare_part_id =
                                        sparePartsListOld[i].work_order_spare_part_id
                                    matched = true
                                }
                            }
                            if (!matched) {
                                val index = sparePartsListOld.indexOf(sparePartsListOld[i])
                                sparePartsListOld[i].location = "_destroy"
                                sparePartsListOld[index] = sparePartsListOld[i]
                                sparePartsListNew.add(sparePartsListOld[i])
                            }
                        }
                        workorder!!.spareParts = sparePartsListNew
                        if (sparePartsListNew != null) {
                            AssetsUtility.sparePartsList = sparePartsListNew
                        }

                        val newIntent = Intent(this, WorkOrderCompleteSignatureActivity::class.java)
                        newIntent.putExtra(SELECTED_WORK_ORDER, workorder as Serializable?)
                        startActivity(newIntent)
                    }
                }
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val SELECTED_WORK_ORDER = "selected_work_order"
        const val SPARE_PARTS_REQUEST_CODE = 541
        const val CONTRIBUTOR_REQUEST_CODE = 542
        lateinit var activity: Activity
    }
}
