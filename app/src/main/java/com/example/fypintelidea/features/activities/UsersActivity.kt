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
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.ConnectavoUtils
import com.example.fypintelidea.core.providers.models.User
import com.example.fypintelidea.features.adapters.UserAdapter
import kotlinx.android.synthetic.main.activity_users.*
import org.koin.android.ext.android.inject
import java.util.*

class UsersActivity : ConnectavoBaseActivity() {
    private val usersActivityViewModel: UsersActivityViewModel by inject()
    private var mList: List<User>? = null
    var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        fetchAllUsers()
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        bDone?.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(
                KEY_SELECTED_USERS,
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
            adapter?.updateList(mList as ArrayList<User>)
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

    private fun selectedAssetCount(multiSelectItemCount: ArrayList<String>?) {
        bDone?.text = ConnectavoUtils.makeSpannableText(
            arrayListOf("DONE\n", "${multiSelectItemCount?.size ?: 0} selected"),
            arrayListOf(Typeface.BOLD, Typeface.NORMAL),
            arrayListOf(1.3f, 1f)
        )
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<User> = ArrayList()
        mList?.let {
            for (item in it) {
                if (item.name?.toLowerCase(Locale.ROOT)
                        ?.contains(text.toLowerCase(Locale.ROOT)) == true
                ) {
                    filteredList.add(item)
                }
            }
        }
        adapter?.updateList(filteredList)
    }

    private fun fetchAllUsers() {
        progressBar?.visibility = View.VISIBLE
        usersActivityViewModel.fetchAllUsers()
    }

    private fun setObservers() {
        usersActivityViewModel.usersObservable.observe(this) { response ->
            progressBar?.visibility = View.GONE
            mList = response.value

            if (response.error == null) {
                val selectedUsers =
                    intent?.getStringArrayListExtra(KEY_SELECTED_USERS) as ArrayList<String>?
                if (selectedUsers != null) {
                    selectedAssetCount(selectedUsers)
                }
                adapter = selectedUsers?.let {
                    UserAdapter(
                        response.value,
                        it
                    ) { multiSelectItemCount -> selectedAssetCount(multiSelectItemCount) }
                }
                recyclerView.adapter = adapter
                mList = response.value
            }
        }
    }

    companion object {
        const val KEY_SELECTED_USERS = "selected_users"
    }
}
