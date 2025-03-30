package ru.hitsbank.clientbankapplication.login.mapper

import ru.hitsbank.clientbankapplication.core.domain.model.LoginRequestEntity
import ru.hitsbank.clientbankapplication.login.model.LoginScreenModel
import javax.inject.Inject

class LoginScreenModelMapper @Inject constructor() {

    fun map(state: LoginScreenModel): LoginRequestEntity {
        return LoginRequestEntity(
            email = state.email,
            password = state.password,
        )
    }
}