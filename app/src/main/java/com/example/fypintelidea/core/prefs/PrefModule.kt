package com.example.fypintelidea.core.prefs

import android.content.Context
import com.example.fypintelidea.core.session.SessionManager
import org.koin.dsl.module

val prefModule = module {
    fun getPrefManager(context: Context): SessionManager {
        return SessionManager(context)
    }

    single { getPrefManager(get()) }
}