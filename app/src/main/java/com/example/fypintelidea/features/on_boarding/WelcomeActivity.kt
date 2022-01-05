package com.example.fypintelidea.features.on_boarding

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.fypintelidea.R
import com.example.fypintelidea.core.MyURLs
import com.example.fypintelidea.core.session.SessionManager
import kotlinx.android.synthetic.main.activity_welcome.*
import org.koin.android.ext.android.inject

class WelcomeActivity : AppCompatActivity() {
    val sessionManager: SessionManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        fa = this
        if (!sessionManager.getString(SessionManager.KEY_LOGIN_TOKEN).isNullOrEmpty()) {
            startActivity(Intent(applicationContext, DashboardActivity::class.java))
            finish()
        }
        ivLogo?.clearAnimation()
        tvText?.clearAnimation()
        val animation = AnimationUtils.loadAnimation(this, R.anim.translate)
        animation.reset()
        ivLogo?.startAnimation(animation)
        tvText?.startAnimation(animation)
        val alpha = 50
        tvAlreadyAccount?.setTextColor(Color.argb(alpha, 0, 0, 0))
//        bContinue?.paintFlags = bContinue.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        bContinue?.setOnClickListener { v ->
            val intent = Intent(this@WelcomeActivity, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        bLearnMore?.setOnClickListener { v ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(MyURLs.LEARN_MORE)
            startActivity(i)
        }
    }

    companion object {
        var fa: Activity? = null
    }
}
