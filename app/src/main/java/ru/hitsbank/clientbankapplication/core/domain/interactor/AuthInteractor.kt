package ru.hitsbank.clientbankapplication.core.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.hitsbank.clientbankapplication.core.data.common.toState
import ru.hitsbank.clientbankapplication.core.domain.common.Completable
import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.common.State
import ru.hitsbank.clientbankapplication.core.domain.model.LoginRequestEntity
import ru.hitsbank.clientbankapplication.core.domain.model.TokenResponseEntity
import ru.hitsbank.clientbankapplication.core.domain.repository.IAuthRepository

class AuthInteractor(
    private val authRepository: IAuthRepository,
) {

    fun login(
        channel: String,
        request: LoginRequestEntity,
    ): Flow<State<Completable>> = flow {
        emit(State.Loading)

        when (val userProfile = authRepository.getSelfProfile()) {
            is Result.Error -> emit(userProfile.toState())
            is Result.Success -> authRepository.saveIsUserBlocked(userProfile.data.isBanned)
        }

        emit(authRepository.login(channel, request).toState())
    }

    fun getIsUserBlocked(): Flow<State<Boolean>> = flow {
        emit(State.Loading)
        emit(authRepository.getIsUserBlocked().toState())
    }
}
