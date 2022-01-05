package com.example.fypintelidea.features.on_boarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.permissions.PermissionsHelper
import com.example.fypintelidea.core.providers.models.Permission
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.features.workOrder.WorkOrdersFragmentTab
import com.example.fypintelidea.features.workOrder.newWorkorder.NewWorkorderActivity
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject

class DashboardActivity : ConnectavoBaseActivity() {
    val sessionManager: SessionManager by inject()
    private val dashboardActivityViewModel: DashboardActivityViewModel by inject()
    private var navigation: BottomNavigationView? = null
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_workorders -> {
                    switchToFragmentWorkOrders()
                    return@OnNavigationItemSelectedListener true
                }
//                R.id.navigation_assets -> {
//                    switchToFragmentAssets()
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.navigation_requests -> {
//                    switchToFragmentRequests()
//                    return@OnNavigationItemSelectedListener true
//                }
            }
            false
        }

    private fun switchToFragmentWorkOrders() {
        val manager = supportFragmentManager
        manager.beginTransaction()
            .replace(
                R.id.llFragmentContainer,
                WorkOrdersFragmentTab(),
                FRAGMENT_TAG_WORK_ORDERS
            )
            .commitAllowingStateLoss()
    }

//    private fun switchToFragmentAssets() {
//        val manager = supportFragmentManager
//        manager.beginTransaction()
//            .replace(R.id.llFragmentContainer, AssetsMainFragmentTab(), FRAGMENT_TAG_ASSETS)
//            .commitAllowingStateLoss()
//    }

//    private fun switchToFragmentRequests() {
//        val manager = supportFragmentManager
//        manager.beginTransaction()
//            .replace(R.id.llFragmentContainer, RequestsFragmentTab(), FRAGMENT_TAG_REQUESTS)
//            .commitAllowingStateLoss()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirst()
        setContentView(R.layout.activity_main)
        navigation = findViewById(R.id.navigation)
        navigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation?.itemIconTintList = null
        val floatingActionMenu =
            findViewById<FloatingActionMenu>(R.id.material_design_android_floating_action_menu)
        val floatingActionButtonWorkOrder =
            findViewById<FloatingActionButton>(R.id.material_design_floating_action_workorder)
        val floatingActionButtonRequest =
            findViewById<FloatingActionButton>(R.id.material_design_floating_action_request)
        floatingActionMenu.setClosedOnTouchOutside(true)
        floatingActionButtonWorkOrder.setOnClickListener { view: View ->
            floatingActionMenu.close(true)
            startActivity(Intent(view.context, NewWorkorderActivity::class.java))
        }
//        floatingActionButtonRequest.setOnClickListener { view: View ->
//            floatingActionMenu.close(true)
//            startActivity(Intent(view.context, NewRequestActivity::class.java))
//        }

        // TODO: 1/21/2019 hit API to fetch fresh permissions for now. (jugaar)
        dashboardActivityViewModel.getUserPermissions()

        dashboardActivityViewModel.getCompanyDetails()

        setObservers()
//
//        if (!PermissionsHelper.canViewRequest() && !PermissionsHelper.canViewWorkOrders() && !PermissionsHelper.canViewAssets()) {
//            navigation?.menu?.clear()
//            navigation?.inflateMenu(R.menu.navigation_workorder_requests_assets)
//            switchToFragmentWorkOrders()
//            return
//        } else if (PermissionsHelper.canViewWorkOrders() && PermissionsHelper.canViewAssets()) {
//            navigation?.menu?.clear()
//            navigation?.inflateMenu(R.menu.navigation_requests)
//            switchToFragmentRequests()
//        }

            navigation?.menu?.clear()
            navigation?.inflateMenu(R.menu.navigation_workorder_assets)
            switchToFragmentWorkOrders()
