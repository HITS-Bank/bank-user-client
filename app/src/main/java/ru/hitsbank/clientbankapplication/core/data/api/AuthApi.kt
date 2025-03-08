package ru.hitsbank.clientbankapplication.core.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import ru.hitsbank.clientbankapplication.core.data.model.LoginRequest
import ru.hitsbank.clientbankapplication.core.data.model.RefreshRequest
import ru.hitsbank.clientbankapplication.core.data.model.TokenResponse

interface AuthApi {

    // TODO
//    @POST("users/auth/login")
    @POST("auth/login")
    suspend fun login(
        @Query("channel") channel: String,
        @Body request: LoginRequest,
    ): Response<TokenResponse>

    // TODO
//    @POST("users/auth/refresh")
    @POST("auth/refresh")
    suspend fun refresh(
        @Header("Authorization") expiredToken: String,
        @Body request: RefreshRequest,
    ): Response<TokenResponse>
}