package com.example.fypintelidea.core.views

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fypintelidea.R
import com.example.fypintelidea.core.providers.models.SpareParts
import kotlinx.android.synthetic.main.layout_contributor_addition_view.view.*


class ConnectavoSparePartsComponent : ConstraintLayout {

    private lateinit var mView: View
    var spareParts = arrayListOf<SpareParts>()

    constructor(context: Context) : super(context)

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

    private fun initialize(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = inflater.inflate(R.layout.layout_spare_parts_view, this)
    }

    private fun showHideContributorAdditionSection() {
        if (spareParts.size < 5) {
            imageView_contributorIcon.visibility = View.VISIBLE
            textView_addContributors.visibility = View.VISIBLE
            imageView_add.visibility = View.GONE
        } else {
            imageView_contributorIcon.visibility = View.GONE
            textView_addContributors.visibility = View.GONE
            imageView_add.visibility = View.GONE
        }
    }

    fun checkValidations(): Boolean {
        for (i in 0..spareParts.size) {
            val cl = findViewById<LinearLayout>(R.id.cl)
            val etQuantity = cl.findViewWithTag<EditText>(QUANTITY + i)
            if (etQuantity != null) {
                if (etQuantity.text.isNullOrBlank() || etQuantity.text.isNullOrEmpty() || etQuantity.text == null || etQuantity.text.equals(
                        ""
                    )
                ) {
                    etQuantity.error = resources.getString(R.string.field_required)
                    return false
                }
            }
        }
        return true
    }

    fun getListOfSpareParts(): ArrayList<SpareParts> {
        return spareParts
    }

    fun setListOfSpareParts(list: ArrayList<SpareParts>) {
        if (spareParts.isEmpty()) {
            spareParts.addAll(list)
        } else {
            for (oneSparePart in list) {
                var add = false
                list.forEach { listItem ->
                    spareParts.forEach { sparePartsItem ->
                        add = sparePartsItem.spare_part_id == listItem.spare_part_id
                    }
                }
                if (!add) {
                    spareParts.add(oneSparePart)
                }
            }
        }
        updateUi()
    }

    @SuppressLint("ResourceType")
    fun updateUi() {
        cl.removeAllViews()
        spareParts.forEach { sparePartsIt ->

            val mainLayout = mView.findViewById(R.id.cl) as LinearLayout
            val inflater =
                mView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val child: View =
                inflater.inflate(R.layout.activity_complete_workorder_dynamic_layout, null)

            //Title
            val tvName = child.findViewById<TextView>(R.id.tvContributor)
            tvName.text = sparePartsIt.name
            tvName.tag = SPARE_PARTS_ID + spareParts.size


            // Edit text
            val editTextValue = child.findViewById<EditText>(R.id.etHoursSpent)
            editTextValue.setText(sparePartsIt.quantity)
            editTextValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    sparePartsIt.quantity = editTextValue.text.toString()
                }

                override fun afterTextChanged(editable: Editable) {}
            })
            editTextValue.tag = QUANTITY + spareParts.size
            editTextValue.hint = resources.getString(R.string.quantity)

            // Cancel Icon
            val imageViewCross = child.findViewById<ImageView>(R.id.imageViewCross)
            imageViewCross.setOnClickListener {
                mainLayout.removeView(child)
                spareParts.remove(sparePartsIt)
                showHideContributorAdditionSection()
            }

            mainLayout.addView(child)
            showHideContributorAdditionSection()
        }
    }

    companion object {
        private const val SPARE_PARTS_ID = "id"
        private const val QUANTITY = "quantity"
    }
}