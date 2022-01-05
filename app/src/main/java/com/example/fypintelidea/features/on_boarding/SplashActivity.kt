package com.example.fypintelidea.features.on_boarding

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.fypintelidea.R
import com.example.fypintelidea.core.ConnectavoBaseActivity
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.utils.AssetsUtility
import com.example.fypintelidea.core.utils.UsersUtility
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject
import java.util.*

class SplashActivity : ConnectavoBaseActivity() {
    val sessionManager: SessionManager by inject()
    private val splashScreenViewModel: SplashScreenViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //not going to use this feature in near future.
        val ids = intArrayOf(R.drawable.intelidea_with_icon, R.drawable.intelidea_with_icon)
        val randomGenerator = Random()
        val r = randomGenerator.nextInt(ids.size)
        ivSplashLogo?.setImageDrawable(ContextCompat.getDrawable(this, ids[r]))
        val anim = AnimationUtils.loadAnimation(this, R.anim.alpha)
        anim.reset()
        ivSplashLogo?.clearAnimation()
        ivSplashLogo?.startAnimation(anim)
        val anim2 = AnimationUtils.loadAnimation(this, R.anim.translate)
        anim2.reset()
        tvSmartMaintenance?.clearAnimation()
        tvSmartMaintenance?.startAnimation(anim2)

        splashScreenViewModel.fetchAssetsFromServer()
        splashScreenViewModel.fetchAllUsers()
        setObserver()
    }

    private fun setObserver() {
        if (!sessionManager.getString(SessionManager.KEY_LOGIN_TOKEN)
                .isNullOrEmpty()
        ) {
            splashScreenViewModel.assetObservable.observe(this) {
                if (it.error != null) {
                    Toast.makeText(this, "Sorry couldn't fetch the assets list", Toast.LENGTH_SHORT)
                        .show()
                    onDestroy()
                } else {
                    val apiResponse = it.value
                    if (apiResponse != null) {
                        AssetsUtility.assetListHigh = apiResponse
                    } else {
                        Toast.makeText(
                            this,
                            "Api response is empty So App is Closing Now",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                    val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()

                }
            }
            splashScreenViewModel.getAllUsersObservable.observe(this) {
                if (it.error != null) {
                    Toast.makeText(this, "Sorry couldn't fetch the users list", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val apiResponse = it.value
                    UsersUtility.userList = apiResponse
                }
            }
        } else {
            val mCountDownTimer: CountDownTimer =
                object : CountDownTimer(SPLASH_TIME_OUT.toLong(), 3000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }
                }
            mCountDownTimer.start()
        }
    }

    companion object {
        const val SPLASH_TIME_OUT = 1500
    }
}