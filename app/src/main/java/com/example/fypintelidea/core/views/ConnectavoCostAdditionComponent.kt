package com.example.fypintelidea.core.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.Costs
import kotlinx.android.synthetic.main.layout_cost_addition_view.view.*

class ConnectavoCostAdditionComponent : ConstraintLayout {
    companion object {
        private const val COST_TITLE = "title"
        private const val COST_VALUE = "value"
    }

    private lateinit var mView: View
    private var list = arrayListOf<Costs>()
    var count = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs)
    }

    @SuppressLint("ResourceType")
    private fun initialize(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = inflater.inflate(R.layout.layout_cost_addition_view, this)

        imageView_add.setOnClickListener {
            val mainLayout = mView.findViewById(R.id.cl) as LinearLayout
            val inflaterChild =
                mView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val child: View =
                inflaterChild.inflate(R.layout.cost_component_layout, null)

            //Edit Text Title
            val etTitle = child.findViewById<EditText>(R.id.etTitle)
            etTitle.tag = COST_TITLE + count

            //Edit Text Value
            val etValue = child.findViewById<EditText>(R.id.etValue)
            etValue.tag = COST_VALUE + count

            //IV Close
            val imageViewCross = child.findViewById<ImageView>(R.id.imageViewCross)
            imageViewCross.setOnClickListener {
                mainLayout.removeView(child)
                count -= 1
                showHideContributorAdditionSection(count)
            }

            mainLayout.addView(child)
            count += 1
            showHideContributorAdditionSection(count)
        }
    }

    @SuppressLint("ResourceType")
    fun setListOfCosts(costs: List<Costs>) {
        cl.removeAllViews()
        costs.forEach { costIt ->
            val mainLayout = mView.findViewById(R.id.cl) as LinearLayout
            val inflaterChild =
                mView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val child: View =
                inflaterChild.inflate(R.layout.cost_component_layout, null)

            //Edit Text Title
            val etTitle = child.findViewById<EditText>(R.id.etTitle)
            etTitle.tag = COST_TITLE + count
            etTitle.setText(costIt.title)

            //Edit Text Value
            val etValue = child.findViewById<EditText>(R.id.etValue)
            etValue.tag = COST_VALUE + count
            etValue.setText(costIt.value)

            //IV Close
            val imageViewCross = child.findViewById<ImageView>(R.id.imageViewCross)
            imageViewCross.setOnClickListener {
                mainLayout.removeView(child)
                count -= 1
                showHideContributorAdditionSection(count)
            }
            mainLayout.addView(child)
            count += 1
            showHideContributorAdditionSection(count)
        }
    }

    private fun showHideContributorAdditionSection(count: Int) {
        if (count >= 5) {
            imageView_costIcon?.visibility = View.GONE
            textView_addOtherCosts?.visibility = View.GONE
            imageView_add?.visibility = View.GONE
        } else {
            imageView_costIcon?.visibility = View.VISIBLE
            textView_addOtherCosts?.visibility = View.VISIBLE
            imageView_add?.visibility = View.VISIBLE
        }
    }

    fun checkValidations(): Boolean {
        for (i in 0..count) {
            val cl = findViewById<LinearLayout>(R.id.cl)
            val title = cl.findViewWithTag<EditText>(COST_TITLE + i)
            val value = cl.findViewWithTag<EditText>(COST_VALUE + i)
            if (value != null && title != null) {
                if (title.text.isEmpty()) {
                    title.error = resources.getString(R.string.field_required)
                    return false
                }
                if (value.text.isEmpty()) {
                    value.error = resources.getString(R.string.field_required)
                    return false
                }
            }
        }
        return true
    }

    fun getListOfCosts(): List<Costs> {
        for (i in 0..count) {
            val cl = findViewById<LinearLayout>(R.id.cl)
            val title = cl.findViewWithTag<EditText>(COST_TITLE + i)
            val value = cl.findViewWithTag<EditText>(COST_VALUE + i)
            if (title != null && value != null) {
                if (title.text.isNotEmpty() && value.text.isNotEmpty())
                    list.add(Costs(title = title.text.toString(), value = value.text.toString()))
            }
        }
        return list
    }

}
