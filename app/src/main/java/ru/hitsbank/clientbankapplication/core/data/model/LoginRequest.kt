package ru.hitsbank.clientbankapplication.core.data.model

data class LoginRequest(
    val email: String,
    val password: String,
)