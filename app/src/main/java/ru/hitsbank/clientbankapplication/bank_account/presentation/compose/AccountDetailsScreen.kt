package ru.hitsbank.clientbankapplication.bank_account.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountDetailsEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsScreenModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountDetailsViewModel
import ru.hitsbank.clientbankapplication.core.presentation.common.BankButton
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.ErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.LoadingContent
import ru.hitsbank.clientbankapplication.core.presentation.common.rememberCallback
import ru.hitsbank.clientbankapplication.core.presentation.common.verticalSpacer
import ru.hitsbank.clientbankapplication.core.presentation.theme.S14_W400
import ru.hitsbank.clientbankapplication.core.presentation.theme.S16_W400
import ru.hitsbank.clientbankapplication.core.presentation.theme.S22_W400
import ru.hitsbank.clientbankapplication.core.presentation.theme.S24_W600

@Composable
internal fun AccountDetailsScreenWrapper(
    viewModel: AccountDetailsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)

    AccountDetailsScreen(
        uiState = uiState,
        onEvent = onEvent,
    )
}

@Composable
private fun AccountDetailsScreen(
    uiState: BankUiState<AccountDetailsScreenModel>,
    onEvent: (AccountDetailsEvent) -> Unit,
) {
    when (uiState) {
        BankUiState.Loading -> {
            LoadingContent()
        }

        is BankUiState.Error -> {
            ErrorContent(
                onBack = {
                    onEvent.invoke(AccountDetailsEvent.OnBack)
                }
            )
        }

        is BankUiState.Ready -> {
            AccountDetailsScreenReady(
                model = uiState.model,
                onEvent = onEvent,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDetailsScreenReady(
    model: AccountDetailsScreenModel,
    onEvent: (AccountDetailsEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Счет",
                        style = S22_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable(
                            onClick = {
                                onEvent.invoke(AccountDetailsEvent.OnBack)
                            }
                        ),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back_48),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        },
    ) { paddings ->
        Column(
            modifier = Modifier
                .padding(paddings)
                .padding(horizontal = 16.dp),
        ) {
            16.dp.verticalSpacer()
            Text(
                text = "Информация о счете",
                style = S24_W600,
                color = MaterialTheme.colorScheme.onSurface,
            )
            16.dp.verticalSpacer()
            model.accountDetails.items.forEach { item ->
                8.dp.verticalSpacer()
                Text(
                    text = item.title,
                    style = S16_W400,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = item.subtitle,
                    style = S14_W400,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                8.dp.verticalSpacer()
            }
            12.dp.verticalSpacer()
            Row(
                modifier = Modifier.height(40.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                BankButton.Outlined(
                    modifier = Modifier.weight(1f),
                    text = "Пополнить",
                    onClick = { /* TODO */ },
                    icon = ImageVector.vectorResource(id = R.drawable.ic_top_up_18),
                    enabled = true,
                )
                BankButton.Regular(
                    modifier = Modifier.weight(1f),
                    text = "Вывести",
                    onClick = { /* TODO */ },
                    icon = ImageVector.vectorResource(id = R.drawable.ic_withdraw_18),
                    enabled = true,
                )
            }
            32.dp.verticalSpacer()
            Text(
                text = "История операций",
                style = S24_W600,
                color = MaterialTheme.colorScheme.onSurface,
            )
            LazyColumn {
                item {
                    16.dp.verticalSpacer()
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = "Пока тут ничего нет",
                        style = S16_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}