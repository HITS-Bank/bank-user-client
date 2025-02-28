package ru.hitsbank.clientbankapplication.core.domain.model

import kotlin.String

data class LoginRequestEntity(
    val email: String,
    val password: String,
)
