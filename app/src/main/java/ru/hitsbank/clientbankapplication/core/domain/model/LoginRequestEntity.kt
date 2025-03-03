package ru.hitsbank.clientbankapplication.core.domain.model

data class LoginRequestEntity(
    val email: String,
    val password: String,
)
