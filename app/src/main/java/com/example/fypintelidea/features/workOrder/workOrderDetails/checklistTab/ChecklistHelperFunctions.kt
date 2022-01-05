package com.example.fypintelidea.features.workOrder.workOrderDetails.checklistTab

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Asset
import com.example.fypintelidea.core.providers.models.ChklistItem
import com.example.fypintelidea.core.providers.models.ChklistSection
import com.example.fypintelidea.core.utils.Utils

object ChecklistHelperFunctions {

    fun getTvNumbering(activity: Context, num: Int): TextView {
        val tvNumbering = TextView(activity)
        tvNumbering.text = num.toString()
        tvNumbering.minWidth = 50
        tvNumbering.setTextColor(Color.BLACK)
        tvNumbering.textSize = 16f
        tvNumbering.typeface = ResourcesCompat.getFont(activity, R.font.inter_regular)
        tvNumbering.setPadding(0, 0, 16, 0)
        return tvNumbering
    }

     fun getllTvAddImageRow(activity: Context, chklistItem: ChklistItem): View {
        val llTvAddImageRow = LinearLayout(activity)
        llTvAddImageRow.orientation = LinearLayout.HORIZONTAL
        val lpAddImageRow = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lpAddImageRow.setMargins(40, 16, 40, 16)
        llTvAddImageRow.layoutParams = lpAddImageRow

        val tvImageLabel = getTextViewImageLabel(activity)
        val lltvImageLabel = LinearLayout(activity)
        val lpImageLabel = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        lltvImageLabel.layoutParams = lpImageLabel
        lltvImageLabel.addView(tvImageLabel)
        llTvAddImageRow.addView(lltvImageLabel)

        val tvImage = getTextViewImage(activity, chklistItem?.id!!)
        val lltvImage = LinearLayout(activity)
        lltvImage.gravity = Gravity.END
        val lpImage = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        lltvImage.layoutParams = lpImage
        lltvImage.addView(tvImage)
        llTvAddImageRow.addView(lltvImage)
        return llTvAddImageRow
    }

    fun getTextViewImageLabel(activity: Context): View {
        val tvImage = TextView(activity)
        tvImage.text = activity.resources.getString(R.string.images)
        tvImage.minWidth = 50
        tvImage.textSize = 15f
        tvImage.setTypeface(ResourcesCompat.getFont(activity, R.font.inter_bold), Typeface.BOLD)
        tvImage.setPadding(0, 0, 0, 0)
        tvImage.gravity = Gravity.START or Gravity.CENTER_VERTICAL
        return tvImage
    }

