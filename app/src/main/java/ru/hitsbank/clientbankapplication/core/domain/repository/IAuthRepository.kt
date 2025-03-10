package ru.hitsbank.clientbankapplication.core.domain.repository

import ru.hitsbank.clientbankapplication.core.domain.common.Completable
import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.model.LoginRequestEntity

interface IAuthRepository {

    suspend fun login(
        channel: String,
        request: LoginRequestEntity,
    ): Result<Completable>

    suspend fun refresh(): Result<Completable>

    fun saveIsUserBlocked(isUserBlocked: Boolean)

    fun getIsUserBlocked(): Result<Boolean>
}