package com.example.fypintelidea.core.providers.models

class UserLoginRequest(
    val user: UserLogin
)

class UserLogin(
    val email: String,
    val password: String,
    val subdomain: String
)