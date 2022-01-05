package com.example.fypintelidea.features.workOrder.newWorkorder

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.connectavo.app.connectavo_android.core.*
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.R
import com.example.fypintelidea.core.*
import com.example.fypintelidea.core.providers.models.*
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.session.SessionManager.Companion.KEY_LOGIN_SUBDOMAIN
import com.example.fypintelidea.core.utils.AssetsUtility
import com.example.fypintelidea.core.utils.ImageTreatmentActivity
import com.example.fypintelidea.core.utils.Utils
import com.example.fypintelidea.core.views.ChecklistTemplateContainer
import com.example.fypintelidea.core.views.CustomImageViewCancelable
import com.example.fypintelidea.features.activities.TagsActivity
import com.example.fypintelidea.features.custom_status.CustomStatusAdapter
import com.example.fypintelidea.features.workOrder.workOrderCompletion.SingleSelectComponentActivity
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_new_workorder.*
import kotlinx.android.synthetic.main.activity_workorder_complete.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class NewWorkorderActivity : ConnectavoBaseActivity(), EasyPermissions.PermissionCallbacks {
    val sessionManager: SessionManager by inject()
    private val newWorkOrderActivityViewModel: NewWorkOrderActivityViewModel by inject()
    internal var totalAssets: List<Asset>? = null
    private var photoPaths = ArrayList<String>()
    private var selectedPriority = Workorder.WORKORDER_PRIORITY_MEDIUM
    private var selectedAssetId: String? = null
    private var selectedUserId: String? = null
    private var selectedChklistTemplateId: String? = null
    private var selectedCategory: String? = null
    private var selectedStatus: String? = null
    private val tags = ArrayList<Tag>()
    private val spareParts = ArrayList<SpareParts>()
    private var employees = ArrayList<User>()
    private val teams = ArrayList<Team>()
    private val tagIds = ArrayList<String>()
    private val teamIds = ArrayList<String>()
    private var showMore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_workorder)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = resources.getString(R.string.new_work_order)
            actionBar.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.elevation = 0f
        }

        fetchAssetsFromServer()

        ivSpeech.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, SPEECH_TO_TEXT_REQUEST_CODE)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.device_doesnt_support_speech_input),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        addItemsOnSpinnerUsers()
        addItemsOnSpinnerStatus()
        addItemsOnSpinnerCategory()

        tvTags.setOnClickListener {
            val i = Intent(this@NewWorkorderActivity, TagsActivity::class.java)
            i.putExtra(
                TagsActivity.KEY_SELECTED_TAGS,
                tags as ArrayList<Tag>?
            )
            startActivityForResult(i, TAGS_REQUEST_CODE)
        }

        addItemsOnSpinnerTemplate()

        ivAddDocuments.setOnClickListener {
            if (EasyPermissions.hasPermissions(
                    this@NewWorkorderActivity,
                    FilePickerConst.PERMISSIONS_FILE_PICKER
                ) && EasyPermissions.hasPermissions(
                    this@NewWorkorderActivity,
                    Manifest.permission.CAMERA
                )
            ) {
                onPickPhoto()
            } else {
                // Ask for one permission
                EasyPermissions.requestPermissions(
                    this@NewWorkorderActivity,
                    getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM,
                    FilePickerConst.PERMISSIONS_FILE_PICKER
                )
                EasyPermissions.requestPermissions(
                    this@NewWorkorderActivity,
                    getString(R.string.camera_permission),
                    1,
                    Manifest.permission.CAMERA
                )
            }
        }
