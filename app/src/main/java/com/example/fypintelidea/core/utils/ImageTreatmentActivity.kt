package com.example.fypintelidea.core.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.fypintelidea.R
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.activity_image_treatment.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ImageTreatmentActivity : AppCompatActivity() {
    private var imagePath: String? = null
    private var myFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_treatment)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = resources.getString(R.string.add_annotation)
            supportActionBar?.elevation = 0f
        }
        verifyStoragePermissions(this)

        val bundle = intent.extras
        if (bundle != null) {
            imagePath = bundle.getString(IMAGE_PATH)
            imagePath?.let {
                myFile = File(it)
                signature_pad?.setPenColorRes(R.color.md_red_600)
                val bitmap = BitmapFactory.decodeFile(myFile?.absolutePath)
                signature_pad?.signatureBitmap = Utils.rotateImageIfNeeded(bitmap, imagePath!!)
            }
        } else {
            finish()
        }

        signature_pad?.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {}

            override fun onSigned() {
                //                mSaveButton.setEnabled(true);
                clear_button?.isEnabled = true
                val drawable = resources.getDrawable(R.drawable.ic_undo_blue_24dp)
                //                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable, resources.getColor(R.color.Color_Primary))
                clear_button?.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }

            override fun onClear() {
                //                mSaveButton.setEnabled(false);
                clear_button?.isEnabled = false
                val drawable = resources.getDrawable(R.drawable.ic_undo_grey_24dp)
                //                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable, resources.getColor(R.color.md_grey_400))
                clear_button?.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }
        })
        clear_button?.setOnClickListener { view ->
            signature_pad?.clear()
            if (imagePath != null) {
                myFile = File(imagePath)
                val bmOptions = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeFile(myFile?.absolutePath, bmOptions)
                signature_pad?.signatureBitmap = bitmap
            }
        }
    }

    private fun onSave() {
        val signatureBitmap = signature_pad?.signatureBitmap
        if (myFile?.let {
                signatureBitmap?.let { it1 ->
                    saveImageToOriginalLocation(
                        it1,
                        it
                    )
                }
            } == true) {
            Toast.makeText(
                this,
                resources.getString(R.string.successfully_updated),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } else {
            Toast.makeText(this, resources.getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        // If request is cancelled, the result arrays are empty.
        //                if (grantResults.length <= 0
        //                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        //                    Toast.makeText(ImageTreatmentActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
        //                }
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
        }
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    private fun saveImageToOriginalLocation(signature: Bitmap, photo: File): Boolean {
        var result = false
        try {
            saveBitmapToJPG(signature, photo)
            scanMediaFile(photo)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun scanMediaFile(photo: File?) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        this@ImageTreatmentActivity.sendBroadcast(mediaScanIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.draw_signature_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_tick ->
                if (EasyPermissions.hasPermissions(
                        this@ImageTreatmentActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    onSave()
                } else {
                    // Ask for one permission
                    EasyPermissions.requestPermissions(
                        this@ImageTreatmentActivity,
                        getString(R.string.permission_filepicker_rationale),
                        34,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val IMAGE_PATH = "image_path"
        private val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        /**
         * Checks if the app has permission to write to device storage
         *
         *
         * If the app does not has permission then the user will be prompted to grant permissions
         *
         * @param activity the activity from which permissions are checked
         */
        fun verifyStoragePermissions(activity: Activity) {
            // Check if we have write permission
            val permission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }
}
