package ru.hitsbank.clientbankapplication.core.data.repository

import kotlinx.coroutines.Dispatchers
import ru.hitsbank.clientbankapplication.core.data.api.AuthApi
import ru.hitsbank.clientbankapplication.core.data.common.apiCall
import ru.hitsbank.clientbankapplication.core.data.common.toCompletableResult
import ru.hitsbank.clientbankapplication.core.data.common.toResult
import ru.hitsbank.clientbankapplication.core.data.datasource.SessionManager
import ru.hitsbank.clientbankapplication.core.data.mapper.AuthMapper
import ru.hitsbank.clientbankapplication.core.data.model.RefreshRequest
import ru.hitsbank.clientbankapplication.core.data.model.TokenType
import ru.hitsbank.clientbankapplication.core.domain.common.Completable
import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.model.LoginRequestEntity
import ru.hitsbank.clientbankapplication.core.domain.model.TokenResponseEntity
import ru.hitsbank.clientbankapplication.core.domain.repository.IAuthRepository

class AuthRepository(
    private val api: AuthApi,
    private val mapper: AuthMapper,
    private val sessionManager: SessionManager,
) : IAuthRepository {

    override suspend fun login(
        channel: String,
        request: LoginRequestEntity,
    ): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            api.login(channel, mapper.map(request))
                .toResult()
                .also { result ->
                    if (result is Result.Success) {
                        sessionManager.saveTokens(result.data)
                    }
                }
                .toCompletableResult()
        }
    }

    override suspend fun refresh(): Result<Completable> {
        val accessToken = sessionManager.fetchToken(TokenType.ACCESS)
        val refreshToken = sessionManager.fetchToken(TokenType.REFRESH)
        if (accessToken == null || refreshToken == null) {
            return Result.Error(Exception("could not retrieve tokens"))
        }

        return apiCall(Dispatchers.IO) {
            api.refresh(
                expiredToken = "Bearer $accessToken",
                request = RefreshRequest(refreshToken),
            )
                .toResult()
                .also { result ->
                    if (result is Result.Success) {
                        sessionManager.saveTokens(result.data)
                    }
                }
                .toCompletableResult()
        }
    }
}
