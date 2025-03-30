package ru.hitsbank.clientbankapplication.login.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.hitsbank.clientbankapplication.core.constants.Constants
import ru.hitsbank.clientbankapplication.core.constants.Constants.CLIENT_APP_CHANNEL
import ru.hitsbank.clientbankapplication.core.domain.common.State
import ru.hitsbank.clientbankapplication.core.domain.interactor.AuthInteractor
import ru.hitsbank.clientbankapplication.core.navigation.RootDestinations
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager
import ru.hitsbank.clientbankapplication.core.navigation.base.replace
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.getIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.common.updateIfSuccess
import ru.hitsbank.clientbankapplication.loan.presentation.event.create.LoanCreateEffect
import ru.hitsbank.clientbankapplication.login.event.LoginEffect
import ru.hitsbank.clientbankapplication.login.event.LoginEvent
import ru.hitsbank.clientbankapplication.login.mapper.LoginScreenModelMapper
import ru.hitsbank.clientbankapplication.login.model.LoginScreenModel

class LoginViewModel(
    private val authCode: String?,
    private val authInteractor: AuthInteractor,
    private val mapper: LoginScreenModelMapper,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    private val _state: MutableStateFlow<BankUiState<LoginScreenModel>> =
        MutableStateFlow(BankUiState.Ready(LoginScreenModel.EMPTY))
    val uiState = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        if (authCode != null) {
            exchangeAuthCodeForToken(authCode)
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> onEmailChanged(event.email)
            is LoginEvent.OnPasswordChanged -> onPasswordChanged(event.password)
            LoginEvent.LogIn -> logIn()
        }
    }

    private fun exchangeAuthCodeForToken(authCode: String) {
        _state.updateIfSuccess { it.copy(isLoading = true) }
        viewModelScope.launch {
            authInteractor.exchangeAuthCodeForToken(authCode).collectLatest { state ->
                when (state) {
                    is State.Error -> {
                        _state.updateIfSuccess { it.copy(isLoading = false) }
                        sendEffect(LoginEffect.OnError)
                    }

                    State.Loading -> {
                        _state.updateIfSuccess { it.copy(isLoading = true) }
                    }

                    is State.Success -> {
                        _state.updateIfSuccess { it.copy(isLoading = false) }
                        navigationManager.replace(RootDestinations.BottomBarRoot.destination)
                    }
                }
            }
        }
    }

    private fun onEmailChanged(email: String) {
        _state.updateIfSuccess { it.copy(email = email) }
    }

    private fun onPasswordChanged(password: String) {
        _state.updateIfSuccess { it.copy(password = password) }
    }

    private fun logIn() {
        _state.updateIfSuccess { it.copy(isLoading = true) }
        sendEffect(
            LoginEffect.OpenAuthPage(
                uri = Uri.parse("${Constants.AUTH_BASE_URL}/${Constants.AUTH_PAGE_PATH}")
                    .buildUpon()
                    .appendQueryParameter("client_id", "bank-rest-api")
                    .appendQueryParameter("response_type", "code")
                    .appendQueryParameter("redirect_uri", "hitsbankapp://authorized")
                    .build()
                    .toString()
            )
        )
    }

    private fun sendEffect(effect: LoginEffect) {
        _effects.trySend(effect)
    }
}