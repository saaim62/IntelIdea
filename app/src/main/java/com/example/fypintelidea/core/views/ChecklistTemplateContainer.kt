package com.example.fypintelidea.core.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.ChklistTemplateResponse
import com.example.fypintelidea.core.utils.Utils
import com.example.fypintelidea.core.views.textview.CustomTextView
import com.example.fypintelidea.core.views.textview.TTextView
import kotlinx.android.synthetic.main.layout_checklist_template_container.view.*

class ChecklistTemplateContainer : LinearLayout {

    private var mContext: Context? = null

    constructor(context: Context) : super(context) {
        this.mContext = context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_checklist_template_container, this, true)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // more stuff
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_checklist_template_container, this, true)
    }

    fun setTvItemsCount(tvItemsCount: String) {
        this.tvItemsCount!!.text =
            tvItemsCount + " " + context.getString(R.string.checklist_items_caps)
    }

    fun setmBtnClose(drawable: Int) {
        this.btn_close!!.setImageResource(drawable)
    }

    fun getmBtnClose(): ImageButton? {
        return btn_close
    }

    fun setTemplate(chklistTemplate: ChklistTemplateResponse) {
        var count = 1
        val sections = chklistTemplate.chklistSections
        if (sections != null && sections.size > 0) {
            setTvItemsCount(Integer.toString(Utils.getTotalChecklistItemsCount(sections)))
            for (oneSection in sections) {
                val llCVContainer = LinearLayout(mContext)
                llCVContainer.orientation = VERTICAL
                val lpllCVContainer =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                lpllCVContainer.setMargins(4, 8, 4, 8)
                llCVContainer.layoutParams = lpllCVContainer

                if (oneSection.title != null) {
                    val tvSectionTitle = TTextView.Builder(mContext)
                        .setText(oneSection.title)
                        .setTextSize(26f)
                        .setTextColor(Color.BLACK)
                        .setStyle(CustomTextView.TextViewStyle.BOLD)
                        .setMinWidth(100)
                        .setPadding(0, 0, 0, 0)
                        .build()
                    llCVContainer.addView(tvSectionTitle)
                }

                val items = oneSection.chklistItem

                if (items?.isNotEmpty() == true) {
                    for (j in items.indices) {
                        val tvNumber = TTextView.Builder(mContext)
                            .setText((count++).toString())
                            .setTextSize(26f)
                            .setTextColor(Color.BLACK)
                            .setTypeface(ResourcesCompat.getFont(mContext!!, R.font.inter_regular))
                            .setMinWidth(50)
                            .setPadding(8, 0, 0, 0)
                            .build()
                        val tvTitle = TTextView.Builder(mContext)
                            .setText(items[j].title)
                            .setTextSize(26f)
                            .setTextColor(Color.BLACK)
                            .setTypeface(ResourcesCompat.getFont(mContext!!, R.font.inter_regular))
                            .setMinWidth(50)
                            .setPadding(16, 0, 0, 0)
                            .build()
                        val llItemRow = LinearLayout(mContext)
                        llItemRow.orientation = HORIZONTAL
                        val lpllItemRow =
                            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                        if (oneSection.id != null) {
                            lpllItemRow.setMargins(32, 8, 4, 8)
                        } else {
                            lpllItemRow.setMargins(0, 8, 4, 8)
                        }
                        llItemRow.layoutParams = lpllItemRow
                        llItemRow.addView(tvNumber)
                        llItemRow.addView(tvTitle)
                        llCVContainer.addView(llItemRow)
                    }
                }
                ll!!.addView(llCVContainer)
            }
        }
    }
}
