package com.example.fypintelidea.core.koin

import com.example.fypintelidea.core.base.modules.accountRepoModule
import com.example.fypintelidea.core.base.modules.accountViewModelModule
import com.example.fypintelidea.core.base.modules.baseRepoModule
import com.example.fypintelidea.core.base.modules.baseViewModelModule
import com.example.fypintelidea.core.modules.apiModule
import com.example.fypintelidea.core.prefs.prefModule
import org.koin.core.module.Module


fun getListOfModules(): List<Module> {

    return (listOf(
        apiModule,
        prefModule,
        accountRepoModule,
        accountViewModelModule,
        baseRepoModule,
        baseViewModelModule
    ))
}