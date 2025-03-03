package ru.hitsbank.clientbankapplication.core.data.api

import retrofit2.Response
import retrofit2.http.GET
import ru.hitsbank.clientbankapplication.core.data.model.ProfileResponse

interface ProfileApi {

    @GET("/profile")
    suspend fun getSelfProfile(): Response<ProfileResponse>
}