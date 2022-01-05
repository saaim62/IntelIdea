package com.example.fypintelidea.core.utils


import android.app.DownloadManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.fypintelidea.core.utils.photoview.PhotoView
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Document
import kotlinx.android.synthetic.main.activity_large_image.*
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL


class LargeImageActivity : AppCompatActivity() {
    var workorder: List<Document>? = null
    private var shortAnimationDuration: Int = 0

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_large_image)
        val docName = intent.extras?.getString(DOC_NAME)
        val docUrl = intent.extras?.getString(DOC_URL)
        val docType = intent.extras?.getString(DOC_TYPE)

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        tvName.text = docName
        if (docType == "image/jpeg" || docType == "image/png") {
            imageFunc(user_avatar, "https:$docUrl")
            ivDownload?.setOnClickListener {
                val request = DownloadManager.Request(
                    Uri.parse("https:$docUrl")
                )
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    "downloaded"
                )
                val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)

                Toast.makeText(
                    applicationContext, "Downloading File",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            webView.visibility = View.VISIBLE
            loadPdf(docUrl)
            ivDownload?.setOnClickListener {
                val request = DownloadManager.Request(
                    Uri.parse("https:$docUrl")
                )
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOCUMENTS,
                    "downloaded"
                )
                val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)

                Toast.makeText(
                    applicationContext, "Downloading File",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        ivBack?.setOnClickListener {
            finish()
        }

    }

    private fun loadPdf(docUrl: String?) {
        MyAsyncTask(this, docUrl).execute()
    }

    private fun imageFunc(imageView: PhotoView, url: String) {
        Glide.with(this)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    user_avatar.visibility = View.GONE
                    ivPlaceHolder.visibility = View.VISIBLE
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(imageView)
    }

    companion object {
        class MyAsyncTask internal constructor(
            context: LargeImageActivity,
            private val docUrl: String?
        ) : AsyncTask<Unit, Unit, InputStream>() {
            private val activityReference: WeakReference<LargeImageActivity> =
                WeakReference(context)

            override fun onPreExecute() {
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.PdfProgressBar.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: Unit): InputStream {
                return URL("https:$docUrl").openStream()
            }

            override fun onPostExecute(result: InputStream) {
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.PdfProgressBar.visibility = View.GONE
                activity.webView.fromStream(result).load()
            }
        }

        val DOC_DATE = "doc_date"
        val DOC_TYPE = "doc_type"
        val DOC_NAME = "doc_name"
        val DOC_URL = "doc_url"
    }
}