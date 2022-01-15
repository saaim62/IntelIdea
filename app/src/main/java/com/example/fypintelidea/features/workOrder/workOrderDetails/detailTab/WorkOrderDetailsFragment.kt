package com.example.fypintelidea.features.workOrder.workOrderDetails.detailTab

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseFragment
import com.example.fypintelidea.core.providers.models.*
import com.example.fypintelidea.core.services.ApiClient
import com.example.fypintelidea.core.services.ApiInterface
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.utils.ImageTreatmentActivity
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import com.example.fypintelidea.core.utils.Utils
import com.example.fypintelidea.core.views.CustomImageViewCancelable
import com.example.fypintelidea.core.views.DynamicViews
import com.example.fypintelidea.features.DocAdapter
import com.example.fypintelidea.features.workOrder.workOrderDetails.WorkOrderDetailActivity
import com.google.gson.JsonObject
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.fragment_workorder_detail.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class WorkOrderDetailsFragment : ConnectavoBaseFragment(), EasyPermissions.PermissionCallbacks {
    private val workOrderDetailsFragmentViewModel: WorkOrderDetailsFragmentViewModel by inject()
    private var photoPaths: ArrayList<String> = ArrayList()
    private var workorder: Workorder? = null
    private var showMore = false
    private var recommendationsList: ArrayList<WorkOrderRecommendation.Recommendation>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workorder_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { activity ->
            workorder = args?.getSerializable(SELECTED_WORK_ORDER) as Workorder?

            // ID
            tvWorkorderIdValue.text = workorder?.sequence_id


            //RESPONSIBLE
            if (workorder?.assigned_to != null) {
                if (workorder?.assigned_to?.name != null) {
                    tvAssigneeValue.text = workorder?.assigned_to?.name
                } else if (workorder?.assigned_to?.email != null) {
                    tvAssigneeValue.text = workorder?.assigned_to?.email
                }
            } else {
                if (workorder?.assignee != null) {
                    tvAssigneeValue.text = workorder?.assignee
                }
            }

            // TEAM
            fetchTeamsAndPopulate()


            //CreatedAt
            val createdAtFormatted =
                workorder?.created_at?.let { java.lang.Long.valueOf(it) }?.let {
                    MyDateTimeStamp.getDateTimeFormattedStringFromMilliseconds(
                        it
                    )
                }
            tvCreatedAtValue.text = createdAtFormatted

            //ESTIMATED
            tvEstimatedValue.text = workorder?.eta

            //DESCRIPTION
            tvDescriptionValue.text = workorder?.description

            val dynamicViews = DynamicViews(activity)

            //TAGS
            if (workorder?.tags != null) {
                if (workorder?.tags?.size!! > 0) {
                    for (j in 0 until workorder?.tags?.size!!) {
                        gridTag.addView(
                            dynamicViews.textView(
                                workorder?.tags!![j].name,
                                "tagTags",
                                true,
                                200
                            )
                        )
                    }
                }
            } else {
                tvTags.visibility = View.GONE
            }

            //Documents
            if (workorder?.documents != null) {
                val list = workorder?.documents!!.filter { !it.signature }
                gridDocument.adapter = DocAdapter(activity, R.layout.pdf_layout, list)
            } else {
                tvDocuments.visibility = View.GONE
            }

            if (workorder?.status.equals(Workorder.WORKORDER_STATUS_DONE, ignoreCase = true)) {
                textViewAddDocument.isEnabled = false
            }
            textViewAddDocument.setOnClickListener {
                requestPickPhoto()
            }

            buttonUpload.setOnClickListener {
                if (photoPaths.size > 0) {
                    photoPaths.forEach {
                        updatePicture(File(it))
                    }
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.no_images_selected_to_upload),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            tvTags.visibility = View.GONE
            gridTag.visibility = View.GONE
            tvDocuments.visibility = View.GONE
            gridDocument.visibility = View.GONE
            textViewAddDocument.visibility = View.GONE
            gridDocumentPreview.visibility = View.GONE
        }

    }

    private fun fetchGuideRecommendations(selectedWorkOrderId: String) {
        progressBarWorkOrderDetailFragment.visibility = View.VISIBLE
        workOrderDetailsFragmentViewModel.getWorkOrderRecommendations(selectedWorkOrderId)
    }

    @SuppressLint("SetTextI18n")
    private fun populateRecommendationsToUI(
        workOrderRecommendation: WorkOrderRecommendation
    ) {
        val recommendationsList: ArrayList<WorkOrderRecommendation.Recommendation> = ArrayList()
        recommendationsList.addAll(workOrderRecommendation.recommendationWorkOrder)
        recommendationsList.addAll(workOrderRecommendation.recommendationDocuments)
    }


    override fun onResume() {
        super.onResume()
        populateReceivedPhotoAndAddListener()
    }

    @AfterPermissionGranted(RC_FILE_PICKER_PERM)
    fun requestPickPhoto() {
        activity?.let { activity ->
            if (EasyPermissions.hasPermissions(
                    activity,
                    FilePickerConst.PERMISSIONS_FILE_PICKER
                ) && EasyPermissions.hasPermissions(
                    activity,
                    Manifest.permission.CAMERA
                )
            ) {
                onPickPhoto()
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM,
                    FilePickerConst.PERMISSIONS_FILE_PICKER
                )
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.camera_permission),
                    1,
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    private fun onPickPhoto() {
        FilePickerBuilder.instance
            .setActivityTheme(R.style.FilePickerTheme)
            .setActivityTitle(getString(R.string.please_select_media))
            .enableVideoPicker(false)
            .enableCameraSupport(true)
            .showGifs(true)
            .showFolderView(false)
            .enableSelectAll(false)
            .enableImagePicker(true)
            .setCameraPlaceholder(R.drawable.custom_camera)
            .pickPhoto(this, FilePickerConst.REQUEST_CODE_DOC)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchTeamsAndPopulate() {
        progressBarWorkOrderDetailFragment.visibility = View.VISIBLE
        workOrderDetailsFragmentViewModel.getAllTeams()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_DOC -> if (resultCode == Activity.RESULT_OK && data != null) {
                val listOfUri =
                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                photoPaths.clear()
                activity?.let { Utils.getArrayOfStringsFromArrayOfUris(it, listOfUri) }
                    ?.let { photoPaths.addAll(it) }
                populateReceivedPhotoAndAddListener()
                buttonUpload.visibility = View.VISIBLE
            }
        }
    }

    private fun populateReceivedPhotoAndAddListener() {
        gridDocumentPreview!!.removeAllViews()
        if (photoPaths.size > 0) {
            for (j in photoPaths.indices) {
                val path = photoPaths[j]
                val customIV = CustomImageViewCancelable(activity)
                customIV.setmImgPhoto(R.drawable.image_placeholder)
                customIV.setmImgFromPath(path)
                customIV.setOnClickListener {
                    val intent =
                        Intent(activity, ImageTreatmentActivity::class.java)
                    intent.putExtra(ImageTreatmentActivity.IMAGE_PATH, path)
                    startActivity(Intent(intent))
                }
                customIV.getmBtnClose().setOnClickListener {
                    photoPaths.remove(path)
                    buttonUpload.visibility = View.GONE
                    gridDocumentPreview!!.removeAllViews()
                    populateReceivedPhotoAndAddListener()
                }
                gridDocumentPreview!!.addView(customIV)
            }
        }
    }

    private fun setObservers() {
        workOrderDetailsFragmentViewModel.workOrderRecommendationObservable.observe(this) {
            val apiResponse = it.value
            populateRecommendationsToUI(apiResponse)
        }

        workOrderDetailsFragmentViewModel.getAllTeamsObservable.observe(this) {
            val apiResponse = it.value
            progressBarWorkOrderDetailFragment?.visibility = View.GONE
            if (isAdded) {
                if (apiResponse.isNotEmpty()) {
                    val dynamicViews = DynamicViews(activity)
                    if (workorder?.teamIds != null) {
                        if (workorder?.teamIds?.size!! > 0) {
                            for (j in 0 until workorder?.teamIds?.size!!) {
                                for (i in apiResponse.indices) {
                                    if (workorder?.teamIds!![j].equals(
                                            apiResponse[i].id,
                                            ignoreCase = true
                                        )
                                    ) {
                                        gridTeam.addView(
                                            dynamicViews.textView(
                                                apiResponse[i].name,
                                                "tagTeams",
                                                false,
                                                200
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            gridTeam.addView(
                                dynamicViews.textView(
                                    "-",
                                    "tagTeams",
                                    false,
                                    200
                                )
                            )
                        }
                    } else {
                        tvTeam.visibility = View.GONE
                        gridTeam.addView(
                            dynamicViews.textView(
                                "-",
                                "tagTeams",
                                false,
                                200
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updatePicture(myFile: File) {
        activity?.let {
            val entityId = workorder?.id?.toRequestBody("text/plain".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData(
                "doc",
                myFile.name,
                myFile.asRequestBody("image/*".toMediaTypeOrNull())
            )

            if (entityId != null) {
                progressBarWorkOrderDetailFragment.visibility = View.VISIBLE
                SessionManager(it).getString(SessionManager.KEY_LOGIN_EMAIL)?.let { it1 ->
                    SessionManager(it).getString(SessionManager.KEY_LOGIN_TOKEN)?.let { it2 ->
                        ApiClient.getClient(it)?.create(ApiInterface::class.java)
                            ?.uploadDoc(
                                it1,
                                it2,
                                entityId,
                                filePart
                            )?.enqueue(object : Callback<JsonObject> {
                                override fun onResponse(
                                    call1: Call<JsonObject>,
                                    response: Response<JsonObject>
                                ) {
                                    progressBarWorkOrderDetailFragment.visibility = View.GONE
                                    try {
                                        val statusCode = response.code()
                                        if (response.isSuccessful) {
                                            if (response.body() != null && statusCode == 200) {
                                                val jObj: JsonObject = response.body()!!
                                                val id = jObj.get("id").asString
                                                val contentType =
                                                    jObj.get("content_type").asString
                                                val filename = jObj.get("filename").asString
                                                val url = jObj.get("url").asString
                                                val signature = jObj.get("signature").asBoolean
                                                workorder?.documents?.add(
                                                    Document(
                                                        id,
                                                        filename,
                                                        contentType,
                                                        url,
                                                        signature
                                                    )
                                                )
                                                Toast.makeText(
                                                    activity,
                                                    resources.getString(R.string.successfully_updated),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                it.finish()
                                                val i = Intent(
                                                    activity,
                                                    WorkOrderDetailActivity::class.java
                                                )
                                                i.putExtra(
                                                    WorkOrderDetailActivity.SELECTED_WORK_ORDER,
                                                    workorder as Serializable
                                                )
                                                startActivity(i)
                                            }
                                        }
                                    } catch (e: Exception) {
                                    }

                                }

                                override fun onFailure(call1: Call<JsonObject>, t: Throwable) {
                                    progressBarWorkOrderDetailFragment.visibility = View.GONE
                                    Toast.makeText(
                                        activity,
                                        resources.getString(R.string.check_internet),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(workOrder: Workorder): WorkOrderDetailsFragment {
            val fragmentFirst = WorkOrderDetailsFragment()
            args = Bundle()
            args!!.putSerializable(SELECTED_WORK_ORDER, workOrder)
            fragmentFirst.arguments = args
            return fragmentFirst
        }

        private const val RC_FILE_PICKER_PERM = 321
        const val SELECTED_WORK_ORDER = "selected_work_order"
        private var args: Bundle? = null
    }
}