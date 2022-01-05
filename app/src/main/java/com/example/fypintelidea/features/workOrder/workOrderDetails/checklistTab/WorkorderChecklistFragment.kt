package com.example.fypintelidea.features.workOrder.workOrderDetails.checklistTab

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fypintelidea.core.Constants
import com.example.fypintelidea.core.MaxDocumentAttachmentCounts
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.core.providers.models.ChklistItem.Companion.ITEM_TYPE_CHECKBOX
import com.example.fypintelidea.core.providers.models.ChklistItem.Companion.ITEM_TYPE_MULTI_SELECT
import com.example.fypintelidea.core.providers.models.ChklistItem.Companion.ITEM_TYPE_NUMBERFIELD
import com.example.fypintelidea.core.providers.models.ChklistItem.Companion.ITEM_TYPE_RANGE
import com.example.fypintelidea.core.providers.models.ChklistItem.Companion.ITEM_TYPE_SCORE
import com.example.fypintelidea.core.providers.models.ChklistItem.Companion.ITEM_TYPE_SINGLE_SELECT
import com.example.fypintelidea.core.providers.models.ChklistItem.Companion.ITEM_TYPE_TEXTFIELD
import com.example.fypintelidea.core.providers.models.Workorder.Companion.WORKORDER_STATUS_DONE
import com.example.fypintelidea.core.services.ApiClient
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.utils.AssetsUtility
import com.example.fypintelidea.core.utils.ImageTreatmentActivity
import com.example.fypintelidea.core.views.CustomImageViewCancelable
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseFragment
import com.example.fypintelidea.core.providers.models.Asset
import com.example.fypintelidea.core.providers.models.ChklistItem
import com.example.fypintelidea.core.providers.models.ChklistSection
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.core.utils.Utils
import com.example.fypintelidea.features.workOrder.CheckListImageAdapter
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.fragment_workorder_checklist.*
import kotlinx.android.synthetic.main.item_check_list_item.*
import kotlinx.android.synthetic.main.item_section.*
import kotlinx.android.synthetic.main.layout_checklist_question_types.*
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class WorkorderChecklistFragment : ConnectavoBaseFragment() {
    private var mWorkOrder: Workorder? = null
    private var waitingForSectionScanQRResult: ChklistSection? = null
    private var waitingForImageResult: ChklistItem? = null
    private var waitingForScanQRResult: ChklistItem? = null
    internal var totalAssets: List<Asset> = AssetsUtility.assetListHigh
    var assetNames: ArrayList<String> = ArrayList()
    private var uniqueChecklistSectionId = 0
    private var uniqueChecklistItemId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workorder_checklist, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val sectionsContainer = view.findViewById<LinearLayout>(R.id.sectionsContainer)
        val bSave = view.findViewById<Button>(R.id.bSave)
        activity?.let {
            mWorkOrder = arguments?.getSerializable(SELECTED_WORK_ORDER) as Workorder?
            mWorkOrder?.let { workorder ->

                if (workorder.chklistSections != null) {
                    if (workorder.chklistSections?.isNotEmpty() == true) {

                        for (k in 0 until workorder.chklistSections?.size!!) {
                            bSave.visibility = View.VISIBLE
                            val chklistSection = workorder.chklistSections?.get(k)
                            if (chklistSection != null) {
                                chklistSection.intID = ++uniqueChecklistSectionId
                                val itemContainerInsideOneSection =
                                    addSectionUI(inflater, chklistSection)
                                sectionsContainer.addView(
                                    itemContainerInsideOneSection
                                )
                                itemContainerInsideOneSection?.tag =
                                    "itemContainerInsideOneSection" + chklistSection.id

                                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                /////////////////////////////////////////////////////////////////////CHECKLIST ITEMS/////////////////////////////////////////////////////

                                if (chklistSection.chklistItem != null && chklistSection.chklistItem!!.isNotEmpty()) {
                                    for (j in 0 until chklistSection.chklistItem?.size!!) {
                                        val myChklist = chklistSection.chklistItem!![j]
                                        myChklist.intID = ++uniqueChecklistItemId

                                        val llItemCheckListContainer =
                                            itemContainerInsideOneSection?.findViewById<LinearLayout>(
                                                R.id.llItemCheckListContainer
                                            )
                                        llItemCheckListContainer?.addView(
                                            addCheckListItemUI(inflater, myChklist)
                                        )
                                        llItemCheckListContainer?.tag =
                                            "llItemCheckListContainer" + chklistSection.id
                                    }
                                }

                                if (chklistSection.isRequireScanQr) {
                                    if (!chklistSection.isVerified) {
                                        disableEnableControls(
                                            false, itemContainerInsideOneSection as ViewGroup
                                        )
                                    }
                                }
                            }
                        }
                        updateProgressBar()

                        if (mWorkOrder?.status == WORKORDER_STATUS_DONE) {
                            disableEnableControls(false, sectionsContainer)
                        }
                    } else {
                        bSave.visibility = View.GONE
                    }

                    bSave.setOnClickListener {

                        if (workorder.chklistSections != null) {
                            workorder.chklistSections?.forEachIndexed { _, chklistSection ->
                                for (itemIndex in 0 until chklistSection.chklistItem?.size!!) {
                                    val myChklist = chklistSection.chklistItem!![itemIndex]
                                    if (myChklist.isRequired) {
                                        when {
                                            myChklist.item_type.equals(ITEM_TYPE_CHECKBOX) -> if (myChklist.value.isNullOrEmpty()) {
                                                linearLayoutErrorField.visibility = View.VISIBLE
                                            }

                                            myChklist.item_type.equals(ITEM_TYPE_TEXTFIELD) -> if (myChklist.value.isNullOrEmpty()) {
                                                val editTextItemAnswer =
                                                    view.findViewWithTag<EditText>(
                                                        ITEM_TYPE_TEXTFIELD + VALUE + myChklist.id
                                                    )
                                                val linearLayoutErrorField =
                                                    view.findViewWithTag<LinearLayout>(
                                                        ITEM_TYPE_TEXTFIELD + REQUIRED_FIELD + myChklist.id
                                                    )
                                                if (editTextItemAnswer?.text.toString().isEmpty()) {
                                                    linearLayoutErrorField.visibility = View.VISIBLE
                                                    editTextItemAnswer?.error = "error text"
                                                }
                                            }
                                            myChklist.item_type.equals(ITEM_TYPE_NUMBERFIELD) -> if (myChklist.value.isNullOrEmpty()) {
                                                val editTextItemNumber =
                                                    view.findViewWithTag<EditText>(
                                                        ITEM_TYPE_NUMBERFIELD + VALUE + myChklist.id
                                                    )
                                                val linearLayoutErrorField =
                                                    view.findViewWithTag<LinearLayout>(
                                                        ITEM_TYPE_NUMBERFIELD + REQUIRED_FIELD + myChklist.id
                                                    )
                                                if (editTextItemNumber?.text.toString().isEmpty()) {
                                                    linearLayoutErrorField.visibility = View.VISIBLE
                                                    editTextItemNumber?.error = "error number"
                                                }
                                            }
                                            myChklist.item_type.equals(ITEM_TYPE_SCORE) -> if (myChklist.value.isNullOrEmpty()) { //                                                    val score =
                                                //                                                        view.findViewWithTag<TextView>(
                                                //                                                            ITEM_TYPE_SCORE + VALUE + myChklist.id
                                                //                                                        )
                                                val linearLayoutErrorField =
                                                    view.findViewWithTag<LinearLayout>(
                                                        ITEM_TYPE_SCORE + REQUIRED_FIELD + myChklist.id
                                                    )
                                                linearLayoutErrorField.visibility = View.VISIBLE
                                            }

                                            myChklist.item_type.equals(ITEM_TYPE_SINGLE_SELECT) -> if (myChklist.selected_option_ids!!.size == 0) {
                                                val linearLayoutErrorField =
                                                    view.findViewWithTag<LinearLayout>(
                                                        ITEM_TYPE_SINGLE_SELECT + REQUIRED_FIELD + myChklist.id
                                                    )
                                                linearLayoutErrorField.visibility = View.VISIBLE
                                            }

                                            myChklist.item_type.equals(ITEM_TYPE_MULTI_SELECT) -> if (myChklist.selected_option_ids!!.size == 0) { //                                                    val range =
                                                //                                                        view.findViewWithTag<CheckBox>(
                                                //                                                            ITEM_TYPE_MULTI_SELECT + VALUE + myChklist.id
                                                //                                                        )
                                                val linearLayoutErrorField =
                                                    view.findViewWithTag<LinearLayout>(
                                                        ITEM_TYPE_MULTI_SELECT + REQUIRED_FIELD + myChklist.id
                                                    )
                                                linearLayoutErrorField.visibility = View.VISIBLE
                                            }

                                            myChklist.item_type.equals(ITEM_TYPE_RANGE) -> if (myChklist.value.isNullOrEmpty()) {
                                                linearLayoutErrorField.visibility = View.VISIBLE
                                            }
                                        }
                                    }
                                }
                            }

                            submitChklistDataToServer(workorder)
                        }
                    }
                } //////////// done WOs /////////////
                if (workorder.status.equals(
                        WORKORDER_STATUS_DONE, ignoreCase = true
                    )
                ) {
                    bSave.visibility = View.GONE
                }
            }
        }
        setHasOptionsMenu(true)
    }

    private fun addSectionUI(
        inflater: LayoutInflater, chklistSection: ChklistSection
    ): View? {
        val child = inflater.inflate(R.layout.item_section, null)
        val cardViewSectionScanQR = child.findViewById<CardView>(R.id.cardViewSectionScanQR)
        val clTopBarCard = child.findViewById<ConstraintLayout>(R.id.clTopBarCard)
        val ivSectionAsset = child.findViewById<ImageView>(R.id.ivSectionAsset)
        val tvAssetsHierarchy = child.findViewById<TextView>(R.id.tvAssetsHierarchy)
        val tvSectionTitle = child.findViewById<TextView>(R.id.tvSectionTitle)
        val llItemCheckListContainer =
            child.findViewById<LinearLayout>(R.id.llItemCheckListContainer)
        val llTvSectionAssetVerified =
            child.findViewById<ConstraintLayout>(R.id.llTvSectionAssetVerified)
        val llTvSectionAssetNotVerified =
            child.findViewById<ConstraintLayout>(R.id.llTvSectionAssetNotVerified)
        val buttonSectionScanQR = child.findViewById<Button>(R.id.buttonSectionScanQR)

        llTvSectionAssetVerified.tag = "llTvSectionAssetVerified" + chklistSection.id
        llTvSectionAssetNotVerified.tag = "llTvSectionAssetNotVerified" + chklistSection.id
        cardViewSectionScanQR.tag = "cardViewSectionScanQR" + chklistSection.id
        buttonSectionScanQR.tag = "buttonSectionScanQR" + chklistSection.id

        chklistSection.let {
            tvSectionTitle?.text = it.title
            if (chklistSection.isRequireScanQr) {
                if (chklistSection.isVerified) {
                    cardViewSectionScanQR?.visibility = View.GONE
                    llItemCheckListContainer?.alpha = 1.0F
                    llTvSectionAssetVerified?.visibility = View.VISIBLE
                    llTvSectionAssetNotVerified?.visibility = View.GONE
                    cardViewSectionScanQR?.visibility = View.GONE
                } else {
                    cardViewSectionScanQR?.visibility = View.VISIBLE
                    llItemCheckListContainer?.alpha = 0.5F
                    llTvSectionAssetVerified?.visibility = View.GONE
                    llTvSectionAssetNotVerified?.visibility = View.VISIBLE
                }
            } else {
                cardViewSectionScanQR?.visibility = View.GONE
            }

            if (chklistSection.id == null) {
                clTopBarCard?.visibility = View.GONE
            }

            if (chklistSection.assetId == null) {
                ivSectionAsset?.visibility = View.GONE
                tvAssetsHierarchy?.visibility = View.GONE
            }

            tvAssetsHierarchy?.text =
                chklistSection.assetId?.let { Utils.getAssetById(totalAssets, it)?.name }

//            buttonSectionScanQR?.setOnClickListener {
//                waitingForSectionScanQRResult = chklistSection
//                startActivityForResult(
//                    Intent(
//                        activity, QRScanActivity::class.java
//                    ), CHECKLIST_SECTION_REQUEST_CODE
//                )
//            }

            //////////// done WOs /////////////
            if (mWorkOrder?.status == WORKORDER_STATUS_DONE) {
                cardViewSectionScanQR?.visibility = View.GONE
                llItemCheckListContainer?.alpha = 1.0F
            }
        }
        return child
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCheckListItemUI(
        inflater: LayoutInflater, chklistItem: ChklistItem?
    ): View? {
        val child = inflater.inflate(R.layout.item_check_list_item, null)
        activity?.let { activity ->
            val rvCheckList = child.findViewById<RecyclerView>(R.id.rvCheckList)
            val checkBox = child.findViewById<CheckBox>(R.id.checkBox)
            val ivDoc = child.findViewById<ImageView>(R.id.ivDoc)
            val tvDoc = child.findViewById<TextView>(R.id.tvDoc)
            val recyclerViewDocVal = child.findViewById<RecyclerView>(R.id.recyclerViewDocVal)
            val ivArticle = child.findViewById<ImageView>(R.id.ivArticle)
            val tvArticle = child.findViewById<TextView>(R.id.tvArticle)
            val tvItemName = child.findViewById<TextView>(R.id.tvItemName)
            val recyclerViewArticleVal =
                child.findViewById<RecyclerView>(R.id.recyclerViewArticleVal)
            val tvItemCount = child.findViewById<TextView>(R.id.tvItemCount)
            val cardViewItemScanQR = child.findViewById<CardView>(R.id.cardViewItemScanQR)
            val tvInnerItemAsset = child.findViewById<TextView>(R.id.tvInnerItemAsset)
            val clItemCheckListItem = child.findViewById<ConstraintLayout>(R.id.clItemCheckListItem)
            val llTvItemAssetVerified =
                child.findViewById<ConstraintLayout>(R.id.llTvItemAssetVerified)
            val llTvItemAssetNotVerified =
                child.findViewById<ConstraintLayout>(R.id.llTvItemAssetNotVerified)
            val ivAssetItem = child.findViewById<ImageView>(R.id.ivAssetItem)
            val buttonItemScanQR = child.findViewById<Button>(R.id.buttonItemScanQR)
            val linearLayoutErrorField =
                child.findViewById<LinearLayout>(R.id.linearLayoutErrorField)
            val linearLayoutCheckbox = child.findViewById<LinearLayout>(R.id.linearLayoutCheckbox)
            val linearLayoutTextField = child.findViewById<LinearLayout>(R.id.linearLayoutTextField)
            val editTextItemAnswer = child.findViewById<EditText>(R.id.editTextItemAnswer)
            val editTextItemNumber = child.findViewById<EditText>(R.id.editTextItemNumber)
            val linearLayoutNumberField =
                child.findViewById<LinearLayout>(R.id.linearLayoutNumberField)
            val llItemCheckBox = child.findViewById<LinearLayout>(R.id.llItemCheckBox)
            val linearLayoutRadioGroup =
                child.findViewById<LinearLayout>(R.id.linearLayoutRadioGroup)
            val radioGroup = child.findViewById<RadioGroup>(R.id.radioGroup)
            val linearLayoutMultiSelectCheckBox =
                child.findViewById<LinearLayout>(R.id.linearLayoutMultiSelectCheckBox)
            val linearLayoutRange = child.findViewById<LinearLayout>(R.id.linearLayoutRange)
            val seekBar = child.findViewById<SeekBar>(R.id.seekBar)
            val seekBarVal = child.findViewById<TextView>(R.id.seekBarVal)
            val tvMin = child.findViewById<TextView>(R.id.tvMin)
            val tvMax = child.findViewById<TextView>(R.id.tvMax)
            val linearLayoutScore = child.findViewById<LinearLayout>(R.id.linearLayoutScore)
            val tvScore1 = child.findViewById<TextView>(R.id.tvScore1)
            val tvScore2 = child.findViewById<TextView>(R.id.tvScore2)
            val tvScore3 = child.findViewById<TextView>(R.id.tvScore3)
            val tvScore4 = child.findViewById<TextView>(R.id.tvScore4)
            val tvScore5 = child.findViewById<TextView>(R.id.tvScore5)
            val tvScore6 = child.findViewById<TextView>(R.id.tvScore6)
            val itemAddImageButton = child.findViewById<Button>(R.id.itemAddImageButton)
            val recyclerViewImagesAlreadyAdded =
                child.findViewById<RecyclerView>(R.id.recyclerViewImagesAlreadyAdded)
            val gridLayoutImagesToBeAdded =
                child.findViewById<GridLayout>(R.id.gridLayoutImagesToBeAdded)
            val itemSwitchButton = child.findViewById<SwitchCompat>(R.id.itemSwitchButton)
            val itemSwitchButtonText = child.findViewById<TextView>(R.id.itemSwitchButtonText)
            val etItemComment = child.findViewById<EditText>(R.id.etItemComment)

            llTvItemAssetVerified.tag = "llTvItemAssetVerified" + chklistItem?.id
            llTvItemAssetNotVerified.tag = "llTvItemAssetNotVerified" + chklistItem?.id
            clItemCheckListItem.tag = "clItemCheckListItem" + chklistItem?.id
            cardViewItemScanQR.tag = "cardViewItemScanQR" + chklistItem?.id
            buttonItemScanQR.tag = "buttonItemScanQR" + chklistItem?.id

            if (!chklistItem?.assetId.isNullOrEmpty()) {
                ivAssetItem?.visibility = View.VISIBLE
                tvInnerItemAsset?.visibility = View.VISIBLE
                tvInnerItemAsset?.text =
                    chklistItem?.assetId?.let { Utils.getAssetById(totalAssets, it)?.name }
            }
            if (chklistItem?.isRequireQrScan == true) {
                if (!chklistItem.isVerified) {
                    disableEnableControls(
                        false, clItemCheckListItem as ViewGroup
                    )
                }
            }
            if (chklistItem?.isRequireQrScan == true) {
                if (chklistItem.isVerified) {
                    clItemCheckListItem?.alpha = 1.0F
                    llTvItemAssetVerified?.visibility = View.VISIBLE
                    llTvItemAssetNotVerified?.visibility = View.GONE
                } else {
                    cardViewItemScanQR?.visibility = View.VISIBLE
                    clItemCheckListItem?.alpha = 0.5F
                    ivAssetItem?.visibility = View.VISIBLE
                    llTvItemAssetVerified?.visibility = View.GONE
                    llTvItemAssetNotVerified?.visibility = View.VISIBLE
                }
            } else {
                cardViewItemScanQR?.visibility = View.GONE
                clItemCheckListItem?.alpha = 1.0F
            }

            tvItemCount.text = chklistItem?.intID.toString() + "."
            chklistItem?.comment?.let {
                etItemComment.setText(it)
            }

            etItemComment.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence, i: Int, i1: Int, i2: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence, i: Int, i1: Int, i2: Int
                ) {
                    if (etItemComment.text.toString().isNotEmpty()) {
                        chklistItem?.comment = etItemComment.text.toString()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            editTextItemNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence, i: Int, i1: Int, i2: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence, i: Int, i1: Int, i2: Int
                ) {
                    chklistItem?.value = editTextItemNumber.text.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            editTextItemAnswer.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence, i: Int, i1: Int, i2: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence, i: Int, i1: Int, i2: Int
                ) {
                    chklistItem?.value = editTextItemAnswer.text.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            chklistItem?.let {
                if (chklistItem.isRequired) {
                    tvItemName?.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.asterisk_red_8dp, 0
                    )
                }
                tvItemName?.text = it.title
            }
            val adapter = chklistItem?.item_images?.let {
                rvCheckList.visibility = View.VISIBLE
                CheckListImageAdapter(it)
            }
            rvCheckList?.adapter = adapter

            if (!chklistItem?.item_pdfs.isNullOrEmpty()) {
                ivDoc?.visibility = View.VISIBLE
                tvDoc?.visibility = View.VISIBLE
                recyclerViewDocVal?.visibility = View.VISIBLE
                val itemDocAdapter =
                    chklistItem?.item_pdfs?.let { CheckListDocItemAdapter(activity, it) }
                recyclerViewDocVal?.adapter = itemDocAdapter
            } else {
                ivDoc?.visibility = View.GONE
                tvDoc?.visibility = View.GONE
                recyclerViewDocVal?.visibility = View.GONE
            }
            if (!chklistItem?.articles.isNullOrEmpty()) {
                ivArticle?.visibility = View.VISIBLE
                tvArticle?.visibility = View.VISIBLE
                recyclerViewArticleVal?.visibility = View.VISIBLE
                val itemArticleAdapter = chklistItem?.articles?.let { it1 ->
                    CheckListDocItemAdapter(activity, it1)
                }
                recyclerViewArticleVal?.adapter = itemArticleAdapter
            } else {
                ivArticle?.visibility = View.GONE
                tvArticle?.visibility = View.GONE
                recyclerViewArticleVal?.visibility = View.GONE
            }

//            buttonItemScanQR?.setOnClickListener {
//                waitingForScanQRResult = chklistItem
//                startActivityForResult(
//                    Intent(
//                        activity, QRScanActivity::class.java
//                    ), CHECKLIST_ITEM_REQUEST_CODE
//                )
//            }


            when (chklistItem?.item_type) {
                ITEM_TYPE_CHECKBOX -> {
                    linearLayoutCheckbox.visibility = View.VISIBLE
                    checkBox.isChecked =
                        chklistItem.value?.isNotEmpty() == true && chklistItem.value == "1"

                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            chklistItem.value = "1"
                        } else {
                            chklistItem.value = "0"
                        }
                    }
                }
                ITEM_TYPE_TEXTFIELD -> {
                    editTextItemAnswer.tag = ITEM_TYPE_TEXTFIELD + VALUE + chklistItem.id
                    linearLayoutErrorField.tag =
                        ITEM_TYPE_TEXTFIELD + REQUIRED_FIELD + chklistItem.id
                    linearLayoutTextField.visibility = View.VISIBLE
                    if (!chklistItem.value.isNullOrEmpty()) {
                        editTextItemAnswer.setText(chklistItem.value)
                    }
                }
                ITEM_TYPE_NUMBERFIELD -> {
                    linearLayoutNumberField.visibility = View.VISIBLE
                    editTextItemNumber.tag = ITEM_TYPE_NUMBERFIELD + VALUE + chklistItem.id
                    linearLayoutErrorField.tag =
                        ITEM_TYPE_NUMBERFIELD + REQUIRED_FIELD + chklistItem.id
                    if (!chklistItem.value.isNullOrEmpty()) {
                        editTextItemNumber.setText(chklistItem.value)
                    }
                }
                ITEM_TYPE_SINGLE_SELECT -> {
                    linearLayoutRadioGroup.visibility = View.VISIBLE
                    radioGroup.tag = ITEM_TYPE_SINGLE_SELECT + REQUIRED_FIELD + chklistItem.id
                    linearLayoutErrorField.tag =
                        ITEM_TYPE_SINGLE_SELECT + REQUIRED_FIELD + chklistItem.id
                    if (!chklistItem.chklist_item_options.isNullOrEmpty()) {
                        for (o in 0 until chklistItem.chklist_item_options?.size!!) {
                            val btn = RadioButton(activity)
                            btn.text = chklistItem.chklist_item_options!![o].option
                            btn.tag = chklistItem.chklist_item_options!![o].id
                            radioGroup.addView(btn)

                            for (i in 0 until chklistItem.selected_option_ids?.size!!) {
                                if (chklistItem.selected_option_ids?.get(i) == chklistItem.chklist_item_options?.get(
                                        o
                                    )?.id
                                ) {
                                    btn.isChecked = true
                                }
                            }

                            ////single select////
                            radioGroup.setOnCheckedChangeListener { radioGroup1, checkedId ->
                                val radioBtnId = radioGroup1.findViewById<RadioButton>(checkedId)
                                chklistItem.selected_option_ids!!.add(radioBtnId.tag.toString())
                            }
                        }
                    }
                }
                ITEM_TYPE_MULTI_SELECT -> { //                    multiSelectCheckBox.tag =
                    //                        ITEM_TYPE_MULTI_SELECT + REQUIRED_FIELD + chklistItem.id
                    linearLayoutErrorField.tag =
                        ITEM_TYPE_MULTI_SELECT + REQUIRED_FIELD + chklistItem.id
                    linearLayoutMultiSelectCheckBox.visibility = View.VISIBLE
                    if (!chklistItem.chklist_item_options.isNullOrEmpty()) {
                        for (o in 0 until chklistItem.chklist_item_options?.size!!) {
                            val btn = CheckBox(activity)
                            btn.text = chklistItem.chklist_item_options!![o].option
                            btn.tag = chklistItem.chklist_item_options!![o].id
                            llItemCheckBox.addView(btn)

                            for (i in 0 until chklistItem.selected_option_ids?.size!!) {
                                if (chklistItem.selected_option_ids?.get(i) == chklistItem.chklist_item_options?.get(
                                        o
                                    )?.id
                                ) {
                                    btn.isChecked = true
                                }
                            }

                            ////multi select////
                            btn.setOnCheckedChangeListener { _, _ ->
                                if (btn.isChecked) {
                                    chklistItem.selected_option_ids!!.add(btn.tag.toString())
                                } else {
                                    chklistItem.selected_option_ids!!.remove(btn.tag.toString())
                                }
                            }
                        }
                    }
                }
                ITEM_TYPE_RANGE -> {
                    linearLayoutRange.visibility = View.VISIBLE
                    seekBar.tag = ITEM_TYPE_RANGE + VALUE + chklistItem.id
                    linearLayoutErrorField.tag = ITEM_TYPE_RANGE + REQUIRED_FIELD + chklistItem.id
                    chklistItem.chklist_item_range?.let {
                        seekBar.min = it.min
                        seekBar.max = it.max
                        tvMin.text = it.min.toString()
                        tvMax.text = it.max.toString()
                    }

                    if (!chklistItem.value.isNullOrEmpty() && !chklistItem.value.isNullOrBlank()) {
                        chklistItem.value?.let {
                            seekBarVal.text = it
                            seekBar.progress = it.toInt()
                        }
                    }


                    seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                        override fun onStopTrackingTouch(arg0: SeekBar) {
                            val progressChanged = arg0.progress
                            chklistItem.value = progressChanged.toString()
                        }

                        override fun onStartTrackingTouch(arg0: SeekBar) {}

                        override fun onProgressChanged(
                            arg0: SeekBar, progress: Int, fromUser: Boolean
                        ) {
                            seekBarVal?.text = progress.toString()
                        }
                    })
                }
                ITEM_TYPE_SCORE -> {
                    linearLayoutScore.visibility = View.VISIBLE
                    seekBar.tag = ITEM_TYPE_SCORE + VALUE + chklistItem.id
                    linearLayoutErrorField.tag = ITEM_TYPE_SCORE + REQUIRED_FIELD + chklistItem.id
                    if (!chklistItem.value.isNullOrEmpty()) {
                        when (chklistItem.value) {
                            "1" -> {
                                tvScore1.setBackgroundResource(R.drawable.full_round_corners_selected)
                                tvScore1.setTextColor(
                                    ContextCompat.getColor(activity, R.color.md_white)
                                )
                            }
                            "2" -> {
                                tvScore2.setBackgroundResource(R.drawable.full_round_corners_selected)
                                tvScore2.setTextColor(
                                    ContextCompat.getColor(activity, R.color.md_white)
                                )
                            }
                            "3" -> {
                                tvScore3.setBackgroundResource(R.drawable.full_round_corners_selected)
                                tvScore3.setTextColor(
                                    ContextCompat.getColor(activity, R.color.md_white)
                                )
                            }
                            "4" -> {
                                tvScore4.setBackgroundResource(R.drawable.full_round_corners_selected)
                                tvScore4.setTextColor(
                                    ContextCompat.getColor(activity, R.color.md_white)
                                )
                            }
                            "5" -> {
                                tvScore5.setBackgroundResource(R.drawable.full_round_corners_selected)
                                tvScore5.setTextColor(
                                    ContextCompat.getColor(activity, R.color.md_white)
                                )
                            }
                            "6" -> {
                                tvScore6.setBackgroundResource(R.drawable.full_round_corners_selected)
                                tvScore6.setTextColor(
                                    ContextCompat.getColor(activity, R.color.md_white)
                                )
                            }
                        }
                    }
                    tvScore1.setOnClickListener {
                        tvScore1.setBackgroundResource(R.drawable.full_round_corners_selected)
                        tvScore1.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_white)
                        )
                        chklistItem.value = tvScore1.text.toString()

                        tvScore2.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore2.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore3.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore3.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore4.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore4.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore5.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore5.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore6.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore6.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                    }
                    tvScore2.setOnClickListener {
                        tvScore2.setBackgroundResource(R.drawable.full_round_corners_selected)
                        tvScore2.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_white)
                        )
                        chklistItem.value = tvScore2.text.toString()

                        tvScore1.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore1.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore3.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore3.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore4.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore4.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore5.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore5.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore6.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore6.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                    }
                    tvScore3.setOnClickListener {
                        tvScore3.setBackgroundResource(R.drawable.full_round_corners_selected)
                        tvScore3.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_white)
                        )
                        chklistItem.value = tvScore3.text.toString()

                        tvScore1.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore1.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore2.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore2.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore4.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore4.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore5.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore5.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore6.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore6.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                    }
                    tvScore4.setOnClickListener {
                        tvScore4.setBackgroundResource(R.drawable.full_round_corners_selected)
                        tvScore4.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_white)
                        )
                        chklistItem.value = tvScore4.text.toString()

                        tvScore1.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore1.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore2.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore2.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore3.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore3.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore5.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore5.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore6.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore6.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                    }
                    tvScore5.setOnClickListener {
                        tvScore5.setBackgroundResource(R.drawable.full_round_corners_selected)
                        tvScore5.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_white)
                        )
                        chklistItem.value = tvScore5.text.toString()

                        tvScore1.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore1.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore2.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore2.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore3.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore3.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore4.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore4.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore6.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore6.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                    }
                    tvScore6.setOnClickListener {
                        tvScore6.setBackgroundResource(R.drawable.full_round_corners_selected)
                        tvScore6.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_white)
                        )
                        chklistItem.value = tvScore6.text.toString()

                        tvScore1.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore1.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore2.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore2.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore3.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore3.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore4.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore4.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                        tvScore5.setBackgroundResource(R.drawable.full_round_corners)
                        tvScore5.setTextColor(
                            ContextCompat.getColor(activity, R.color.md_dark_green)
                        )
                    }
                }
            }

            recyclerViewImagesAlreadyAdded?.adapter = chklistItem?.images?.let {
                recyclerViewImagesAlreadyAdded?.visibility = View.VISIBLE
                CheckListImageAdapter(it)
            }

            if (chklistItem?.isAdd_images == true) {
                itemAddImageButton?.visibility = View.VISIBLE
                gridLayoutImagesToBeAdded?.visibility = View.VISIBLE
                itemAddImageButton.setOnClickListener {
                    waitingForImageResult = chklistItem
                    gridLayoutImagesToBeAdded?.tag = GL_IMAGE + chklistItem.id
                    requestPickPhoto(chklistItem)
                }
            }

            if (chklistItem?.isCreate_request == true) {
                itemSwitchButton?.visibility = View.VISIBLE
                itemSwitchButtonText?.visibility = View.VISIBLE
                if (chklistItem.isCreate_request_on_complete) {
                    itemSwitchButton.isChecked = true
                }
                itemSwitchButton.setOnCheckedChangeListener { _, isChecked ->
                    chklistItem.isCreate_request_on_complete = isChecked
                }
            } //////////// done WOs /////////////
            if (mWorkOrder?.status == WORKORDER_STATUS_DONE) {
                cardViewItemScanQR?.visibility = View.GONE
                clItemCheckListItem?.alpha = 1.0F
            }
        }
        return child
    }

    private fun updateProgressBar() {
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)
        val totalCount = mWorkOrder?.chklistSections?.let { Utils.getTotalChecklistItemsCount(it) }
        val filledCount =
            mWorkOrder?.chklistSections?.let { Utils.getFilledChecklistItemsCount(it) }
        if (totalCount != null) {
            progressBar?.max = totalCount
        }
        view?.findViewById<TextView>(R.id.tvCountFilled)?.text =
            "$filledCount" + Constants.SPACE_STRING + getString(R.string.of) + Constants.SPACE_STRING + "$totalCount" + Constants.SPACE_STRING + getString(
                R.string.completed
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (filledCount != null) {
                progressBar?.setProgress(filledCount, true)
            }
        } else {
            if (filledCount != null) {
                progressBar?.progress = filledCount
            }
        }
    }

    private fun onPickPhoto(checklistItem: ChklistItem?) {
        var photoPaths = ArrayList<String>()
        if (checklistItem!!.photoPaths != null) {
            photoPaths = checklistItem.photoPaths as ArrayList<String>
        }
        val maxCount =
            MaxDocumentAttachmentCounts.MAX_ATTACHMENT_COUNT_CHECKLIST - checklistItem.images!!.size
        if (photoPaths.size + checklistItem.images!!.size >= MaxDocumentAttachmentCounts.MAX_ATTACHMENT_COUNT_CHECKLIST) {
            Toast.makeText(
                activity,
                getString(R.string.Max) + Constants.SPACE_STRING + MaxDocumentAttachmentCounts.MAX_ATTACHMENT_COUNT_CHECKLIST + Constants.SPACE_STRING + getString(
                    R.string.images_allowed
                ),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            FilePickerBuilder.instance.setMaxCount(maxCount)
                .setSelectedFiles(Utils.getArrayOfUrisFromArrayOfStrings(photoPaths))
                .setActivityTheme(R.style.FilePickerTheme)
                .setActivityTitle(getString(R.string.please_select_media)).enableVideoPicker(false)
                .enableCameraSupport(true).showGifs(true).showFolderView(false)
                .enableSelectAll(false).enableImagePicker(true)
                .setCameraPlaceholder(R.drawable.custom_camera).pickPhoto(this, checklistItem.intID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            activity?.let { activity ->
                if (waitingForImageResult != null) {
                    val gridLayoutImagesToBeAdded = requireView().findViewWithTag<GridLayout>(
                        GL_IMAGE + waitingForImageResult?.id
                    )
                    gridLayoutImagesToBeAdded?.let {
                        val photoPaths =
                            data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                        if (photoPaths != null) {
                            populateReceivedPhotoAndAddListener(
                                Utils.getArrayOfStringsFromArrayOfUris(activity, photoPaths),
                                gridLayoutImagesToBeAdded
                            )
                        }
                    }
                }

//                if (waitingForSectionScanQRResult != null) {
//                    val bundle = data.extras
//                    if (bundle != null) {
//                        val scannedId = bundle.getString(QRScanActivity.ID)
//                        if (scannedId != null && waitingForSectionScanQRResult?.assetId != null) {
//                            val asset = getAssetById(totalAssets, scannedId)
//                            if (waitingForSectionScanQRResult!!.assetId == asset?.id) { // update state to server as well. i.e isVerified = true api
//                                updateChecklistSectionAssetVerificationToServer(
//                                    waitingForSectionScanQRResult ?: return,
//                                    waitingForSectionScanQRResult?.assetId!!
//                                )
//                            } else {
//                                Toast.makeText(
//                                    activity,
//                                    resources.getString(R.string.wrong_qr_code),
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//                    }
//                }

//                if (waitingForScanQRResult != null) {
//                    val bundle = data.extras
//                    if (bundle != null) {
//                        val scannedId = bundle.getString(QRScanActivity.ID)
//                        if (scannedId != null && waitingForScanQRResult?.assetId != null) {
//                            val asset = getAssetById(totalAssets, scannedId)
//                            if (waitingForScanQRResult?.assetId == asset?.id) { //update state to server as well . i . e isVerified = true api
//                                updateChecklistItemAssetVerificationToServer(
//                                    waitingForScanQRResult ?: return,
//                                    waitingForScanQRResult?.assetId!!
//                                )
//                            } else {
//                                Toast.makeText(
//                                    activity,
//                                    resources.getString(R.string.wrong_qr_code),
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    private fun enableSection(section: ChklistSection) {
        section.chklistItem?.forEach {
            if (it.isRequireQrScan) {
                val llScanQr = ll?.findViewWithTag<CardView>("cardViewItemScanQR" + it.id)
                if (llScanQr != null) {
                    disableEnableControls(true, llScanQr)
                }
                return@forEach
            }

            val llItemContain =
                ll!!.findViewWithTag<ConstraintLayout>("clItemCheckListItem" + it.id)
            if (llItemContain != null) {
                enableControls(llItemContain)
            }

        }
    }

    private fun enableControls(vg: ViewGroup) {
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            child.isEnabled = true
            if (child is ViewGroup) {
                enableControls(child)
            }
        }
    }

    private fun disableChecklistItem(checklistItem: ChklistItem, vg: ViewGroup) {
        disableEnableControls(false, vg)

        val llTvRequireScan =
            vg.findViewWithTag<LinearLayout>(TV_REQUIRE_SCAN + checklistItem.id)
        disableEnableControls(true, llTvRequireScan)

        val llScanQr = vg.findViewWithTag<LinearLayout>(BUTTON_SCAN_QR + checklistItem.intID)
        disableEnableControls(true, llScanQr)
    }

    private fun disableEnableControls(enable: Boolean, vg: ViewGroup) {
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i) // Don't disable scan qr button
            if (child.id == R.id.buttonSectionScanQR) continue

            child.isEnabled = enable
            if (child is ViewGroup) {
                disableEnableControls(enable, child)
            }
        }
    }

    @AfterPermissionGranted(RC_FILE_PICKER_PERM)
    private fun requestPickPhoto(checklistItem: ChklistItem) {
        val perms = arrayOf(Manifest.permission.CAMERA, FilePickerConst.PERMISSIONS_FILE_PICKER)
        if (activity?.let { EasyPermissions.hasPermissions(it, *perms) } == true) {
            onPickPhoto(checklistItem)
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_doc_picker),
                RC_FILE_PICKER_PERM,
                FilePickerConst.PERMISSIONS_FILE_PICKER,
                Manifest.permission.CAMERA
            )
        }
    }

    private fun populateReceivedPhotoAndAddListener(
        filePaths: ArrayList<String>, gridLayout: GridLayout
    ) {
        waitingForImageResult?.photoPaths = filePaths
        gridLayout.removeAllViews()
        if (!filePaths.isNullOrEmpty()) {
            for (j in filePaths.indices) {
                val path = filePaths[j] //it contain your path of image..im using a temp string..
                val customIV = CustomImageViewCancelable(activity)
                customIV.setmImgPhoto(R.drawable.image_placeholder)
                customIV.setmImgFromPath(path)
                customIV.setOnClickListener {
                    val intent = Intent(activity, ImageTreatmentActivity::class.java)
                    intent.putExtra(ImageTreatmentActivity.IMAGE_PATH, path)
                    startActivity(Intent(intent))
                }
                customIV.getmBtnClose().setOnClickListener {
                    filePaths.remove(filePaths[j])
                    gridLayout.removeAllViews()
                    populateReceivedPhotoAndAddListener(filePaths, gridLayout)
                }
                gridLayout.addView(customIV)
            }
        }
    }

    private fun submitChklistDataToServer(workOrder: Workorder) {
        activity?.let { activity ->
            val mapChecklist = HashMap<String, String>()
            if (workOrder.chklistSections != null) {
                workOrder.chklistSections?.forEachIndexed { sectionIndex, chklistSection ->

                    for (i in 0 until chklistSection.chklistItem?.size!!) {

                        if (chklistSection.chklistItem!![i].item_type.equals(
                                ITEM_TYPE_CHECKBOX, ignoreCase = true
                            ) || chklistSection.chklistItem!![i].item_type.equals(
                                ITEM_TYPE_TEXTFIELD, ignoreCase = true
                            ) || chklistSection.chklistItem!![i].item_type.equals(
                                ITEM_TYPE_NUMBERFIELD, ignoreCase = true
                            ) || chklistSection.chklistItem!![i].item_type.equals(
                                ITEM_TYPE_SCORE, ignoreCase = true
                            ) || chklistSection.chklistItem!![i].item_type.equals(
                                ITEM_TYPE_RANGE, ignoreCase = true
                            )
                        ) {

                            if (chklistSection.chklistItem!![i].id != null) {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][id]"] =
                                    chklistSection.chklistItem!![i].id!!
                            }

                            if (chklistSection.chklistItem!![i].value != null) {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][value]"] =
                                    java.lang.String.valueOf(chklistSection.chklistItem!![i].value)
                            } else {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][value]"] =
                                    ""
                            }

                            if (chklistSection.chklistItem!![i].comment != null) {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][comment]"] =
                                    java.lang.String.valueOf(chklistSection.chklistItem!![i].comment)
                            } else {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][comment]"] =
                                    ""
                            }

                            mapChecklist["chklist_section[$sectionIndex][items][$i][create_request_on_complete]"] =
                                java.lang.String.valueOf(chklistSection.chklistItem!![i].isCreate_request_on_complete)

                            chklistSection.chklistItem!![i].docs_attributes_base64 =
                                Utils.getBase64StringFromPhotoPath(chklistSection.chklistItem!![i].photoPaths)
                            if (chklistSection.chklistItem!![i].docs_attributes_base64 != null) {
                                for (j in 0 until chklistSection.chklistItem!![i].docs_attributes_base64?.size!!) {
                                    mapChecklist["chklist_section[$sectionIndex][items][$i][images][$j]"] =
                                        chklistSection.chklistItem!![i].docs_attributes_base64!![j]
                                }
                            }

                        } else if (chklistSection.chklistItem!![i].item_type.equals(
                                ITEM_TYPE_SINGLE_SELECT, ignoreCase = true
                            ) || chklistSection.chklistItem!![i].item_type.equals(
                                ITEM_TYPE_MULTI_SELECT, ignoreCase = true
                            )
                        ) {

                            if (chklistSection.chklistItem!![i].id != null) {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][id]"] =
                                    chklistSection.chklistItem!![i].id!!
                            }

                            if (chklistSection.chklistItem!![i].selected_option_ids != null) {
                                var ids = StringBuilder()
                                for (j in 0 until chklistSection.chklistItem!![i].selected_option_ids?.size!!) {
                                    ids.insert(
                                        0,
                                        chklistSection.chklistItem!![i].selected_option_ids!![j] + ","
                                    )
                                }
                                if (ids.length > 1) {
                                    ids = StringBuilder(ids.substring(0, ids.length - 1))
                                }
                                mapChecklist["chklist_section[$sectionIndex][items][$i][selected_option_ids]"] =
                                    ids.toString()
                            } else {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][selected_option_ids]"] =
                                    ""
                            }

                            if (chklistSection.chklistItem!![i].comment != null) {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][comment]"] =
                                    java.lang.String.valueOf(chklistSection.chklistItem!![i].comment)
                            } else {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][comment]"] =
                                    ""
                            }

                            mapChecklist["chklist_section[$sectionIndex][items][$i][create_request_on_complete]"] =
                                java.lang.String.valueOf(chklistSection.chklistItem!![i].isCreate_request_on_complete)

                            chklistSection.chklistItem!![i].docs_attributes_base64 =
                                Utils.getBase64StringFromPhotoPath(chklistSection.chklistItem!![i].photoPaths)
                            if (chklistSection.chklistItem!![i].docs_attributes_base64 != null) {
                                for (j in 0 until chklistSection.chklistItem!![i].docs_attributes_base64?.size!!) {
                                    mapChecklist["chklist_section[$sectionIndex][items][$i][images][$j]"] =
                                        chklistSection.chklistItem!![i].docs_attributes_base64!![j]
                                }
                            }

                        }
                    }
                }
            }
            progressBarWorkOrderCheckList.visibility = View.VISIBLE
            workOrder.chklist_id?.let {
                ApiClient.submitChklistMultipart(activity, this, it, mapChecklist)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateChecklistSectionAssetVerificationToServer(
        section: ChklistSection, selectedAssetId: String
    ) {
        activity?.let { activity ->
            if (isAdded) {
                val sectionId = section.id!!.toRequestBody("text/plain".toMediaTypeOrNull())
                val assetId = selectedAssetId.toRequestBody("text/plain".toMediaTypeOrNull())

                progressBarWorkOrderCheckList?.visibility = View.VISIBLE
                ApiClient.submitChecklistSectionAssetVerification(
                    activity, this, sectionId, assetId
                )
            }
        }
    }

    private fun updateChecklistItemAssetVerificationToServer(
        item: ChklistItem, selectedAssetId: String
    ) {
        activity?.let { activity ->
            if (isAdded) {
                val itemId = item.id!!.toRequestBody("text/plain".toMediaTypeOrNull())
                val assetId = selectedAssetId.toRequestBody("text/plain".toMediaTypeOrNull())
                progressBarWorkOrderCheckList?.visibility = View.VISIBLE
                ApiClient.submitChecklistItemAssetVerification(activity, this, itemId, assetId)
            }
        }
    }

    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse, errorCode: Int, hearders: Headers
    ) {
        super.onApiSuccess(apiResponse, errorCode, hearders)

        progressBarWorkOrderCheckList?.visibility = View.GONE
        if (apiResponse is ChklistSection) {
            progressBarWorkOrderCheckList?.visibility = View.GONE
            if (errorCode == 200) {
                waitingForSectionScanQRResult?.isVerified = apiResponse.isVerified
                waitingForSectionScanQRResult?.let {
                    enableSection(it)
                }
                val llTvSectionAssetVerified =
                    view?.findViewWithTag<ConstraintLayout>("llTvSectionAssetVerified" + apiResponse.id)
                val llTvSectionAssetNotVerified =
                    view?.findViewWithTag<ConstraintLayout>("llTvSectionAssetNotVerified" + apiResponse.id)
                val llItemCheckListContainer =
                    view?.findViewWithTag<LinearLayout>("llItemCheckListContainer" + apiResponse.id)
                val cardViewSectionScanQR =
                    view?.findViewWithTag<CardView>("cardViewSectionScanQR" + apiResponse.id)

                if (llTvSectionAssetVerified != null && llItemCheckListContainer != null && cardViewSectionScanQR != null) {
                    llTvSectionAssetVerified.visibility = View.VISIBLE
                    llTvSectionAssetNotVerified?.visibility = View.GONE
                    llItemCheckListContainer.alpha = 1.0F
                    cardViewSectionScanQR.visibility = View.GONE
                }
                waitingForSectionScanQRResult = null
            }


        } else if (apiResponse is ChklistItem) {
            progressBarWorkOrderCheckList?.visibility = View.GONE
            if (errorCode == 200) {
                waitingForScanQRResult?.isVerified = apiResponse.isVerified

                val llTvItemAssetVerified =
                    view?.findViewWithTag<ConstraintLayout>("llTvItemAssetVerified" + apiResponse.id)
                val llTvItemAssetNotVerified =
                    view?.findViewWithTag<ConstraintLayout>("llTvItemAssetNotVerified" + apiResponse.id)
                val clItemCheckListItem =
                    view?.findViewWithTag<ConstraintLayout>("clItemCheckListItem" + apiResponse.id)
                val cardViewItemScanQR =
                    view?.findViewWithTag<CardView>("cardViewItemScanQR" + apiResponse.id)

                if (clItemCheckListItem != null) {
                    disableEnableControls(true, clItemCheckListItem)
                }

                if (llTvItemAssetVerified != null && clItemCheckListItem != null && cardViewItemScanQR != null) {
                    llTvItemAssetVerified.visibility = View.VISIBLE
                    llTvItemAssetNotVerified?.visibility = View.GONE
                    clItemCheckListItem.alpha = 1.0F
                    cardViewItemScanQR.visibility = View.GONE
                }
                waitingForScanQRResult = null
            }
        } else if (apiResponse is Workorder) {
            progressBarWorkOrderCheckList?.visibility = View.GONE
            if (errorCode == 200) {
                apiResponse.chklistSections?.forEachIndexed { _, oneSectionTemp ->
                    oneSectionTemp.chklistItem?.forEachIndexed { _, oneChklistItemTemp ->
                        mWorkOrder?.chklistSections?.forEachIndexed { _, oneSection ->
                            oneSection.chklistItem?.forEachIndexed { _, oneChklistItem ->
                                if (oneChklistItemTemp.id == oneChklistItem.id) {
                                    oneChklistItem.images =
                                        oneChklistItemTemp.images // set them to null so that it does not resync on pressing save button again.
                                    oneChklistItem.docs_attributes_base64 = null
                                    oneChklistItem.photoPaths = null
                                }
                            }
                        }
                    }
                }

                if (!apiResponse.status.equals("500", ignoreCase = true)) {
                    progressBarWorkOrderCheckList?.visibility = View.GONE
                    if (isAdded) {
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.successfully_updated),
                            Toast.LENGTH_SHORT
                        ).show()
                        updateProgressBar()
                    }
                }
            }
        } else {
            Toast.makeText(activity, "Unknown api response received", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onApiFailure(errorCode: Int) {
        super.onApiFailure(errorCode)
        progressBarWorkOrderCheckList?.visibility = View.GONE
        when (errorCode) {
            422 -> Toast.makeText(
                activity, resources.getString(R.string.something_wrong) + "422", Toast.LENGTH_SHORT
            ).show()
            401 -> Toast.makeText(
                activity, "Privileges changed, please re-login the app", Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        fun newInstance(workOrder: Workorder) = WorkorderChecklistFragment().apply {
            val args = Bundle()
            args.putSerializable(SELECTED_WORK_ORDER, workOrder)
            arguments = args
        }

        private const val RC_FILE_PICKER_PERM = 321
        private const val CHECKLIST_SECTION_REQUEST_CODE: Int = 600
        private const val CHECKLIST_ITEM_REQUEST_CODE: Int = 601
        private const val TV_SECTION_ASSET_VERIFIED = "tvSectionAssetVerified"
        private const val TV_SECTION_SCAN_QR = "tvSectionScanQr"
        private const val TV_SECTION_REQUIRE_SCAN = "tvSectionRequireScan"
        private const val TV_REQUIRE_SCAN = "tvRequireScan"
        private const val GL_DOCUMENT = "gl_document"
        private const val GL_ARTICLE = "gl_article"
        private const val GL_IMAGE = "gl_image"
        private const val BUTTON_SCAN_QR = "buttonScanQr"
        private const val TV_ASSET_VERIFIED = "tvAssetVerified"
        private const val CARD_VIEW = "card_view"
        private const val LINEAR_LAYOUT_CHECKLIST_ITEM = "checklist_item"
        private const val REQUIRED_FIELD = "required_field"
        private const val VALUE = "value"
        const val SELECTED_WORK_ORDER = "selected_work_order"
    }
}