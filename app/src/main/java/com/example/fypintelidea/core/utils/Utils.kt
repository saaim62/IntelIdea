package com.example.fypintelidea.core.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.R
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.session.SessionManager.Companion.USER_ROLE_ADMIN
import com.example.fypintelidea.features.on_boarding.SplashActivity
import com.example.fypintelidea.core.providers.models.Asset
import com.example.fypintelidea.core.providers.models.ChklistItem
import com.example.fypintelidea.core.providers.models.ChklistSection
import com.example.fypintelidea.core.providers.models.Workorder
import droidninja.filepicker.utils.ContentUriUtils
import java.io.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.exitProcess

object Utils {

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun onResponse(mContext: Activity, statusCode: Int) {
        if (statusCode == 401) {
            Toast.makeText(
                mContext,
                mContext.resources.getString(R.string.privileges_changed),
                Toast.LENGTH_LONG
            ).show()
            val handler = Handler()
            handler.postDelayed({
                val mStartActivity = Intent(mContext, SplashActivity::class.java)
                val mPendingIntentId = 123456
                val mPendingIntent = PendingIntent.getActivity(
                    mContext, mPendingIntentId, mStartActivity,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
                val mgr = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
                exitProcess(0)
            }, 2000)
        }
    }

    fun getAssetById(totalAssets: List<Asset>, assetIdOrCustomScanCode: String): Asset? {
        for (oneAsset in totalAssets) {
            if (oneAsset.id != null) {
                if (oneAsset.id == assetIdOrCustomScanCode || oneAsset.custom_scan_code == assetIdOrCustomScanCode) {
                    return oneAsset
                }
            }
        }
        return null
    }

    fun getAssetsAssignedToMeOrMyTeam(
        context: Context,
        assets: MutableList<Asset>
    ): MutableList<Asset> {
        if (SessionManager(context).getString(SessionManager.KEY_LOGIN_ROLE) == USER_ROLE_ADMIN) {
            return assets
        } else {
            val assignedAssetsToMeOrMyTeam = arrayListOf<Asset>()
            for (oneAsset in assets) {
                var isInSameTeam = false
                oneAsset.team_ids?.forEach { assetTeamId ->
                    SessionManager(context).getArrayList(SessionManager.KEY_LOGIN_TEAM_IDS)
                        .forEach {
                            if (assetTeamId == it) {
                                isInSameTeam = true
                            }
                        }
                }

                if (oneAsset.responsible_emp == SessionManager(context).getString(SessionManager.KEY_LOGIN_ID) || isInSameTeam)
                    assignedAssetsToMeOrMyTeam.add(oneAsset)
            }
            return assignedAssetsToMeOrMyTeam
        }
    }

    fun getChildAssets(
        list: List<Asset>,
        asset: Asset,
    ): List<Asset> {
        val childAssets: MutableList<Asset> = ArrayList()
        for (oneAsset in list) {
            if (oneAsset.parent_id != null) {
                if (oneAsset.parent_id == asset.id) {
                    childAssets.add(oneAsset)
                }
            }
        }
        return childAssets
    }

    fun areRequiredChklistFieldsFilled(workorder: Workorder): Boolean {
        var validated = true
        if (workorder.chklistSections != null) {
            workorder.chklistSections?.forEach { it ->
                if (it.chklistItem != null) {
                    if (it.chklistItem?.isNotEmpty() == true) {
                        it.chklistItem?.forEach {
                            if (it.isRequired) {
                                if (it.item_type == (ChklistItem.ITEM_TYPE_CHECKBOX)) {
                                    if (it.value == null || it.value == ("") || it.value == ("0")) {
                                        validated = false
                                    }
                                } else if (it.item_type == (ChklistItem.ITEM_TYPE_TEXTFIELD)) {
                                    if (it.value == null || it.value == ("")) {
                                        validated = false
                                    }
                                } else if (it.item_type == (ChklistItem.ITEM_TYPE_NUMBERFIELD)) {
                                    if (it.value == null || it.value == ("")) {
                                        validated = false
                                    }
                                } else if (it.item_type == (ChklistItem.ITEM_TYPE_SCORE)) {
                                    if (it.value == null || it.value == ("")) {
                                        validated = false
                                    }
                                } else if (it.item_type == (ChklistItem.ITEM_TYPE_SINGLE_SELECT)) {
                                    if (it.selected_option_ids == null || it.selected_option_ids?.isEmpty() == true) {
                                        validated = false
                                    }
                                } else if (it.item_type == (ChklistItem.ITEM_TYPE_MULTI_SELECT)) {
                                    if (it.selected_option_ids == null || it.selected_option_ids?.isEmpty() == true) {
                                        validated = false
                                    }
                                } else if (it.item_type == (ChklistItem.ITEM_TYPE_RANGE)) {
                                    if (it.value == null || it.value == ("")) {
                                        validated = false
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        return validated
    }

    fun submitChklistDataToServer(workorder: Workorder): HashMap<String, String> {
        val mapChecklist = HashMap<String, String>()
        if (workorder.chklistSections != null) {
            workorder.chklistSections?.forEachIndexed { sectionIndex, chklistSection ->
                for (i in chklistSection.chklistItem?.indices!!) {
                    if (chklistSection.chklistItem!![i].item_type.equals(
                            ChklistItem.ITEM_TYPE_CHECKBOX,
                            ignoreCase = true
                        ) || chklistSection.chklistItem!![i].item_type.equals(
                            ChklistItem.ITEM_TYPE_TEXTFIELD,
                            ignoreCase = true
                        ) || chklistSection.chklistItem!![i].item_type.equals(
                            ChklistItem.ITEM_TYPE_NUMBERFIELD,
                            ignoreCase = true
                        ) || chklistSection.chklistItem!![i].item_type.equals(
                            ChklistItem.ITEM_TYPE_SCORE,
                            ignoreCase = true
                        ) || chklistSection.chklistItem!![i].item_type.equals(
                            ChklistItem.ITEM_TYPE_RANGE,
                            ignoreCase = true
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
                            getBase64StringFromPhotoPath(chklistSection.chklistItem!![i].photoPaths)
                        if (chklistSection.chklistItem!![i].docs_attributes_base64 != null) {
                            for (j in chklistSection.chklistItem!![i].docs_attributes_base64?.indices!!) {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][images][$j]"] =
                                    chklistSection.chklistItem!![i].docs_attributes_base64?.get(j)!!
                            }
                        }

                    } else if (chklistSection.chklistItem!![i].item_type.equals(
                            ChklistItem.ITEM_TYPE_SINGLE_SELECT,
                            ignoreCase = true
                        ) || chklistSection.chklistItem!![i].item_type.equals(
                            ChklistItem.ITEM_TYPE_MULTI_SELECT,
                            ignoreCase = true
                        )
                    ) {

                        if (chklistSection.chklistItem!![i].id != null) {
                            mapChecklist["chklist_section[$sectionIndex][items][$i][id]"] =
                                chklistSection.chklistItem!![i].id!!
                        }

                        if (chklistSection.chklistItem!![i].selected_option_ids != null) {
                            var ids = StringBuilder()
                            for (element in chklistSection.chklistItem!![i].selected_option_ids!!) {
                                ids.insert(
                                    0,
                                    "$element,"
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
                            getBase64StringFromPhotoPath(chklistSection.chklistItem!![i].photoPaths)
                        if (chklistSection.chklistItem!![i].docs_attributes_base64 != null) {
                            for (j in chklistSection.chklistItem!![i].docs_attributes_base64?.indices!!) {
                                mapChecklist["chklist_section[$sectionIndex][items][$i][images][$j]"] =
                                    chklistSection.chklistItem!![i].docs_attributes_base64?.get(j)!!
                            }
                        }

                    }
                }
            }
        }
        return mapChecklist
    }

    fun reduceFileSize(file: File): File? {
        try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image

            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)

            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)

            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)

            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun getBase64StringFromPhotoPath(photoPaths: List<String>?): List<String>? {
        if (photoPaths != null) {
            val docsAttributesBase64 = ArrayList<String>()
            for (onePhotoPath in photoPaths) {
                val myFile = File(onePhotoPath)
                val tempFile = reduceFileSize(myFile)
                if (tempFile != null) {
                    val size = tempFile.length().toInt()
                    val bytes = ByteArray(size)
                    try {
                        val buf = BufferedInputStream(FileInputStream(tempFile))
                        buf.read(bytes, 0, bytes.size)
                        buf.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    var imageString = Base64.encodeToString(bytes, Base64.DEFAULT)
                    imageString = "data:image/jpeg;base64,$imageString"
                    docsAttributesBase64.add(imageString)
                } else {
                    val size = myFile.length().toInt()
                    val bytes = ByteArray(size)
                    try {
                        val buf = BufferedInputStream(FileInputStream(myFile))
                        buf.read(bytes, 0, bytes.size)
                        buf.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    var imageString = Base64.encodeToString(bytes, Base64.DEFAULT)
                    imageString = "data:image/jpeg;base64,$imageString"
                    docsAttributesBase64.add(imageString)
                }
            }
            return docsAttributesBase64
        }
        return null
    }

    fun getFilledChecklistItemsCount(chklistSections: List<ChklistSection>): Int {
        var count = 0
        for (oneSection in chklistSections) {
            if (oneSection.chklistItem != null && oneSection.chklistItem?.isNotEmpty() == true) {
                for (oneItem in oneSection.chklistItem!!) {
                    if (oneItem.value != null && oneItem.value != "" && oneItem.value != "0") {
                        count++
                    } else if (oneItem.selected_option_ids != null && oneItem.selected_option_ids?.isNotEmpty() == true) {
                        count++
                    }
                }
            }
        }
        return count
    }

    fun getTotalChecklistItemsCount(chklistSections: List<ChklistSection>): Int {
        var count = 0
        for (oneSection in chklistSections) {
            if (oneSection.chklistItem != null && oneSection.chklistItem?.isNotEmpty() == true) {
                for (oneItem in oneSection.chklistItem!!) {
                    count++
                }
            }
        }
        return count
    }

    fun returnTextWithRequiredSign(someText: String): SpannableStringBuilder {
        val title = SpannableStringBuilder()
        title.append(someText)
        val start = title.length
        title.append(" *")
        title.setSpan(
            ForegroundColorSpan(Color.RED),
            start,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        title.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return title
    }

    fun getFileByBitmap(context: Context, myBitmap: Bitmap): File? {
        val f: File
        try {
            //create a file to write bitmap data
            f = File(
                context.cacheDir,
                String.format("Signature_%d.jpg", System.currentTimeMillis())
            )
            f.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return f
    }

    fun rotateImageIfNeeded(bitmap: Bitmap, path: String): Bitmap {
        var rotatedBitmap: Bitmap?
        try {
            val ei = ExifInterface(path)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            rotatedBitmap = when (orientation) {

                ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(
                    bitmap,
                    90
                )

                ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(
                    bitmap,
                    180
                )

                ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(
                    bitmap,
                    270
                )

                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
            rotatedBitmap = bitmap
        }
        return rotatedBitmap!!
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    fun getArrayOfUrisFromArrayOfStrings(photoPaths: ArrayList<String>): ArrayList<Uri> {
        val photoPathsUri = ArrayList<Uri>()
        photoPaths.forEach {
            Uri.parse(it)?.let { onePathString -> photoPathsUri.add(onePathString) }
        }
        return photoPathsUri
    }

    fun getArrayOfStringsFromArrayOfUris(
        context: Context,
        photoPathUri: ArrayList<Uri>?
    ): ArrayList<String> {
        val photoPathsString = ArrayList<String>()
        photoPathUri?.forEach { oneUri ->
            ContentUriUtils.getFilePath(context, oneUri)
                ?.let { oneString -> photoPathsString.add(oneString) }
        }
        return photoPathsString
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
