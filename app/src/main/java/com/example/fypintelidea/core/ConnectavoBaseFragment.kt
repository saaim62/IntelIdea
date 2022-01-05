package com.example.fypintelidea.core

import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.fypintelidea.core.services.ApiCallbacks
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.ConnectavoBaseActivity.Companion.REQUEST_EXIT
import com.example.fypintelidea.features.profile.ProfileActivity
import okhttp3.Headers


abstract class ConnectavoBaseFragment : Fragment(), ApiCallbacks {

    override fun doBeforeApiCall() {
    }

    override fun doAfterApiCall() {
    }

    override fun onApiFailure(errorCode: Int) {
//        activity?.let { AlertDialogProvider.showFailureDialog(it) }
    }

    override fun onApiSuccess(
        apiResponse: ConnectavoBaseApiResponse,
        errorCode: Int,
        hearders: Headers
    ) {
    }

    internal fun goToProfile() {
        activity?.startActivityForResult(Intent(context, ProfileActivity::class.java), REQUEST_EXIT)
    }
}