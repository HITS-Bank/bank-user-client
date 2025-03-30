package ru.hitsbank.clientbankapplication.core.data.datasource

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import ru.hitsbank.clientbankapplication.core.data.model.TokenResponse
import ru.hitsbank.clientbankapplication.core.data.model.TokenType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

class SessionManager(context: Context) {

    private companion object {
        const val TOKEN_PREFERENCES_KEY = "token_preferences"
        const val ACCESS_TOKEN = "access_token"
        const val ACCESS_TOKEN_ISSUED_AT = "access_token_issued_at"
        const val ACCESS_TOKEN_EXPIRES_IN = "access_token_expires_in"
        const val REFRESH_TOKEN = "refresh_token"
        const val REFRESH_TOKEN_ISSUED_AT = "refresh_token_issued_at"
        const val REFRESH_TOKEN_EXPIRES_IN = "refresh_token_expires_in"
        const val IS_USER_BLOCKED = "is_user_blocked"
    }

    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        TOKEN_PREFERENCES_KEY,
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun fetchToken(tokenType: TokenType): String? {
        return when (tokenType) {
            TokenType.ACCESS -> sharedPreferences.getString(ACCESS_TOKEN, null)
            TokenType.REFRESH -> sharedPreferences.getString(REFRESH_TOKEN, null)
        }
    }

    fun saveTokens(tokenResponse: TokenResponse) {
        val currentTimeMillis = System.currentTimeMillis()
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN, tokenResponse.accessToken)
            .putLong(ACCESS_TOKEN_ISSUED_AT, currentTimeMillis)
            .putLong(ACCESS_TOKEN_EXPIRES_IN, tokenResponse.accessTokenExpiresIn)
            .putString(REFRESH_TOKEN, tokenResponse.refreshToken)
            .putLong(ACCESS_TOKEN_ISSUED_AT, currentTimeMillis)
            .putLong(REFRESH_TOKEN_EXPIRES_IN, tokenResponse.refreshTokenExpiresIn)
            .apply()
    }

    fun isTokenExpired(tokenType: TokenType): Boolean {
        return when (tokenType) {
            TokenType.ACCESS -> {
                val issuedAt = sharedPreferences.getLong(ACCESS_TOKEN_ISSUED_AT, 0)
                val expiresIn = sharedPreferences.getLong(ACCESS_TOKEN_EXPIRES_IN, 0)
                if (issuedAt == 0L) return true

                val currentTimeMillis = System.currentTimeMillis()
                val expirationTimeMillis = issuedAt + expiresIn * 1000
                currentTimeMillis >= expirationTimeMillis
            }

            TokenType.REFRESH -> {
                val issuedAt = sharedPreferences.getLong(REFRESH_TOKEN_ISSUED_AT, 0)
                val expiresIn = sharedPreferences.getLong(REFRESH_TOKEN_EXPIRES_IN, 0)
                if (issuedAt == 0L) return true

                val currentTimeMillis = System.currentTimeMillis()
                val expirationTimeMillis = issuedAt + expiresIn * 1000
                currentTimeMillis >= expirationTimeMillis
            }
        }
    }

    fun saveIsUserBlocked(isBlocked: Boolean) {
        sharedPreferences.edit().putBoolean(IS_USER_BLOCKED, isBlocked).apply()
    }

    fun isUserBlocked(): Boolean = sharedPreferences.getBoolean(IS_USER_BLOCKED, false)

    fun hasToken(): Boolean = sharedPreferences.getString(ACCESS_TOKEN, null) != null

    private fun String?.parseUtcDateTime(): LocalDateTime? {
        return try {
            Instant.parse(this).atZone(ZoneOffset.UTC).toLocalDateTime()
        } catch (e: DateTimeParseException) {
            null
        }
    }
}