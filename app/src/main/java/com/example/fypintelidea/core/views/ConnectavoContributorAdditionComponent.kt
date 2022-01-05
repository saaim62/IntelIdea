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
import com.example.fypintelidea.core.providers.models.User
import kotlinx.android.synthetic.main.layout_contributor_addition_view.view.*


class ConnectavoContributorAdditionComponent : ConstraintLayout {

    private lateinit var mView: View
    private var usersList = arrayListOf<User>()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = inflater.inflate(R.layout.layout_contributor_addition_view, this)
    }

    private fun showHideContributorAdditionSection() {
        if (usersList.size < 5) {
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
        for (i in 0..usersList.size) {
            val cl = findViewById<LinearLayout>(R.id.cl)
            val hoursSpent = cl.findViewWithTag<EditText>(CONTRIBUTOR_HOURS_SPENT + i)
            if (hoursSpent != null) {
                if (hoursSpent.text.isNullOrBlank() || hoursSpent.text.isNullOrEmpty() || hoursSpent.text == null || hoursSpent.text.equals(
                        ""
                    )
                ) {
                    hoursSpent.error = resources.getString(R.string.field_required)
                    return false
                }
            }
        }
        return true
    }

    fun getListOfUser(): ArrayList<User> {
        return usersList
    }

    fun setListOfUser(list: ArrayList<User>) {
        if (usersList.isEmpty()) {
            usersList.addAll(list)
        } else {
            for (oneUser in list) {
                var add = false
                list.forEach { listItem ->
                    usersList.forEach { usersListItem ->
                        add = usersListItem.id == listItem.id
                    }
                }
                if (!add) {
                    usersList.add(oneUser)
                }
            }
        }
        updateUi()
    }

    @SuppressLint("ResourceType")
    fun updateUi() {
        cl.removeAllViews()
        usersList.forEach { contributorIt ->
            val mainLayout = mView.findViewById(R.id.cl) as LinearLayout
            val inflater =
                mView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val child: View =
                inflater.inflate(R.layout.activity_complete_workorder_dynamic_layout, null)

            //Title
            val tvName = child.findViewById<TextView>(R.id.tvContributor)
            if (contributorIt.display_name != null) {
                tvName.text = contributorIt.display_name
            } else {
                tvName.text = contributorIt.name
            }
            tvName.tag = CONTRIBUTOR_ID + usersList.size

            //EditTextValue
            val editTextValue = child.findViewById<EditText>(R.id.etHoursSpent)
            editTextValue.setText(contributorIt.hours_spent)
            editTextValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    contributorIt.hours_spent = editTextValue.text.toString()
                }

                override fun afterTextChanged(editable: Editable) {}
            })
            editTextValue.tag = CONTRIBUTOR_HOURS_SPENT + usersList.size

            //close buttom
            val imageViewCross = child.findViewById<ImageView>(R.id.imageViewCross)
            imageViewCross.setOnClickListener {
                mainLayout.removeView(child)
                usersList.remove(contributorIt)
                showHideContributorAdditionSection()
            }
            showHideContributorAdditionSection()
            mainLayout.addView(child)
        }
    }

    companion object {
        private const val CONTRIBUTOR_ID = "id"
        private const val CONTRIBUTOR_HOURS_SPENT = "hours_spent"
    }
}