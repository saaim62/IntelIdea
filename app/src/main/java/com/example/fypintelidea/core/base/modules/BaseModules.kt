package com.example.fypintelidea.core.base.modules

import com.example.fypintelidea.core.base.models.BaseRepo
import com.example.fypintelidea.core.base.viewmodels.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseViewModelModule = module {
    viewModel { BaseViewModel(get()) }
}

val baseRepoModule = module {
    factory { BaseRepo(get(), get()) }
}