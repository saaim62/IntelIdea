package com.example.fypintelidea.core

import android.content.Context
import com.example.fypintelidea.core.session.SessionManager
import com.example.fypintelidea.core.session.SessionManager.Companion.KEY_LOGIN_SUBDOMAIN

object MyURLs {

    fun getBaseUrl(context: Context): String {
        val sessionManager = SessionManager(context)
        return if (!sessionManager.getString(KEY_LOGIN_SUBDOMAIN).isNullOrEmpty()) {
            "https://" + sessionManager.getString(KEY_LOGIN_SUBDOMAIN) + ".connectavo.com"
        } else {
            "https://androidstage.connectavo.com"
        }
    }

    const val LEARN_MORE = "https://www.connectavo.com"
    const val FORGOT_PASSWORD = "https://prod.connectavo.com/users/password/new"
    const val SUBDOMAIN = "subDomain"
}