//
        tvEmployee.setOnClickListener {
            val i = Intent(this@NewWorkorderActivity, SingleSelectComponentActivity::class.java)
            i.putExtra(
                SingleSelectComponentActivity.KEY_ENTITY,
                SingleSelectComponentActivity.ENTITY_EMPLOYEE
            )
            i.putParcelableArrayListExtra(
                SingleSelectComponentActivity.SELECTED_EMPLOYEE,
                employees
            )
            intent.putExtra(
                SingleSelectComponentActivity.KEY_ENTITY,
                SingleSelectComponentActivity.ENTITY_EMPLOYEE
            )
            startActivityForResult(i, EMPLOYEE_REQUEST_CODE)
        }

        setObservers()
    }

    override fun onResume() {
        super.onResume()
        populateReceivedDocumentsAndAddListener(photoPaths)
    }

    private fun fetchAssetsFromServer() {
        totalAssets = AssetsUtility.assetListHigh
        if (totalAssets == null) {
            Toast.makeText(
                this@NewWorkorderActivity,
                resources.getString(R.string.could_not_fetch_assets),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onPickPhoto() {
        if (photoPaths.size >= MaxDocumentAttachmentCounts.MAX_ATTACHMENT_COUNT_WO_REQUESTS) {
            Toast.makeText(
                this,
                "Cannot select more than ${MaxDocumentAttachmentCounts.MAX_ATTACHMENT_COUNT_WO_REQUESTS} items",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            FilePickerBuilder.instance
                .setMaxCount(MaxDocumentAttachmentCounts.MAX_ATTACHMENT_COUNT_WO_REQUESTS)
                .setSelectedFiles(Utils.getArrayOfUrisFromArrayOfStrings(photoPaths))
                .setActivityTheme(R.style.FilePickerTheme)
                .setActivityTitle("Please select media")
                .enableVideoPicker(false)
                .enableCameraSupport(true)
                .showGifs(true)
                .showFolderView(false)
                .enableSelectAll(true)
                .enableImagePicker(true)
                .setCameraPlaceholder(R.drawable.custom_camera)
                .pickPhoto(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ASSET_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK && data != null) {
                val assetId = data.getStringExtra(SELECTED_ASSET_ID)
                val assetName = data.getStringExtra(SELECTED_ASSET_NAME)
            }

            TAGS_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK && data != null) {
                val checkedTagsReceived =
                    data.getSerializableExtra(TagsActivity.KEY_SELECTED_TAGS) as? ArrayList<Tag>
                checkedTagsReceived?.let {
                    tags.clear()
                    tags.addAll(it)
                    when {
                        tags.size == 1 -> tvTags.text = it[0].name
                        tags.size == 2 -> tvTags.text = it[0].name + ", " + it[1].name
                        tags.size > 2 -> tvTags.text =
                            String.format("%d", tags.size) + " " + getString(R.string.selected)
                        else -> tvTags.text = null
                    }

                    // put tag id strings in separate array for syncing
                    tagIds.clear()
                    for (i in tags.indices) {
                        tags[i].id?.let { tagId ->
                            tagIds.add(tagId)
                        }
                    }
                }
            }

            FilePickerConst.REQUEST_CODE_PHOTO -> if (resultCode == Activity.RESULT_OK && data != null) {
                val listOfUri =
                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                photoPaths.clear()
                photoPaths.addAll(Utils.getArrayOfStringsFromArrayOfUris(this, listOfUri))
                populateReceivedDocumentsAndAddListener(photoPaths)
            }
            SPEECH_TO_TEXT_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                result?.let {
                    etDescription.setText(it[0])
                }
            }
        }
    }

    private fun populateReceivedDocumentsAndAddListener(filePaths: ArrayList<String>) {
        glDocuments.removeAllViews()
        if (filePaths.size > 0) {
            for (j in filePaths.indices) {
                val path = filePaths[j]

                val customIV = CustomImageViewCancelable(this@NewWorkorderActivity)
                customIV.setmImgPhoto(R.drawable.image_placeholder)
                customIV.setmImgFromPath(path)
                customIV.setOnClickListener {
                    val intent =
                        Intent(this@NewWorkorderActivity, ImageTreatmentActivity::class.java)
                    intent.putExtra(ImageTreatmentActivity.IMAGE_PATH, path)
                    startActivity(Intent(intent))
                }
                customIV.getmBtnClose().setOnClickListener {
                    filePaths.remove(filePaths[j])
                    glDocuments.removeAllViews()
                    populateReceivedDocumentsAndAddListener(filePaths)
                }
                glDocuments.addView(customIV)
            }
        }
    }

    private fun addItemsOnSpinnerUsers() {
        progressBarNewWorkOrder.visibility = View.VISIBLE
        newWorkOrderActivityViewModel.fetchAllUsers()
    }

    private fun addItemsOnSpinnerStatus() {
        val list = ArrayList<Status>()
        val constantsStatuses = ConstantsStatuses()
        list.addAll(constantsStatuses.statusesForNewWO(this@NewWorkorderActivity))
        spStatus.adapter = CustomStatusAdapter(this@NewWorkorderActivity, list)
        spStatus.onItemSelectedListener = SpinnerStatusOnItemSelectedListener()
    }

    private fun addItemsOnSpinnerCategory() {
        val list = ArrayList<String>()
        list.add("-")
        val constantsCategories = ConstantsCategories()
        when {
            sessionManager.getString(KEY_LOGIN_SUBDOMAIN).equals(
                CustomerSpaces.curatedstage.name,
                ignoreCase = true
            ) -> {
                list.addAll(constantsCategories.allCategoriesForSteiner(this@NewWorkorderActivity))
            }
            sessionManager.getString(KEY_LOGIN_SUBDOMAIN).equals(
                CustomerSpaces.stoba.name,
                ignoreCase = true
            ) -> {
                list.addAll(constantsCategories.allCategoriesForStoba(this@NewWorkorderActivity))
            }
            sessionManager.getString(KEY_LOGIN_SUBDOMAIN).equals(
                CustomerSpaces.aicher.name,
                ignoreCase = true
            ) -> {
                list.addAll(constantsCategories.allCategoriesForAicher(this@NewWorkorderActivity))
            }
            sessionManager.getString(KEY_LOGIN_SUBDOMAIN).equals(
                CustomerSpaces.qa4.name,
                ignoreCase = true
            ) -> {
                list.addAll(constantsCategories.allCategoriesForQA4(this@NewWorkorderActivity))
            }
            else -> {
                list.addAll(constantsCategories.all(this@NewWorkorderActivity))
            }
        }
        val dataAdapter = ArrayAdapter(this, R.layout.spinner_item, list)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = dataAdapter
    }

    private fun addItemsOnSpinnerTemplate() {
        progressBarNewWorkOrder.visibility = View.VISIBLE
        newWorkOrderActivityViewModel.getAllTemplates()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_save -> {
                if (etName.text.toString().isEmpty()) {
                    Toast.makeText(this, "fill the required", Toast.LENGTH_SHORT).show()
                } else if (selectedUserId == null && teamIds.size < 1) {
                    val errorTextSpUser = tvEmployee
                    errorTextSpUser.error = ""
                    errorTextSpUser.setTextColor(Color.RED) //just to highlight that this is an error
                    errorTextSpUser.text = resources.getString(R.string.field_required)
                    tvTeam.error = resources.getString(R.string.field_required)
                } else {
                    val workOrder = NewWorkOrderRequest()
                    workOrder.name = etName.text.toString()
                    workOrder.asset_id = selectedAssetId
                    workOrder.modified_by =
                        sessionManager.getString(SessionManager.KEY_LOGIN_ID)
                    workOrder.status = selectedStatus
                    workOrder.company_id =
                        sessionManager.getString(SessionManager.KEY_LOGIN_COMPANY_ID)
                    workOrder.hashed = "" + Calendar.getInstance().timeInMillis
                    workOrder.category = ""
                    selectedCategory = spCategory?.selectedItem.toString()
                    selectedCategory?.let {
                        if (selectedCategory != "-") {
                            workOrder.category = ConstantsCategories.getAPIName(
                                this@NewWorkorderActivity,
                                it
                            )
                        }
                    }

                    workOrder.description = ""
                    if (etDescription != null) {
                        if (etDescription.text.toString()
                                .isNotEmpty()
                        ) workOrder.description = etDescription.text.toString()
                    }
                    workOrder.tag_ids = tagIds
                    workOrder.teamIds = teamIds


                    val name = workOrder.name?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val assetId =
                        workOrder.asset_id?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val modifiedBy =
                        workOrder.modified_by?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val status =
                        workOrder.status?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val companyId =
                        workOrder.company_id?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val hashed =
                        workOrder.hashed?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val assignedTo: RequestBody? =
                        selectedUserId?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val chklistTemplate: RequestBody = if (selectedChklistTemplateId != null) {
                        selectedChklistTemplateId!!.toRequestBody("text/plain".toMediaTypeOrNull())
                    } else {
                        "".toRequestBody("text/plain".toMediaTypeOrNull())
                    }
                    val dueDate =
                        workOrder.due_date?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val priority =
                        workOrder.priority?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val eta = workOrder.eta?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val category =
                        workOrder.category?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val description =
                        workOrder.description?.toRequestBody("text/plain".toMediaTypeOrNull())

                    val mapSpareParts = HashMap<String, String>()
                    for (i in spareParts.indices) {
                        spareParts[i].spare_part_id?.let {
                            mapSpareParts["work_order[work_order_spare_parts_attributes][$i][spare_part_id]"] =
                                it
                        }
                        spareParts[i].quantity?.let {
                            mapSpareParts["work_order[work_order_spare_parts_attributes][$i][quantity]"] =
                                it
                        }
                    }


                    val files = arrayOfNulls<MultipartBody.Part>(photoPaths.size)
                    for (index in photoPaths.indices) {
                        val file = File(photoPaths[index])
                        val surveyBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                        files[index] = MultipartBody.Part.createFormData(
                            "work_order[docs_attributes][$index][document]",
                            file.name,
                            surveyBody
                        )
                    }
                    progressBarNewWorkOrder.visibility = View.VISIBLE
                    newWorkOrderActivityViewModel.createWorkOrderMultipart(
                        name,
                        assetId,
                        modifiedBy,
                        status,
                        companyId,
                        hashed,
                        assignedTo,
                        workOrder.teamIds,
                        dueDate,
                        priority,
                        eta,
                        category,
                        description,
                        chklistTemplate,
                        workOrder.tag_ids,
                        mapSpareParts,
                        null,
                        files
                    )
                }
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setObservers() {
        newWorkOrderActivityViewModel.multiPartsWorkOrderObservable.observe(this) {
            val apiResponse = it.value
            Toast.makeText(this, getString(R.string.new_workorder_created), Toast.LENGTH_SHORT)
                .show()
            finish()
        }
        newWorkOrderActivityViewModel.getAllTemplatesObservable.observe(this)
        {
            val apiResponse = it.value
            progressBarNewWorkOrder.visibility = View.GONE
        }
        newWorkOrderActivityViewModel.fetchAllUsersObservable.observe(this)
        {
            progressBarNewWorkOrder.visibility = View.GONE
            employees = it.value!!
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        onPickPhoto()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private inner class SpinnerStatusOnItemSelectedListener : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            when (pos) {
                0 -> selectedStatus = ConstantsStatuses.getAPIName(
                    this@NewWorkorderActivity,
                    this@NewWorkorderActivity.resources.getString(R.string.custom_status_open)
                )
                1 -> selectedStatus = ConstantsStatuses.getAPIName(
                    this@NewWorkorderActivity,
                    this@NewWorkorderActivity.resources.getString(R.string.custom_status_in_progress)
                )
                2 -> selectedStatus = ConstantsStatuses.getAPIName(
                    this@NewWorkorderActivity,
                    this@NewWorkorderActivity.resources.getString(R.string.custom_status_pause)
                )
            }
        }

        override fun onNothingSelected(adapterView: AdapterView<*>) {
        }
    }

    companion object {
        const val RC_FILE_PICKER_PERM = 321
        const val ASSET_REQUEST_CODE = 535
        const val SELECTED_ASSET_ID = "selected_asset_id"
        const val SELECTED_ASSET_NAME = "selected_asset_name"
        private const val TAGS_REQUEST_CODE = 533
        private const val QR_SCAN_REQUEST_CODE = 534
        private const val TEAMS_REQUEST_CODE = 539
        private const val SPARE_PARTS_REQUEST_CODE = 541
        private const val EMPLOYEE_REQUEST_CODE = 543
        private const val SPEECH_TO_TEXT_REQUEST_CODE = 542
        private const val LINEAR_LAYOUT_ONE_ROW = "llOneRow"
    }
}
