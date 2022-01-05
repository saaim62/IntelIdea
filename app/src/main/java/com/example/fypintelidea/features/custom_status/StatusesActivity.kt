package com.example.fypintelidea.features.custom_status

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConstantsStatuses
import com.example.fypintelidea.core.providers.models.Status
import com.example.fypintelidea.core.views.DynamicViews
import java.util.*

class StatusesActivity : AppCompatActivity(), View.OnClickListener {
    private var gl: GridLayout? = null
    private var mList: MutableList<Status>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statuses)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = resources.getString(R.string.select_status_caps)
            supportActionBar?.elevation = 0f
        }

        val constantsStatuses = ConstantsStatuses()
        mList!!.addAll(constantsStatuses.all(this))

        if (mList!!.size > 0) {
            gl = findViewById(R.id.gl)
            val dynamicViews = DynamicViews(this@StatusesActivity)
            for (j in mList!!.indices) {
                val checkBox = dynamicViews.checkBoxWithSpace("", mList!![j].name, 20, 60, 0, 60)
                gl!!.addView(checkBox)
                val ivIcon =
                    dynamicViews.imageView(mList!![j].image!!, mList!![j].name, 0, 60, 20, 60)
                gl!!.addView(ivIcon)
                val tvName = dynamicViews.textView(mList!![j].name, mList!![j].name, false, 300)
                gl!!.addView(tvName)

                //set initial selection if received/required.
                var selected: MutableList<String>? =
                    intent.getStringArrayListExtra(KEY_SELECTED_STATUSES)
                if (selected != null && selected.size > 0) {
                    for (i in selected.indices) {
                        if (mList!![j].name!!.equals(selected[i], ignoreCase = true)) {
                            checkBox.isChecked = true
                        }
                    }
                } else {
                    //none of the status was selected. Hence apply default selection
                    selected = ArrayList()
                    selected.add(getString(R.string.custom_status_open))
                    selected.add(getString(R.string.custom_status_in_progress))
                    selected.add(getString(R.string.custom_status_pause))
                    if (selected.size > 0) {
                        for (i in selected.indices) {
                            if (mList!![j].name!!.equals(selected[i], ignoreCase = true)) {
                                checkBox.isChecked = true
                            }
                        }
                    }
                }
            }
        }

        val bDone = findViewById<Button>(R.id.bDone)
        bDone.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        if (mList != null && mList!!.size > 0) {
            val checkedList = ArrayList<String>()
            for (i in mList!!.indices) {
                val checkBox = gl!!.findViewWithTag<CheckBox>(mList!![i].name)
                if (checkBox.isChecked) {
                    checkedList.add(mList!![i].name!!)
                }
            }
            val returnIntent = Intent()
            returnIntent.putStringArrayListExtra(KEY_SELECTED_STATUSES, checkedList)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        } else {
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }
    }

    companion object {
        const val KEY_SELECTED_STATUSES = "selected_statuses"
    }
}
