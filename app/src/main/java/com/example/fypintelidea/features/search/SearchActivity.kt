package com.example.fypintelidea.features.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.getTrimmedText
import com.example.fypintelidea.core.providers.models.Asset
import com.example.fypintelidea.core.providers.models.Workorder
import com.example.fypintelidea.features.workOrder.workOrderDetails.WorkOrderDetailActivity
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject
import java.io.Serializable
import java.util.*

class SearchActivity : ConnectavoBaseActivity() {
    private val searchActivityViewModel: SearchActivityViewModel by inject()
    var searchAdapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchAdapter =
            SearchAdapter(arrayListOf<Any>(), { workOrder -> selectedWorkOrder(workOrder) },
                { selectedAsset -> selectedAsset(selectedAsset) }
            )

        rvSearch?.adapter = searchAdapter
        setSearchTextChangedListener()

        if (etSearch.isEnabled) {
            etSearch?.visibility = View.VISIBLE
            rvSearch?.visibility = View.VISIBLE
            etSearch?.requestFocus()
            showKeyboard(etSearch)
        }

        ivBack?.setOnClickListener {
            finish()
        }
    }

    private fun showKeyboard(etSearch: EditText) {
        etSearch?.requestFocus()
        etSearch?.postDelayed(Runnable {
            val keyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(etSearch, 0)
        }, 200)
    }

    private fun setSearchTextChangedListener() {
        etSearch.doOnTextChanged { _, _, _, _ ->
            val searchQuery = etSearch.getTrimmedText()
            when {
                searchQuery.isEmpty() -> {
                    ivTypo?.visibility = View.VISIBLE
                    tvTypo?.visibility = View.VISIBLE
                    searchAdapter?.updateList(arrayListOf<Any>())
                }
                else -> {
                    if (searchQuery.isNotEmpty()) {
                        getSearchList(searchQuery)
                    }
                }
            }
        }
    }

    private fun selectedAsset(selectedAsset: Asset) {
//        val i = Intent(this@SearchActivity, AssetDetailsActivity::class.java)
//        i.putExtra(AssetDetailsActivity.SELECTED_ASSET, selectedAsset)
//        i.putExtra(AssetDetailsActivity.CHECK_ASSET, true)
//        startActivity(i)
    }

    private fun selectedWorkOrder(workOrder: Workorder) {
        val i = Intent(this@SearchActivity, WorkOrderDetailActivity::class.java)
        i.putExtra(WorkOrderDetailActivity.SELECTED_WORK_ORDER, workOrder as Serializable?)
        i.putExtra(WorkOrderDetailActivity.IS_FORCE_REFRESH, true)
        startActivity(i)
    }

    private fun getSearchList(search: String) {
        searchActivityViewModel.getSearchList(search)
        setObservers()
    }

    private fun setObservers() {
        searchActivityViewModel.searchObservable.observe(this) {
            if (it.error != null) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.sorry_something_went_wrong),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val apiResponse = it.value
                ivTypo?.visibility = View.GONE
                tvTypo?.visibility = View.GONE
                val totalList = arrayListOf<Any>()
                apiResponse.assets?.let { assetList ->
                    totalList.addAll(assetList)
                }
                apiResponse.workOrders?.let { workOrderList ->
                    totalList.addAll(workOrderList)
                }
                searchAdapter?.updateList(totalList as ArrayList<*>)
            }
        }
    }
}
