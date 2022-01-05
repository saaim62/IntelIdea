package com.example.fypintelidea.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.fypintelidea.core.Constants
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConstantsCategories
import com.example.fypintelidea.core.providers.models.Request_templates
import kotlinx.android.synthetic.main.layout_request_template_container.view.*

class RequestTemplateContainer : LinearLayout {

    private var mContext: Context? = null

    constructor(context: Context) : super(context) {
        this.mContext = context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_request_template_container, this, true)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // more stuff
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_request_template_container, this, true)
    }

    fun setmBtnClose(drawable: Int) {
        this.btn_close!!.setImageResource(drawable)
    }

    fun getmBtnClose(): ImageButton? {
        return btn_close
    }

    fun setTemplate(requestTemplate: Request_templates) {
        if (requestTemplate.name != null) {
            textView_employeeValue.text = requestTemplate.employee
        }

        if (requestTemplate.teams != null) {
            textView_teamValue.text = requestTemplate.teams
        }

        if (requestTemplate.due_within != null) {
            textView_dueWithinValue.text =
                requestTemplate.due_within + Constants.SPACE_STRING + if (requestTemplate.due_within_unit == Request_templates.DUE_WITHIN_DAYS) {
                    resources.getString(R.string.days)
                } else {
                    resources.getString(R.string.hours)
                }
        }

        if (requestTemplate.priority != null) {
            textView_priorityValue.text = requestTemplate.priority
        }

        if (requestTemplate.eta != null) {
            textView_etaValue.text = requestTemplate.eta
        }

        if (requestTemplate.category != null) {
            textView_categoryValue.text = requestTemplate?.category?.let {
                it
            }?.let {
                ConstantsCategories.getUserFriendlyName(
                    mContext!!,
                    it
                )
            }
        }

        if (requestTemplate.description != null) {
            textView_descriptionValue.text = requestTemplate.description
        }

        if (requestTemplate.tags != null) {
            textView_tagsValue.text = requestTemplate.tags
        }

        if (requestTemplate.spare_parts != null) {
            textView_sparePartsValue.text = requestTemplate.spare_parts
        }

        if (requestTemplate.checklist != null) {
            textView_checklistValue.text = requestTemplate.checklist
        }

        if (requestTemplate.documents != null) {
            textView_documentValue.text = requestTemplate.documents
        }
    }
}
