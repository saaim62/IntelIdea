package com.example.fypintelidea.features.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.session.SessionManager.Companion.KEY_LOGIN_SUBDOMAIN
import com.example.fypintelidea.features.adapters.CategoriesAdapter
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.ConnectavoUtils
import com.example.fypintelidea.core.ConstantsCategories
import com.example.fypintelidea.core.CustomerSpaces
import kotlinx.android.synthetic.main.activity_categories.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class CategoriesActivity : ConnectavoBaseActivity() {
    val sessionManager: SessionManager by inject()
    private var mList: MutableList<String> = ArrayList()
    var adapter: CategoriesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val constantsCategories = ConstantsCategories()
        when {
            sessionManager.getString(KEY_LOGIN_SUBDOMAIN).equals(
                CustomerSpaces.curatedstage.name,
                ignoreCase = true
            ) -> {
                mList.addAll(constantsCategories.allCategoriesForSteiner(this))
            }
            sessionManager.getString(KEY_LOGIN_SUBDOMAIN).equals(
                CustomerSpaces.stoba.name,
                ignoreCase = true
            ) -> {
                mList.addAll(constantsCategories.allCategoriesForStoba(this))
            }
            sessionManager.getString(KEY_LOGIN_SUBDOMAIN).equals(
                CustomerSpaces.aicher.name,
                ignoreCase = true
            ) -> {
                mList.addAll(constantsCategories.allCategoriesForAicher(this))
            }
            sessionManager.getString(KEY_LOGIN_SUBDOMAIN).equals(
                CustomerSpaces.qa4.name,
                ignoreCase = true
            ) -> {
                mList.addAll(constantsCategories.allCategoriesForQA4(this))
            }
            else -> {
                mList.addAll(constantsCategories.all(this))
            }
        }
        setObservers()
        setListeners()
    }

    private fun setListeners() {
        bDone?.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(
                KEY_SELECTED_CATEGORIES,
                adapter?.getSelected()
            )
            setResult(Activity.RESULT_OK, returnIntent)
            finish()

        }

        ivBack?.setOnClickListener {
            onBackPressed()
        }

        ivSearch?.setOnClickListener {
            textView3?.visibility = View.INVISIBLE
            ivBack?.visibility = View.INVISIBLE
            ivBack2?.visibility = View.VISIBLE
            ivSearch?.visibility = View.INVISIBLE
            etSearch?.visibility = View.VISIBLE
            etSearch?.isEnabled = true
            etSearch?.visibility = View.VISIBLE
            etSearch?.requestFocus()
            showKeyboard(etSearch)

        }

        ivBack2.setOnClickListener {
            textView3?.visibility = View.VISIBLE
            ivSearch?.visibility = View.VISIBLE
            etSearch?.visibility = View.INVISIBLE
            ivBack2?.visibility = View.INVISIBLE
            ivBack?.visibility = View.VISIBLE
            etSearch.text.clear()
            etSearch?.isEnabled = false
            adapter?.updateList(mList as ArrayList<String>)
        }

        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }

    private fun showKeyboard(etSearch: EditText) {
        etSearch.requestFocus()
        etSearch.postDelayed(Runnable {
            val keyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(etSearch, 0)
        }, 200)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<String> = ArrayList()
        mList.let {
            for (item in it) {
                if (item.toLowerCase(Locale.ROOT)
                        .contains(text.toLowerCase(Locale.ROOT))
                ) {
                    filteredList.add(item)
                }
            }
        }
        adapter?.updateList(filteredList)
    }

    private fun selectedAssetCount(multiSelectItemCount: java.util.ArrayList<String>?) {
        bDone?.text = ConnectavoUtils.makeSpannableText(
            arrayListOf("DONE\n", "${multiSelectItemCount?.size ?: 0} selected"),
            arrayListOf(Typeface.BOLD, Typeface.NORMAL),
            arrayListOf(1.3f, 1f)
        )
    }

    private fun setObservers() {
        val selectedTeams =
            intent?.getStringArrayListExtra(KEY_SELECTED_CATEGORIES) as ArrayList<String>?
        if (selectedTeams != null) {
            selectedAssetCount(selectedTeams)
        }
        adapter = selectedTeams?.let {
            CategoriesAdapter(
                mList as ArrayList<String>,
                it
            ) { multiSelectItemCount -> selectedAssetCount(multiSelectItemCount) }
        }
        recyclerView.adapter = adapter
    }


    companion object {
        const val KEY_SELECTED_CATEGORIES = "selected_categories"
    }
}