    fun getTextViewImage(activity: Context, tag: String): View {
        val tvImage = TextView(activity)
        tvImage.text = activity.resources.getString(R.string.add_image)
        tvImage.tag = tag
        tvImage.minWidth = 50
        tvImage.setTextColor(
            ContextCompat.getColor(
                activity.applicationContext, R.color.Color_Primary
            )
        )
        tvImage.textSize = 12f
        tvImage.setTypeface(ResourcesCompat.getFont(activity, R.font.inter_bold), Typeface.BOLD)
        tvImage.setPadding(0, 0, 0, 0)
        tvImage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_image, 0, 0, 0)
        tvImage.gravity = Gravity.CENTER
        tvImage.compoundDrawablePadding = 10
        return tvImage
    }

    fun getllAsset(activity: Context, chklistSection: ChklistSection, totalAssets: List<Asset>): View {
        var assetName = ""
        chklistSection.assetId?.let {
            assetName = Utils.getAssetById(totalAssets, it)?.name ?: ""
        }
        val tvSectionAsset = TextView(activity)
        tvSectionAsset.text = assetName
        tvSectionAsset.textSize = 10f
        tvSectionAsset.setTextColor(Color.GRAY)
        tvSectionAsset.setTypeface(
            ResourcesCompat.getFont(activity, R.font.inter_bold), Typeface.BOLD
        )
        tvSectionAsset.minWidth = 100
        tvSectionAsset.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_asset_small, 0, 0, 0)
        tvSectionAsset.gravity = Gravity.CENTER
        val llSectionAsset = LinearLayout(activity)
        val lpSectionAsset = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lpSectionAsset.setMargins(16, 8, 40, 8)
        llSectionAsset.layoutParams = lpSectionAsset
        llSectionAsset.addView(tvSectionAsset)
        if (assetName == "") {
            llSectionAsset.visibility = View.GONE
        }
        return llSectionAsset
    }

    fun getllAsset(activity: Context, chklistItem: ChklistItem, totalAssets: List<Asset>): View {
        var assetName = ""
        chklistItem.assetId?.let { assetName = Utils.getAssetById(totalAssets, it)?.name ?: "" }
        val tvAsset = TextView(activity)
        tvAsset.text = assetName
        tvAsset.textSize = 10f
        tvAsset.setTextColor(Color.GRAY)
        tvAsset.setTypeface(
            ResourcesCompat.getFont(activity, R.font.inter_bold), Typeface.BOLD
        )
        tvAsset.minWidth = 100
        tvAsset.setPadding(0, 10, 0, 0)
        tvAsset.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_asset_small, 0, 0, 0)
        tvAsset.gravity = Gravity.CENTER
        val llSectionAsset = LinearLayout(activity)
        val lpSectionAsset = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lpSectionAsset.setMargins(54, 8, 40, 8)
        llSectionAsset.layoutParams = lpSectionAsset
        llSectionAsset.addView(tvAsset)
        if (assetName == "") {
            llSectionAsset.visibility = View.GONE
        }
        return llSectionAsset
    }

    fun getSectionTvRequireScan(
        activity: FragmentActivity, checklistSection: ChklistSection
    ): View {
        val longDescription = SpannableStringBuilder()
        longDescription.append(activity.resources.getString(R.string.please_scan_QR_code_of_asset_to_answer_this_section))
        val tvRequireScan = TextView(activity)
        tvRequireScan.text = longDescription
        tvRequireScan.textSize = 16f
        tvRequireScan.setTextColor(Color.BLACK)
        tvRequireScan.typeface = ResourcesCompat.getFont(activity, R.font.inter_regular)
        tvRequireScan.minWidth = 100
        tvRequireScan.setPadding(0, 10, 0, 0)
        return tvRequireScan
    }

    fun getTvRequireScan(activity: Context, checklistItem: ChklistItem): View {
        val tvRequireScan = TextView(activity)
        tvRequireScan.text =
            activity.resources.getString(R.string.please_scan_QR_code_of_asset_to_answer_this_question)
        tvRequireScan.textSize = 14f
        tvRequireScan.setTextColor(Color.BLACK)
        tvRequireScan.typeface = ResourcesCompat.getFont(activity, R.font.inter_regular)
        tvRequireScan.minWidth = 100
        tvRequireScan.setPadding(0, 10, 0, 0)
        return tvRequireScan
    }

    fun getAssetVerified(activity: Context): View {
        val tvAssetVerified = TextView(activity)
        tvAssetVerified.text = activity.resources.getString(R.string.asset_verified)
        tvAssetVerified.textSize = 16f
        tvAssetVerified.setTextColor(Color.parseColor("#3FD389"))
        tvAssetVerified.setTypeface(
            ResourcesCompat.getFont(activity, R.font.inter_bold), Typeface.BOLD
        )
        tvAssetVerified.minWidth = 100
        tvAssetVerified.setPadding(0, 10, 0, 0)
        tvAssetVerified.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_check_green_24dp, 0, 0, 0
        )
        tvAssetVerified.gravity = Gravity.CENTER
        return tvAssetVerified
    }

    fun getTvAssetNotVerified(activity: Context): View {
        val tvAssetNotVerified = TextView(activity)
        tvAssetNotVerified.text = activity.resources.getString(R.string.asset_not_verified)
        tvAssetNotVerified.textSize = 16f
        tvAssetNotVerified.setTextColor(ContextCompat.getColor(activity, R.color.Color_Red))
        tvAssetNotVerified.typeface = ResourcesCompat.getFont(activity, R.font.inter_regular)
        tvAssetNotVerified.minWidth = 100
        tvAssetNotVerified.setPadding(0, 10, 0, 0)
        tvAssetNotVerified.gravity = Gravity.CENTER
        return tvAssetNotVerified
    }

    fun getButtonScanQr(activity: Context): View {
        val buttonScanQr = AppCompatButton(
            activity, null, android.R.style.Widget_Material_Button_Colored
        )
        buttonScanQr.text = activity.resources.getString(R.string.scan_qr)
        buttonScanQr.textSize = 14f
        buttonScanQr.setTextColor(ContextCompat.getColor(activity, R.color.Color_Primary))
        buttonScanQr.setTypeface(
            ResourcesCompat.getFont(activity, R.font.inter_bold), Typeface.BOLD
        )
        buttonScanQr.minWidth = 100
        buttonScanQr.setPadding(0, 0, 0, 0)
        buttonScanQr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_qr_icon_blue, 0, 0, 0)
        buttonScanQr.compoundDrawablePadding = 10
        buttonScanQr.gravity = Gravity.CENTER
        return buttonScanQr
    }

    fun getTvQuestion(activity: Context, checklistItem: ChklistItem): TextView {
        val tvQuestion = TextView(activity)
        if (checklistItem.title != null) {
            if (checklistItem.isRequired) {
                tvQuestion.text = Utils.returnTextWithRequiredSign(checklistItem?.title!!)
            } else {
                tvQuestion.text = checklistItem.title
            }
        }
        tvQuestion.tag = checklistItem.id
        tvQuestion.textSize = 16f
        tvQuestion.setTextColor(Color.BLACK)
        tvQuestion.typeface = ResourcesCompat.getFont(activity, R.font.inter_regular)
        tvQuestion.minWidth = 100
        tvQuestion.setPadding(0, 10, 0, 20)
        return tvQuestion
    }

    fun getTextViewDocumentLabel(activity: Context, tag: String?): View {
        val tvImage = TextView(activity)
        tvImage.text = activity.resources.getString(R.string.document)
        tvImage.tag = tag
        tvImage.minWidth = 50
        tvImage.setTextColor(
            ContextCompat.getColor(
                activity.applicationContext, R.color.Color_Primary
            )
        )
        tvImage.textSize = 13f
        tvImage.setTypeface(ResourcesCompat.getFont(activity, R.font.inter_bold), Typeface.BOLD)
        tvImage.setPadding(0, 0, 0, 0)
        tvImage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_document_light_small, 0, 0, 0)
        tvImage.compoundDrawablePadding = 10
        tvImage.gravity = Gravity.CENTER
        return tvImage
    }

    fun getTextViewArticleLabel(activity: Context, tag: String?): View {
        val tvImage = TextView(activity)
        tvImage.text = activity.resources.getString(R.string.article_link)
        tvImage.tag = tag
        tvImage.minWidth = 50
        tvImage.setTextColor(
            ContextCompat.getColor(
                activity.applicationContext, R.color.Color_Primary
            )
        )
        tvImage.textSize = 13f
        tvImage.setTypeface(ResourcesCompat.getFont(activity, R.font.inter_bold), Typeface.BOLD)
        tvImage.setPadding(0, 0, 0, 0)
        tvImage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_genius, 0, 0, 0)
        tvImage.compoundDrawablePadding = 10
        tvImage.gravity = Gravity.CENTER
        return tvImage
    }

    fun getTvFieldRequired(activity: Context): View {
        val tvFieldRequired = TextView(activity)
        tvFieldRequired.text = activity.resources.getString(R.string.field_required)
        tvFieldRequired.minWidth = 50
        tvFieldRequired.setTextColor(
            ContextCompat.getColor(
                activity.applicationContext, R.color.Color_Red
            )
        )
        tvFieldRequired.textSize = 16f
        tvFieldRequired.typeface = ResourcesCompat.getFont(activity, R.font.inter_regular)
        tvFieldRequired.setPadding(0, 0, 0, 0)
        return tvFieldRequired
    }

}
