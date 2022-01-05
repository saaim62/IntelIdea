package com.example.fypintelidea.core.base.modules

import com.example.fypintelidea.core.base.models.MainRepo
import com.example.fypintelidea.features.on_boarding.LoginViewModel
import com.example.fypintelidea.features.on_boarding.DashboardActivityViewModel
import com.example.fypintelidea.features.on_boarding.SplashScreenViewModel
import com.example.fypintelidea.features.profile.ProfileActivityViewModel
import com.example.fypintelidea.features.profile.ProfileEditActivityViewModel
import com.example.fypintelidea.features.workOrder.WorkOrdersFragmentTabViewModel
import com.example.fypintelidea.features.activities.TagsActivityViewModel
import com.example.fypintelidea.features.activities.UsersActivityViewModel
import com.example.fypintelidea.features.filters.SingleSelectComponentActivityViewModel
import com.example.fypintelidea.features.workOrder.workOrderCompletion.WorkOrderCompleteSignatureActivityViewModel
import com.example.fypintelidea.features.workOrder.workOrderDetails.WorkOrderDetailActivityViewModel
import com.example.fypintelidea.features.workOrder.workOrderDetails.detailTab.WorkOrderDetailsFragmentViewModel
import com.example.fypintelidea.features.workOrder.workorderguide.WorkOrderGuideFragmentViewModel
import com.example.fypintelidea.features.search.SearchActivityViewModel
import com.example.fypintelidea.features.workOrder.newWorkorder.NewWorkOrderActivityViewModel
import com.example.fypintelidea.features.workOrder.workOrderCompletion.WorkOrderCompleteActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountViewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { SplashScreenViewModel(get()) }
    viewModel { ProfileActivityViewModel(get()) }
    viewModel { ProfileEditActivityViewModel(get()) }
    viewModel { UsersActivityViewModel(get()) }
    viewModel { DashboardActivityViewModel(get()) }
    viewModel { TagsActivityViewModel(get()) }
    viewModel { WorkOrderGuideFragmentViewModel(get()) }
    viewModel { SearchActivityViewModel(get()) }
    viewModel { SingleSelectComponentActivityViewModel(get()) }
    viewModel { NewWorkOrderActivityViewModel(get()) }
    viewModel { WorkOrderCompleteSignatureActivityViewModel(get()) }
    viewModel { WorkOrderCompleteActivityViewModel(get()) }
    viewModel { WorkOrderDetailsFragmentViewModel(get()) }
    viewModel { WorkOrderDetailActivityViewModel(get()) }
    viewModel { WorkOrdersFragmentTabViewModel(get()) }
}

val accountRepoModule = module {
    factory { MainRepo(get(), get()) }
}