package ru.hitsbank.clientbankapplication.core.domain.model

import kotlin.String

data class TokenResponseEntity(
    val accessToken: String,
    val accessTokenExpiresAt: String,
    val refreshToken: String,
    val refreshTokenExpiresAt: String,
)