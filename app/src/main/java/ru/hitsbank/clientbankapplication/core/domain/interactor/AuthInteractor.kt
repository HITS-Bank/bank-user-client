package ru.hitsbank.clientbankapplication.core.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.hitsbank.clientbankapplication.core.data.common.toState
import ru.hitsbank.clientbankapplication.core.domain.common.Completable
import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.common.State
import ru.hitsbank.clientbankapplication.core.domain.model.LoginRequestEntity
import ru.hitsbank.clientbankapplication.core.domain.repository.IAuthRepository
import ru.hitsbank.clientbankapplication.core.domain.repository.IProfileRepository

class AuthInteractor(
    private val authRepository: IAuthRepository,
    private val profileRepository: IProfileRepository,
) {

//    fun login(
//        channel: String,
//        request: LoginRequestEntity,
//    ): Flow<State<Completable>> = flow {
//        emit(State.Loading)
//        emit(authRepository.login(channel, request).toState())
//
//        when (val userProfile = profileRepository.getSelfProfile()) {
//            is Result.Error -> emit(userProfile.toState())
//            is Result.Success -> authRepository.saveIsUserBlocked(userProfile.data.isBanned)
//        }
//    }

    fun exchangeAuthCodeForToken(code: String): Flow<State<Completable>> = flow {
        // TODO
        emit(State.Loading)
        emit(authRepository.exchangeAuthCodeForToken(code).toState())
        // TODO check if is banned
    }

    fun getIsUserBlocked(): Flow<State<Boolean>> = flow {
        emit(State.Loading)
        emit(authRepository.getIsUserBlocked().toState())
    }
}
