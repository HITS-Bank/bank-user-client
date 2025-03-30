package ru.hitsbank.clientbankapplication.core.data.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val accessTokenExpiresIn: Long,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("refresh_expires_in") val refreshTokenExpiresIn: Long,
)