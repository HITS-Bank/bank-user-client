package ru.hitsbank.clientbankapplication.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.hitsbank.clientbankapplication.core.constants.Constants.CLIENT_APP_CHANNEL
import ru.hitsbank.clientbankapplication.core.domain.interactor.AuthInteractor
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.getIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.common.updateIfSuccess
import ru.hitsbank.clientbankapplication.login.event.LoginEvent
import ru.hitsbank.clientbankapplication.login.mapper.LoginScreenModelMapper
import ru.hitsbank.clientbankapplication.login.model.LoginScreenModel

class LoginViewModel(
    private val authInteractor: AuthInteractor,
    private val mapper: LoginScreenModelMapper,
) : ViewModel() {

    private val _state: MutableStateFlow<BankUiState<LoginScreenModel>> =
        MutableStateFlow(BankUiState.Ready(LoginScreenModel.EMPTY))
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> onEmailChanged(event.email)
            is LoginEvent.OnPasswordChanged -> onPasswordChanged(event.password)
            LoginEvent.LogIn -> logIn()
        }
    }

    private fun onEmailChanged(email: String) {
        _state.updateIfSuccess { it.copy(email = email) }
    }

    private fun onPasswordChanged(password: String) {
        _state.updateIfSuccess { it.copy(password = password) }
    }

    private fun logIn() {
        val request = _state.getIfSuccess() ?: return
        viewModelScope.launch {
            authInteractor.login(
                channel = CLIENT_APP_CHANNEL,
                request = mapper.map(request),
            )
        }
    }
}