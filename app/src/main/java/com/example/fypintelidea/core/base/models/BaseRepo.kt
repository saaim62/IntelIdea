package com.example.fypintelidea.core.base.models

import com.example.fypintelidea.core.services.ApiManager
import com.example.fypintelidea.core.session.SessionManager


open class BaseRepo(private val apiManager: ApiManager, private val sessionManager: SessionManager)