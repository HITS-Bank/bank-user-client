package ru.hitsbank.clientbankapplication.core.data.mapper

import ru.hitsbank.clientbankapplication.core.data.model.LoginRequest
import ru.hitsbank.clientbankapplication.core.data.model.ProfileResponse
import ru.hitsbank.clientbankapplication.core.data.model.TokenResponse
import ru.hitsbank.clientbankapplication.core.domain.model.LoginRequestEntity
import ru.hitsbank.clientbankapplication.core.domain.model.ProfileEntity
import ru.hitsbank.clientbankapplication.core.domain.model.TokenResponseEntity

class AuthMapper {

    fun map(entity: LoginRequestEntity): LoginRequest {
        return LoginRequest(
            email = entity.email,
            password = entity.password,
        )
    }

    fun map(response: TokenResponse): TokenResponseEntity {
        return TokenResponseEntity(
            accessToken = response.accessToken,
            accessTokenExpiresAt = response.accessTokenExpiresAt,
            refreshToken = response.refreshToken,
            refreshTokenExpiresAt = response.refreshTokenExpiresAt,
        )
    }
}