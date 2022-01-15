package com.example.fypintelidea.features.workOrder.workOrderCompletion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.providers.models.SpareParts
import com.example.fypintelidea.core.providers.models.User
import com.example.fypintelidea.features.filters.SingleSelectComponentActivityViewModel
import com.example.fypintelidea.features.filters.SingleSelectComponentsAdapter
import kotlinx.android.synthetic.main.activity_single_select_spare_parts.*
import org.koin.android.ext.android.inject
import java.io.Serializable

class SingleSelectComponentActivity : ConnectavoBaseActivity() {
    private val singleSelectComponentActivityViewModel: SingleSelectComponentActivityViewModel by inject()
    val filteredList: ArrayList<Any> = ArrayList()
    private var userList: ArrayList<User> = ArrayList()
    private var sparePartList: ArrayList<SpareParts> = ArrayList()
    var singleSelectComponentsAdapter: SingleSelectComponentsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_select_spare_parts)

        rvSpareParts.adapter = singleSelectComponentsAdapter

        intent.extras?.getString(KEY_ENTITY)?.let {
            when (it) {
                ENTITY_SPARE_PART -> {
                    tvTitleBar.text = resources.getString(R.string.select_spare_parts)
                    singleSelectComponentActivityViewModel.getAllSpareParts()
                    progressBarSpareParts?.visibility = View.VISIBLE
                }
                ENTITY_CONTRIBUTOR -> {
                    tvTitleBar.text = resources.getString(R.string.select_contributors)
                    singleSelectComponentActivityViewModel.fetchAllUsers()
                    progressBarSpareParts?.visibility = View.VISIBLE
                }
                ENTITY_EMPLOYEE -> {
                    tvTitleBar.text = resources.getString(R.string.select_idea_creator)
                    singleSelectComponentActivityViewModel.fetchAllUsers()
                    progressBarSpareParts?.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        }

        setObservers()
        ivSearch.setOnClickListener {
            tvTitleBar?.visibility = View.INVISIBLE
            ivBack?.visibility = View.INVISIBLE
            ivBack2?.visibility = View.VISIBLE
            ivSearch?.visibility = View.INVISIBLE
            etSearch?.isEnabled = true
            etSearch?.visibility = View.VISIBLE
            etSearch?.requestFocus()
            showKeyboard(etSearch)
        }

        ivBack?.setOnClickListener {
            onBackPressed()
        }

        ivBack2.setOnClickListener {
            tvTitleBar?.visibility = View.VISIBLE
            ivSearch?.visibility = View.VISIBLE
            etSearch?.visibility = View.INVISIBLE
            ivBack2?.visibility = View.INVISIBLE
            ivBack?.visibility = View.VISIBLE
            etSearch?.text?.clear()
            etSearch?.isEnabled = false
            if (!userList.isNullOrEmpty()) {
                singleSelectComponentsAdapter?.updateList(userList)
            } else if (!sparePartList.isNullOrEmpty()) {
                singleSelectComponentsAdapter?.updateList(sparePartList)
            } else {
                singleSelectComponentsAdapter?.updateList(sparePartList)
            }
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }

    private fun showKeyboard(etSearch: EditText) {
        etSearch.requestFocus()
        etSearch.postDelayed({
            val keyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(etSearch, 0)
        }, 200)
    }

    private fun filter(text: String) {
        filteredList.clear()
        if (!userList.isNullOrEmpty()) {
            for (item in userList) {
                if (item.name?.toLowerCase()?.contains(text.toLowerCase()) == true) {
                    filteredList.add(item)
                }
            }
        } else {
            for (item in sparePartList) {
                if (item.name?.toLowerCase()?.contains(text.toLowerCase()) == true) {
                    filteredList.add(item)
                }
            }
        }
        singleSelectComponentsAdapter?.updateList(filteredList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setObservers() {
        singleSelectComponentActivityViewModel.sparePartsObservable.observe(this) {
            progressBarSpareParts?.visibility = View.GONE
            if (it.error != null) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.something_wrong) + " while fetching spare parts",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val apiResponse = it.value
                val linearLayoutManager = LinearLayoutManager(this)
                rvSpareParts.layoutManager = linearLayoutManager
                sparePartList = apiResponse?.spareParts as ArrayList<SpareParts>
                singleSelectComponentsAdapter = SingleSelectComponentsAdapter(
                    apiResponse.spareParts as ArrayList<SpareParts>,
                    { selectedSparePart -> selectedSparePart(selectedSparePart) },
                    { selectedUser -> selectedUser(selectedUser) })
                rvSpareParts.adapter = singleSelectComponentsAdapter
            }
        }
        singleSelectComponentActivityViewModel.userObservable.observe(this) {
            progressBarSpareParts?.visibility = View.GONE
            if (it.error != null) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.something_wrong) + " while fetching users",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val apiResponse = it.value
                val linearLayoutManager = LinearLayoutManager(this)
                rvSpareParts.layoutManager = linearLayoutManager
                userList = apiResponse as ArrayList<User>
                singleSelectComponentsAdapter = SingleSelectComponentsAdapter(
                    apiResponse,
                    { selectedSparePart -> selectedSparePart(selectedSparePart) },
                    { selectedUser -> selectedUser(selectedUser) })
                rvSpareParts.adapter = singleSelectComponentsAdapter
            }
        }
    }

    private fun selectedSparePart(selectedSparePart: SpareParts) {
        val returnIntent = Intent()
        selectedSparePart.quantity = "1"
        returnIntent.putExtra(SELECTED_COMPONENT, selectedSparePart as Serializable)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun selectedUser(selectedUser: User) {
        val returnIntent = Intent()
        returnIntent.putExtra(SELECTED_COMPONENT, selectedUser as Serializable)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    companion object {
        const val KEY_ENTITY = "key_entity"
        const val ENTITY_SPARE_PART = "entity_sparePart"
        const val ENTITY_CONTRIBUTOR = "entity_contributor"
        const val ENTITY_EMPLOYEE = "entity_employee"
        const val ENTITY_CUSTOM_FIELD = "entity_custom_field"
        const val SELECTED_COMPONENT = "selected_spare_part"
        const val SELECTED_EMPLOYEE = "selected_employee"

    }
}