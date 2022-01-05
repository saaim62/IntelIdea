package com.example.fypintelidea.core.modules

import android.content.Context
import com.example.fypintelidea.core.services.ApiManager
import org.koin.dsl.module

val apiModule = module {

    fun getAPIManger(context: Context): ApiManager {
        return ApiManager(context)
    }

    single { getAPIManger(get()) }
}