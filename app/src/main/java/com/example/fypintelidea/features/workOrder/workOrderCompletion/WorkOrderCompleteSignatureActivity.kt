package com.example.fypintelidea.features.workOrder.workOrderCompletion

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.utils.Utils
import com.example.fypintelidea.features.workOrder.workOrderDetails.WorkOrderDetailActivity
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.activity_signature.*
import kotlinx.android.synthetic.main.activity_workorder_detail.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import java.io.*
import java.util.*


class WorkOrderCompleteSignatureActivity : ConnectavoBaseActivity() {
    val sessionManager: SessionManager by inject()
    private val workOrderCompleteSignatureActivityViewModel: WorkOrderCompleteSignatureActivityViewModel by inject()
    private var mSignaturePad: SignaturePad? = null
    private var mClearButton: Button? = null
    private var workorder: Workorder? = null
    private var isSigned: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = resources.getString(R.string.draw_signature)
            supportActionBar?.elevation = 0f
        }
        verifyStoragePermissions(this)
        mSignaturePad = findViewById(R.id.signature_pad)
        mSignaturePad!!.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {}

            override fun onSigned() {
                isSigned = true
                mClearButton!!.isEnabled = true
                val drawable = ContextCompat.getDrawable(
                    this@WorkOrderCompleteSignatureActivity,
                    R.drawable.ic_undo_blue_24dp
                )
                drawable?.let {
                    DrawableCompat.setTint(
                        it,
                        ContextCompat.getColor(
                            this@WorkOrderCompleteSignatureActivity,
                            R.color.Color_Primary
                        )
                    )
                    mClearButton!!.setCompoundDrawablesWithIntrinsicBounds(null, null, it, null)
                }
            }

            override fun onClear() {
                isSigned = false
                mClearButton!!.isEnabled = false
                val drawable = ContextCompat.getDrawable(
                    this@WorkOrderCompleteSignatureActivity,
                    R.drawable.ic_undo_grey_24dp
                )
                drawable?.let {
                    DrawableCompat.setTint(
                        it,
                        ContextCompat.getColor(
                            this@WorkOrderCompleteSignatureActivity,
                            R.color.md_grey_400
                        )
                    )
                    mClearButton!!.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        it,
                        null
                    )
                }
            }
        })
        mClearButton = findViewById(R.id.clear_button)
        mClearButton!!.setOnClickListener { mSignaturePad!!.clear() }
        val i = intent
        workorder = i.getParcelableExtra(WorkOrderCompleteActivity.SELECTED_WORK_ORDER)
    }

    private fun onSave() {
        val signatureBitmap = mSignaturePad!!.signatureBitmap
        val myFile = Utils.getFileByBitmap(this, signatureBitmap)
//        ProgressDialogProvider.show(this)
        val mapSparePartHavingWorkOrderSparePartId = HashMap<String, String>()
        val mapSparePartHavingOnlySparePartId = HashMap<String, String>()
        for (i in 0 until workorder?.spareParts?.size!!) {
            if (workorder?.spareParts?.get(i)?.work_order_spare_part_id != null) {
                mapSparePartHavingWorkOrderSparePartId["work_order[work_order_spare_parts_attributes][$i][id]"] =
                    workorder?.spareParts!![i].work_order_spare_part_id!!

                // jugaar here remove it later.
                if (workorder?.spareParts?.get(i)?.quantity != null) {
                    mapSparePartHavingWorkOrderSparePartId["work_order[work_order_spare_parts_attributes][$i][quantity]"] =
                        workorder?.spareParts!![i].quantity!!
                } else {
                    mapSparePartHavingWorkOrderSparePartId["work_order[work_order_spare_parts_attributes][$i][_destroy]"] =
                        "1"
                }

                // jugaar here remove it later.
                if (workorder?.spareParts?.get(i)!!.location != null && workorder?.spareParts?.get(i)!!.location.equals(
                        "_destroy",
                        ignoreCase = true
                    )
                ) {
                    mapSparePartHavingWorkOrderSparePartId["work_order[work_order_spare_parts_attributes][$i][_destroy]"] =
                        "1"
                }
            } else {
                mapSparePartHavingOnlySparePartId["work_order[work_order_spare_parts_attributes][$i][spare_part_id]"] =
                    workorder?.spareParts?.get(i)?.spare_part_id!!
                mapSparePartHavingOnlySparePartId["work_order[work_order_spare_parts_attributes][$i][quantity]"] =
                    workorder?.spareParts?.get(i)?.quantity!!
            }
        }
        val mapChecklist = HashMap<String, String>()
        for (i in workorder?.wo_checklists_attributes?.indices!!) {
            mapChecklist["work_order[wo_checklists_attributes][$i][id]"] =
                workorder?.wo_checklists_attributes?.get(i)?.id!!
            mapChecklist["work_order[wo_checklists_attributes][$i][done]"] =
                workorder?.wo_checklists_attributes?.get(i)?.done.toString()
        }

        val mapContributor = HashMap<String, String>()
        for (i in workorder?.contributors?.indices!!) {
            mapContributor["work_order[contributors][$i][id]"] =
                workorder?.contributors?.get(i)?.id!!
            mapContributor["work_order[contributors][$i][hours_spent]"] =
                workorder?.contributors?.get(i)?.hours_spent!!
        }

        val mapCost = HashMap<String, String>()
        for (i in workorder?.costs?.indices!!) {
            mapCost["work_order[costs][$i][title]"] = workorder?.costs?.get(i)?.title!!
            mapCost["work_order[costs][$i][value]"] = workorder?.costs?.get(i)?.value!!
        }

        val filePart = MultipartBody.Part.createFormData(
            "work_order[docs_attributes][1538183123520][document]",
            myFile!!.name,
            myFile.asRequestBody("image/jpg".toMediaTypeOrNull())
        )
        progressBarWorkOrderCompleteSignature?.visibility = View.VISIBLE
        workorder?.let { workorder ->
            workOrderCompleteSignatureActivityViewModel.updateWorkOrderDone(
                workorder,
                mapSparePartHavingWorkOrderSparePartId,
                mapSparePartHavingOnlySparePartId,
                mapChecklist,
                mapContributor,
                mapCost,
                filePart
            )
        }
        setObservers()
    }

    private fun setObservers() {
        workOrderCompleteSignatureActivityViewModel.workOrderSignatureObservable.observe(this) {
            val apiResponse = it.value
            progressBarWorkOrderCompleteSignature?.visibility = View.GONE
            WorkOrderCompleteActivity.activity.finish()
            Toast.makeText(
                this@WorkOrderCompleteSignatureActivity,
                resources.getString(R.string.successfully_updated),
                Toast.LENGTH_SHORT
            ).show()
            val i = Intent(
                this@WorkOrderCompleteSignatureActivity,
                WorkOrderDetailActivity::class.java
            )
            i.putExtra(
                WorkOrderDetailActivity.SELECTED_WORK_ORDER,
                apiResponse as Serializable?
            )
            startActivity(i)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.draw_signature_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_tick ->
                if (isSigned) {
                    onSave()
                } else {
                    Toast.makeText(
                        this@WorkOrderCompleteSignatureActivity,
                        getString(R.string.a_signature_is_required),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
//        ProgressDialogProvider.dismiss()
        super.onDestroy()
    }

    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        fun verifyStoragePermissions(activity: Activity) {
            val permission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }
}
