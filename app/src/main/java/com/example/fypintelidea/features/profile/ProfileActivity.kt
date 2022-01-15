package com.example.fypintelidea.features.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.providers.models.UserProfileResponse
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import com.example.fypintelidea.features.on_boarding.WelcomeActivity
import com.google.common.base.CaseFormat
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_new_template_request.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject

class ProfileActivity : ConnectavoBaseActivity() {
    val sessionManager: SessionManager by inject()
    private val profileActivityViewModel: ProfileActivityViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = resources.getString(R.string.profile)
            supportActionBar?.elevation = 0f
        }

        bLogout.setOnClickListener {
            sessionManager.logoutUser()
            setResult(Activity.RESULT_OK, null)
            startActivity(Intent(this@ProfileActivity, WelcomeActivity::class.java))
            finish()
        }

        fetchUserProfile(sessionManager.getString(SessionManager.KEY_LOGIN_ID))
        setObservers()
    }

    private fun fetchUserProfile(user: String?) {
        progressBarProfile?.visibility = View.VISIBLE
        profileActivityViewModel.fetchUserProfile(user)
    }

    fun setObservers() {
        profileActivityViewModel.userProfileObservable.observe(this) {
            val apiResponse = it.value
            progressBarProfile?.visibility = View.GONE
            if (apiResponse is UserProfileResponse) {
                progressBarProfile?.visibility = View.GONE
                apiResponse.id?.let { profileId ->
                    Firebase.analytics.setUserId(profileId)
                    FirebaseCrashlytics.getInstance().setUserId(profileId)
                }
                if (apiResponse.role != null) {
                    sessionManager.getString(SessionManager.KEY_LOGIN_ROLE, apiResponse.role!!)
                }
                if (apiResponse.name != null) {
                    sessionManager.getString(SessionManager.KEY_LOGIN_NAME, apiResponse.name!!)
                }
                if (apiResponse.phone != null) {
                    sessionManager.getString(SessionManager.KEY_LOGIN_NUMBER, apiResponse.phone!!)
                }
                if (apiResponse.avatarUrl != null) {
                    sessionManager.getString(
                        SessionManager.KEY_LOGIN_IMAGEPATH,
                        apiResponse.avatarUrl!!
                    )
                }
                populateData()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        populateData()
    }

    private fun populateData() {
        if (sessionManager.getString(SessionManager.KEY_LOGIN_IMAGEPATH) != null && !SessionManager(
                this
            ).getString(SessionManager.KEY_LOGIN_IMAGEPATH).equals(
                "",
                ignoreCase = true
            )
        ) {
            MyDateTimeStamp.setFrescoImage(
                ivPic,
                "https:" + sessionManager.getString(SessionManager.KEY_LOGIN_IMAGEPATH)
            )
        }
        if (sessionManager.getString(SessionManager.KEY_LOGIN_ROLE) != null && !SessionManager(
                this
            ).getString(SessionManager.KEY_LOGIN_ROLE).equals(
                "",
                ignoreCase = true
            )
        ) {
            tvRole?.text = CaseFormat.UPPER_UNDERSCORE.to(
                CaseFormat.UPPER_CAMEL,
                sessionManager.getString(SessionManager.KEY_LOGIN_ROLE)!!
                    .replace("_".toRegex(), " ")
            )
        }
        if (sessionManager.getString(SessionManager.KEY_LOGIN_NAME) != null && !SessionManager(
                this
            ).getString(SessionManager.KEY_LOGIN_NAME).equals(
                "",
                ignoreCase = true
            )
        ) {
            tvName?.text = sessionManager.getString(SessionManager.KEY_LOGIN_NAME)
        }
        if (sessionManager.getString(SessionManager.KEY_LOGIN_NUMBER) != null && !SessionManager(
                this
            ).getString(SessionManager.KEY_LOGIN_NUMBER).equals(
                "",
                ignoreCase = true
            )
        ) {
            tvNumber?.text = sessionManager.getString(SessionManager.KEY_LOGIN_NUMBER)
        } else {
            tvNumber?.visibility = View.GONE
        }
        tvEmail?.text = sessionManager.getString(SessionManager.KEY_LOGIN_EMAIL)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_edit -> {
                val i = Intent(applicationContext, ProfileEditActivity::class.java)
                startActivity(i)
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
