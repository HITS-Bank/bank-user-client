package ru.hitsbank.clientbankapplication.login.mapper

import ru.hitsbank.clientbankapplication.core.domain.model.LoginRequestEntity
import ru.hitsbank.clientbankapplication.login.model.LoginScreenModel

class LoginScreenModelMapper {

    fun map(state: LoginScreenModel): LoginRequestEntity {
        return LoginRequestEntity(
            email = state.email,
            password = state.password,
        )
    }
}