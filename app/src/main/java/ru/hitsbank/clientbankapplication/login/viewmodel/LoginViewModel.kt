package ru.hitsbank.clientbankapplication.login.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.hitsbank.bank_common.Constants
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.entity.RoleType
import ru.hitsbank.bank_common.domain.interactor.AuthInteractor
import ru.hitsbank.bank_common.domain.interactor.PushNotificationInteractor
import ru.hitsbank.bank_common.domain.model.RegisterFcmRequest
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.common.updateIfSuccess
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.replace
import ru.hitsbank.clientbankapplication.core.navigation.RootDestinations
import ru.hitsbank.clientbankapplication.login.event.LoginEffect
import ru.hitsbank.clientbankapplication.login.event.LoginEvent
import ru.hitsbank.clientbankapplication.login.model.LoginScreenModel

@HiltViewModel(assistedFactory = LoginViewModel.Factory::class)
class LoginViewModel @AssistedInject constructor(
    @Assisted private val authCode: String?,
    private val authInteractor: AuthInteractor,
    private val notificationInteractor: PushNotificationInteractor,
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
            authInteractor.exchangeAuthCodeForToken(authCode, RoleType.CLIENT).collectLatest { state ->
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
                        registerFcmToken()
                        navigationManager.replace(RootDestinations.BottomBarRoot.destination)
                    }
                }
            }
        }
    }

    private suspend fun registerFcmToken() {
        val request = RegisterFcmRequest(fcmToken = Firebase.messaging.token.await())
        notificationInteractor.registerFcmToken(request).collectLatest { state ->
            when (state) {
                State.Loading -> Unit
                is State.Error -> Log.e(TAG, "error registering token")
                is State.Success -> Log.d(TAG, "successfully registered token")
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
                uri = Uri.parse("${Constants.KEYCLOAK_BASE_URL}${Constants.AUTH_PAGE_PATH}")
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

    @AssistedFactory
    interface Factory {
        fun create(
            authCode: String?,
        ): LoginViewModel
    }

    private companion object {
        const val TAG = "LoginViewModel"
    }
}