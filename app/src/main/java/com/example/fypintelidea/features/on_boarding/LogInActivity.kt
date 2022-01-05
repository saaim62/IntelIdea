package com.example.fypintelidea.features.on_boarding

import android.R.attr.*
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.connectavo.app.connectavo_android.core.*
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.MyURLs
import com.example.fypintelidea.core.providers.models.Permission
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.session.SessionManager.Companion.KEY_LOGIN_SUBDOMAIN
import com.example.fypintelidea.core.utils.AssetsUtility
import com.example.fypintelidea.core.providers.models.UserLogin
import com.example.fypintelidea.core.providers.models.UserLoginRequest
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_asset_details.*
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import java.util.*

class LogInActivity : ConnectavoBaseActivity() {
    val sessionManager: SessionManager by inject()
    private val loginViewModel: LoginViewModel by inject()
    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT) {
                        Toast.makeText(
                            this@LogInActivity,
                            "You have failed multiple times",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if (errorCode == BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT_PERMANENT) {
                        Toast.makeText(
                            this@LogInActivity,
                            "Due to multiple failed times Biometric is disabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val bundle = intent.extras
                    val sharedPref = getPreferences(Context.MODE_PRIVATE)
                    val email = sharedPref.getString(SessionManager.KEY_LOGIN_EMAIL, null)
                    val password = sharedPref.getString(SessionManager.KEY_LOGIN_PASSWORD, null)
                    etEmail.setText(email)
                    etPassword.setText(password)
                    val mSubDomain = bundle?.getString(WORKSPACE_NAME)
                    mSubDomain?.let {
                        if (email != null && password != null) {
                            progressBarLogin?.visibility = View.VISIBLE
                            loginViewModel.login(getLoginRequest(email, password, mSubDomain))
                            setObservers()
                        }
                    }
                }
            }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager.put(KEY_LOGIN_SUBDOMAIN, "qa4")

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val email = sharedPref.getString(SessionManager.KEY_LOGIN_EMAIL, null)
        val password = sharedPref.getString(SessionManager.KEY_LOGIN_PASSWORD, null)

        if (FingerprintManagerCompat.from(this@LogInActivity).isHardwareDetected
            && FingerprintManagerCompat.from(this@LogInActivity).hasEnrolledFingerprints()
        ) {
            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                clBiometric?.visibility = View.VISIBLE
            } else {
                clBiometric?.visibility = View.GONE
            }
        } else {
            clBiometric?.visibility = View.GONE
        }

        loginButtonLoginScreen?.isEnabled = false
        loginButtonLoginScreen?.setBackgroundResource(R.drawable.shape_rectangle_grey_radius_5dp)

        etEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (etEmail?.text.toString().isEmpty() || etPassword?.text.toString().isEmpty()) {
                    loginButtonLoginScreen?.isEnabled = false
                    loginButtonLoginScreen?.setBackgroundResource(R.drawable.shape_rectangle_grey_radius_5dp)
                } else {
                    loginButtonLoginScreen?.isEnabled = true
                    loginButtonLoginScreen?.setBackgroundResource(R.drawable.shape_rectangle_dark_blue_radius_10dp)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        etPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (etEmail?.text.toString().isEmpty() || etPassword?.text.toString().isEmpty()) {
                    loginButtonLoginScreen?.isEnabled = false
                    loginButtonLoginScreen?.setBackgroundResource(R.drawable.shape_rectangle_grey_radius_5dp)
                } else {
                    loginButtonLoginScreen?.isEnabled = true
                    loginButtonLoginScreen?.setBackgroundResource(R.drawable.shape_rectangle_dark_blue_radius_10dp)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        loginButtonLoginScreen?.setOnClickListener {
            var emailVerified = true
            var passwordVerified = true
            val email = etEmail?.text.toString()
            val password = etPassword?.text.toString()
            val subDomain = "qa4"

            if (email.length < 6) {
                emailVerified = false
            }
            if (password.length < 2) {
                passwordVerified = false
            }

            if (emailVerified && passwordVerified) {
                progressBarLogin?.visibility = View.VISIBLE
                loginViewModel.login(getLoginRequest(email, password, subDomain))
            }
        }

        bReset.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(MyURLs.FORGOT_PASSWORD)
            startActivity(i)
        }

        checkBiometricSupport()

        clBiometric.setOnClickListener {
            val biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle(resources.getString(R.string.login))
                .setNegativeButton(
                    resources.getString(R.string.cancel),
                    this.mainExecutor,
                    { _, _ ->
                    }).build()

            biometricPrompt.authenticate(
                getCancellationSignal(),
                mainExecutor,
                authenticationCallback
            )
        }
        setObservers()
    }

    private fun fetchCompanyDetails() {
        loginViewModel.getCompanyDetails()
    }

    private fun fetchAssets() {
        loginViewModel.fetchAssetsFromServer()
    }

    private fun fetchUserProfile(user: String) {
        loginViewModel.fetchUserProfile(user)

    }

    private fun getLoginRequest(
        email: String,
        password: String,
        subDomain: String
    ): UserLoginRequest {
        return UserLoginRequest(UserLogin(email, password, subDomain))
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager: KeyguardManager =
            getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    private fun moveToDashboard() {
        startActivity(Intent(this@LogInActivity, DashboardActivity::class.java))
        finish()
    }

    private fun setObservers() {
        loginViewModel.loginObservable.observe(this) {
            if (it.error == null) {
                it.value.user_id?.let { it1 ->
                    it.value.auth_token?.let { it2 ->
                        it.value.email?.let { it3 ->
                            it.value.company_id?.let { it4 ->
                                sessionManager.loginUser(
                                    it1,
                                    it2,
                                    it3,
                                    it4,
                                )
                            }
                        }
                    }
                }
                fetchAssets()
            } else {
                progressBarLogin.visibility = View.GONE
                Toast.makeText(
                    this,
                    this.getString(R.string.invalid_email_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginViewModel.userProfileObservable.observe(this) {
            val apiResponse = it.value
            progressBarLogin.visibility = View.GONE

            apiResponse.id?.let { profileId ->
                Firebase.analytics.setUserId(profileId)
                FirebaseCrashlytics.getInstance().setUserId(profileId)
            }
            if (apiResponse.role != null) {
                sessionManager.put(SessionManager.KEY_LOGIN_ROLE, apiResponse.role!!)
            }
            if (apiResponse.name != null) {
                sessionManager.put(SessionManager.KEY_LOGIN_NAME, apiResponse.name!!)
            }
            if (apiResponse.phone != null) {
                sessionManager.put(SessionManager.KEY_LOGIN_NUMBER, apiResponse.phone!!)
            }
            if (apiResponse.avatarUrl != null) {
                sessionManager.put(
                    SessionManager.KEY_LOGIN_IMAGEPATH,
                    apiResponse.avatarUrl!!
                )
            }
            if (apiResponse.teamIds != null) {
                sessionManager.saveArrayList(
                    SessionManager.KEY_LOGIN_TEAM_IDS,
                    apiResponse.teamIds
                )
            }
            if (SessionManager.USER_ROLE_ADMIN.equals(
                    sessionManager.getString(SessionManager.KEY_LOGIN_ROLE),
                    ignoreCase = true
                ) || sessionManager.getString(SessionManager.USER_ROLE_TEAMLEADER).equals(
                    sessionManager.getString(SessionManager.KEY_LOGIN_ROLE),
                    ignoreCase = true
                )
            ) {
                fetchCompanyDetails()
            } else {
                storeBiometric()
            }
        }

        loginViewModel.companyDetailsObservable.observe(this) {
            val apiResponse = it.value
            progressBarLogin.visibility = View.GONE
            sessionManager.put(SessionManager.KEY_COMPANY_TYPE, apiResponse.cType)
            storeBiometric()
        }


        loginViewModel.assetObservable.observe(this) {
            val apiResponse = it.value
            AssetsUtility.assetListHigh = apiResponse
            sessionManager.getString(SessionManager.KEY_LOGIN_ID)
                ?.let { it1 -> fetchUserProfile(it1) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun storeBiometric() {
        val builder = AlertDialog.Builder(this@LogInActivity)
        builder.setTitle("Alert")
        builder.setMessage(resources.getString(R.string.do_you_want_to_enable_biometric_authentication))
        builder.setPositiveButton(resources.getString(R.string.sure)) { _, _ ->
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(SessionManager.KEY_LOGIN_EMAIL, etEmail?.text.toString())
                putString(SessionManager.KEY_LOGIN_PASSWORD, etPassword?.text.toString())
                apply()
            }
            clBiometric.visibility = View.VISIBLE
            moveToDashboard()
        }

        builder.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
            clBiometric.visibility = View.GONE
            moveToDashboard()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val email = sharedPref.getString(SessionManager.KEY_LOGIN_EMAIL, null)
        val password = sharedPref.getString(
            SessionManager.KEY_LOGIN_PASSWORD,
            null
        )
        if (FingerprintManagerCompat.from(this@LogInActivity).isHardwareDetected
            && FingerprintManagerCompat.from(this@LogInActivity).hasEnrolledFingerprints()
        ) {
            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                alertDialog.dismiss()
                clBiometric.visibility = View.VISIBLE
                moveToDashboard()
            } else {
                alertDialog.show()
            }
        } else {
            moveToDashboard()
        }
    }

    companion object {
        const val WORKSPACE_NAME = "workspace_name"
    }
}
