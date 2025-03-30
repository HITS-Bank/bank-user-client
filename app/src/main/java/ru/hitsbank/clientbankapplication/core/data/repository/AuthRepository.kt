package ru.hitsbank.clientbankapplication.core.data.repository

import kotlinx.coroutines.Dispatchers
import ru.hitsbank.clientbankapplication.core.constants.Constants.AUTH_CLIENT_ID
import ru.hitsbank.clientbankapplication.core.constants.Constants.AUTH_REDIRECT_URI
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
import ru.hitsbank.clientbankapplication.core.domain.repository.IAuthRepository

private const val AUTH_CODE_GRANT_TYPE = "authorization_code"
private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"

class AuthRepository(
    private val authApi: AuthApi,
    private val mapper: AuthMapper,
    private val sessionManager: SessionManager,
) : IAuthRepository {

    override suspend fun exchangeAuthCodeForToken(code: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            authApi.exchangeAuthCodeForToken(
                clientId = AUTH_CLIENT_ID,
                grantType = AUTH_CODE_GRANT_TYPE,
                code = code,
                redirectUri = AUTH_REDIRECT_URI,
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

    override suspend fun refresh(): Result<Completable> {
        val refreshToken = sessionManager.fetchToken(TokenType.REFRESH)
            ?: return Result.Error(Exception("could not retrieve refresh token"))

        return apiCall(Dispatchers.IO) {
            authApi.refreshToken(
                clientId = AUTH_CLIENT_ID,
                grantType = REFRESH_TOKEN_GRANT_TYPE,
                refreshToken = refreshToken,
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

    override fun saveIsUserBlocked(isUserBlocked: Boolean) {
        sessionManager.saveIsUserBlocked(isUserBlocked)
    }

    override fun getIsUserBlocked(): Result<Boolean> {
        return Result.Success(sessionManager.isUserBlocked())
    }
}
