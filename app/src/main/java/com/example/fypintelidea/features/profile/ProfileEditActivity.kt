package com.example.fypintelidea.features.profile

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.providers.models.RootUser
import com.example.fypintelidea.core.providers.models.User
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import com.example.fypintelidea.core.utils.Utils
import kotlinx.android.synthetic.main.activity_asset_details.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile_edit.*
import kotlinx.android.synthetic.main.activity_profile_edit.ivPic
import kotlinx.android.synthetic.main.layout_request_template_container.*
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.EasyPermissions
import java.io.*


class ProfileEditActivity : ConnectavoBaseActivity() {
    val sessionManager: SessionManager by inject()
    private val profileEditActivityViewModel: ProfileEditActivityViewModel by inject()
    private var myFile: File? = null
    private var resultReceived: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = resources.getString(R.string.edit_profile)
            supportActionBar?.elevation = 0f
        }

        MyDateTimeStamp.setFrescoImage(
            ivPic,
            "https:" + sessionManager.getString(SessionManager.KEY_LOGIN_IMAGEPATH)
        )
        ivPic?.setOnClickListener {
            ivPic?.setImageBitmap(null)
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY)
        }
        tvUpload.setOnClickListener {
            ivPic?.setImageBitmap(null)
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY)
        }
    }

    override fun onResume() {
        super.onResume()
        populateData()
    }

    private fun populateData() {
        if (sessionManager.getString(SessionManager.KEY_LOGIN_NAME) != null) {
            etName?.setText(sessionManager.getString(SessionManager.KEY_LOGIN_NAME))
        }
        if (sessionManager.getString(SessionManager.KEY_LOGIN_NUMBER) != null) {
            etNumber?.setText(sessionManager.getString(SessionManager.KEY_LOGIN_NUMBER))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY && resultCode != 0) {
            this.resultReceived = data
            if (EasyPermissions.hasPermissions(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                populateImageAndData(resultReceived)
            } else {
                // Ask for one permission
                EasyPermissions.requestPermissions(
                    this, getString(R.string.permission_filepicker_rationale),
                    34, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (EasyPermissions.hasPermissions(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            populateImageAndData(resultReceived)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun populateImageAndData(data: Intent?) {
        val mImageUri = data?.data
        if (mImageUri != null) {
            myFile = File(getPath(this, mImageUri))
        }
        try {
            val image = MediaStore.Images.Media.getBitmap(this.contentResolver, mImageUri)
            if (getOrientation(applicationContext, mImageUri) != 0) {
                val matrix = Matrix()
                matrix.postRotate(getOrientation(applicationContext, mImageUri).toFloat())
                if (rotateImage != null)
                    rotateImage?.recycle()
                rotateImage = Bitmap.createBitmap(
                    image,
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )
                ivPic?.setImageBitmap(rotateImage)
            } else
                ivPic?.setImageBitmap(image)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_save -> when {
                etName?.text.toString().isEmpty() -> {
                    etName?.error = resources.getString(R.string.name_is_empty)
                }
                etNumber?.text.toString().isEmpty() -> {
                    etNumber?.error = resources.getString(R.string.number_is_empty)
                }
                else -> {
                    if (EasyPermissions.hasPermissions(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        editProfile()
                    } else {
                        // Ask for one permission
                        EasyPermissions.requestPermissions(
                            this, getString(R.string.permission_filepicker_rationale),
                            34, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
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

    private fun editProfile() {
        var user = User(etName?.text.toString(), etNumber?.text.toString())
        if (myFile != null) {
            val tempFile = Utils.reduceFileSize(myFile!!)
            tempFile?.let {
                val size = tempFile.length().toInt()
                val bytes = ByteArray(size)
                try {
                    val buf = BufferedInputStream(FileInputStream(tempFile))
                    buf.read(bytes, 0, bytes.size)
                    buf.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                var imageString = Base64.encodeToString(bytes, Base64.DEFAULT)
                imageString = "data:image/jpeg;base64,$imageString"
                user = User(
                    name = etName?.text.toString(),
                    phone = etNumber?.text.toString(),
                    avatarBase64 = imageString
                )
            }
        }

        val rootUser = RootUser(user)
        progressBarEditProfile?.visibility = View.VISIBLE
        profileEditActivityViewModel.updateUserProfile(rootUser)
        setObservers()
    }

    fun setObservers() {
        profileEditActivityViewModel.userProfileEditObservable.observe(this) {
            val apiResponse = it.value
            progressBarEditProfile?.visibility = View.GONE
            if (apiResponse.name != null) {
                sessionManager.put(SessionManager.KEY_LOGIN_NAME, apiResponse.name!!)
            }
            if (apiResponse.phone != null) {
                sessionManager.put(SessionManager.KEY_LOGIN_NUMBER, apiResponse.phone!!)
            }
            if (apiResponse.avatarUrl != null) {
                sessionManager.put(
                    SessionManager.KEY_LOGIN_IMAGEPATH,
                    apiResponse.avatarUrl!!
                )
            }
            Toast.makeText(
                this@ProfileEditActivity,
                "Successfully updated",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    companion object {
        private const val GALLERY = 1
        private var rotateImage: Bitmap? = null

        fun getOrientation(context: Context, photoUri: Uri?): Int {
            /* it's on the external media. */
            val cursor = photoUri?.let {
                context.contentResolver.query(
                    it,
                    arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null
                )
            }
            if (cursor != null) {
                if (cursor.count != 1) {
                    return -1
                }
                cursor.moveToFirst()
                val orientation = cursor.getInt(0)
                cursor.close()
                return orientation
            }
            return 0
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun getPath(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = uri?.let {
                    context.contentResolver.query(
                        it,
                        projection,
                        selection,
                        selectionArgs,
                        null
                    )
                }
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }
    }
}
