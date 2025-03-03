package ru.hitsbank.clientbankapplication.login.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.hitsbank.clientbankapplication.LocalSnackbarController
import ru.hitsbank.clientbankapplication.core.constants.Constants.GENERAL_ERROR_TEXT
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.LoadingContentOverlay
import ru.hitsbank.clientbankapplication.core.presentation.common.observeWithLifecycle
import ru.hitsbank.clientbankapplication.core.presentation.common.rememberCallback
import ru.hitsbank.clientbankapplication.core.presentation.common.verticalSpacer
import ru.hitsbank.clientbankapplication.login.event.LoginEffect
import ru.hitsbank.clientbankapplication.login.event.LoginEvent
import ru.hitsbank.clientbankapplication.login.model.LoginScreenModel
import ru.hitsbank.clientbankapplication.login.viewmodel.LoginViewModel

@Composable
internal fun LoginScreenWrapper(
    viewModel: LoginViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val snackbar = LocalSnackbarController.current

    viewModel.effects.observeWithLifecycle { effect ->
        when (effect) {
            LoginEffect.OnError -> snackbar.show(GENERAL_ERROR_TEXT)
        }
    }

    LoginScreen(
        uiState = uiState,
        onEvent = onEvent,
    )
}

@Composable
internal fun LoginScreen(
    uiState: BankUiState<LoginScreenModel>,
    onEvent: (LoginEvent) -> Unit,
) {
    when (uiState) {
        is BankUiState.Ready -> LoginScreenReady(
            model = uiState.model,
            onEvent = onEvent,
        )
        else -> Unit
    }
}

@Composable
internal fun LoginScreenReady(
    model: LoginScreenModel,
    onEvent: (LoginEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Банк",
            fontSize = 45.sp,
        )
        36.dp.verticalSpacer()
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.email,
            onValueChange = { onEvent.invoke(LoginEvent.OnEmailChanged(it)) },
            label = {
                Text(text = "Почта")
            },
            singleLine = true,
        )
        24.dp.verticalSpacer()
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.password,
            onValueChange = { onEvent.invoke(LoginEvent.OnPasswordChanged(it)) },
            label = {
                Text(text = "Пароль")
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
        )
        36.dp.verticalSpacer()
        Button(
            modifier = Modifier.size(width = 256.dp, height = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = { onEvent.invoke(LoginEvent.LogIn) },
        ) {
            Text(
                text = "Войти",
                textAlign = TextAlign.Center,
            )
        }
    }

    if (model.isLoading) {
        LoadingContentOverlay()
    }
}