//
//         if (PermissionsHelper.canViewRequest() && PermissionsHelper.canViewAssets()) {
//            navigation?.menu?.clear()
//            navigation?.inflateMenu(R.menu.navigation_workorders)
//            switchToFragmentWorkOrders()
//        }
//        else if (!PermissionsHelper.canViewRequest() && !PermissionsHelper.canViewAssets()) {
//            navigation?.menu?.clear()
//            navigation?.inflateMenu(R.menu.navigation_requests_assets)
//            switchToFragmentRequests()
//        } else if (PermissionsHelper.canViewAssets() && PermissionsHelper.canViewWorkOrders()) {
//            navigation?.menu?.clear()
//            navigation?.inflateMenu(R.menu.navigation_requests)
//            switchToFragmentRequests()
//        }
//        else if (!PermissionsHelper.canViewAssets() && !PermissionsHelper.canViewWorkOrders()) {
//            navigation?.menu?.clear()
//            navigation?.inflateMenu(R.menu.navigation_workorder_assets)
//            switchToFragmentWorkOrders()
//        }
//        else if (PermissionsHelper.canViewRequest() && PermissionsHelper.canViewWorkOrders()) {
//            navigation?.menu?.clear()
//            navigation?.inflateMenu(R.menu.navigation_assets)
//            switchToFragmentAssets()
//        }
//        else if (!PermissionsHelper.canViewRequest() && !PermissionsHelper.canViewWorkOrders()) {
//            navigation?.menu?.clear()
//            navigation?.inflateMenu(R.menu.navigation_workorder_requests)
//            switchToFragmentWorkOrders()
//        }
//        else {
            // show toast and logout user
//            Toast.makeText(
//                this@DashboardActivity,
//                getString(R.string.no_privileges_signing_out),
//                Toast.LENGTH_LONG
//            ).show()
//            val handler = Handler()
//            handler.postDelayed({
//                sessionManager.logoutUser()
//                startActivity(Intent(this@DashboardActivity, WelcomeActivity::class.java))
//                finish()
//            }, 4000)
//        }
//        if (PermissionsHelper.canCreateWorkOrders()) {
//            floatingActionButtonWorkOrder.hideButtonInMenu(true)
//        }
//        if (!PermissionsHelper.canCreateRequest()) {
//            floatingActionButtonRequest.hideButtonInMenu(true)
//        }
//        if (PermissionsHelper.canCreateWorkOrders() && !PermissionsHelper.canCreateRequest()) {
//            floatingActionMenu?.visibility = View.GONE
//        }
    }

    private fun setObservers() {
        dashboardActivityViewModel.companyDetailsObservable.observe(this) {
            if (it.error != null) {
                Toast.makeText(this, "company detail api error", Toast.LENGTH_SHORT).show()
            } else {
                val apiResponse = it.value
                sessionManager.put(SessionManager.KEY_COMPANY_TYPE, apiResponse.cType)
            }
        }
        dashboardActivityViewModel.permissionObservable.observe(this) {
            val apiResponse = it.value
            var foundDifferenceInLocalAndServerPermissions = false
            apiResponse?.permissions?.forEach { onePermission ->
//                val id = onePermission.id
//                val name = onePermission.name
//                val tempPermission = Permission.getPermissionByServerId(id)
//                if (tempPermission == null) {
//                    foundDifferenceInLocalAndServerPermissions = true
//                    val newPermission = Permission(id, name)
//                    newPermission.save()
//                }
            }
//            if (Permission.count<Any>(Permission::class.java) != apiResponse?.permissions?.size
//                    ?.toLong()
//            ) {
//                foundDifferenceInLocalAndServerPermissions = true
//                // delete all
//                if (Permission.count<Any>(Permission::class.java) > 0) {
//                    Permission.deleteAll(Permission::class.java)
//                }
//                 and re-save all from server
//                apiResponse?.permissions?.let { onePermission ->
//                    for (i in onePermission.size - 1 downTo 0) {
//                        val jsonObjectOnePermission = onePermission[i]
//                        val id = jsonObjectOnePermission.id
//                        val name = jsonObjectOnePermission.name
//                        val newPermission = Permission(id, name)
//                        newPermission.save()
//                    }
//                }
            }

//            if (foundDifferenceInLocalAndServerPermissions) {
//                Toast.makeText(
//                    this,
//                    "Privileges changed, please re-login the app",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
    }

    private fun initFirst() {
        SessionManager(
            applicationContext
        )
        if (sessionManager.getString(SessionManager.KEY_LOGIN_TOKEN).isNullOrEmpty()) {
            startActivity(Intent(applicationContext, LogInActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                finish()
            }
        }
    }

    companion object {
        private const val FRAGMENT_TAG_WORK_ORDERS = "WorkOrdersFragmentTab"
        private const val FRAGMENT_TAG_ASSETS = "AssetsMainFragmentTab"
        private const val FRAGMENT_TAG_REQUESTS = "RequestsFragmentTab"
    }
}