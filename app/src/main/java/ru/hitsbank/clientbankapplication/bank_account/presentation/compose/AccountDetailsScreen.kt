package ru.hitsbank.clientbankapplication.bank_account.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.hitsbank.clientbankapplication.LocalSnackbarController
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountDetailsEffect
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountDetailsEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsScreenModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountDetailsViewModel
import ru.hitsbank.clientbankapplication.core.presentation.common.BankButton
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.Divider
import ru.hitsbank.clientbankapplication.core.presentation.common.ErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.ListItem
import ru.hitsbank.clientbankapplication.core.presentation.common.ListItemEnd
import ru.hitsbank.clientbankapplication.core.presentation.common.ListItemIcon
import ru.hitsbank.clientbankapplication.core.presentation.common.LoadingContent
import ru.hitsbank.clientbankapplication.core.presentation.common.LoadingContentOverlay
import ru.hitsbank.clientbankapplication.core.presentation.common.PaginationErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.PaginationLoadingContent
import ru.hitsbank.clientbankapplication.core.presentation.common.observeWithLifecycle
import ru.hitsbank.clientbankapplication.core.presentation.common.rememberCallback
import ru.hitsbank.clientbankapplication.core.presentation.common.verticalSpacer
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationEvent
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.rememberPaginationListState
import ru.hitsbank.clientbankapplication.core.presentation.theme.S14_W400
import ru.hitsbank.clientbankapplication.core.presentation.theme.S16_W400
import ru.hitsbank.clientbankapplication.core.presentation.theme.S22_W400
import ru.hitsbank.clientbankapplication.core.presentation.theme.S24_W600

@Composable
internal fun AccountDetailsScreenWrapper(
    viewModel: AccountDetailsViewModel,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val readyListState = rememberPaginationListState(viewModel)
    val snackbar = LocalSnackbarController.current

    viewModel.effects.observeWithLifecycle { effect ->
        when (effect) {
            AccountDetailsEffect.OnTopUpError -> snackbar.show("Не получилось пополнить счет")
            AccountDetailsEffect.OnTopUpSuccess -> snackbar.show("Счет пополнен")
            AccountDetailsEffect.OnWithdrawError -> snackbar.show("Не получилось вывести средства")
            AccountDetailsEffect.OnWithdrawSuccess -> snackbar.show("Средства выведены")
            AccountDetailsEffect.OnCloseAccountError -> snackbar.show("Не получилось закрыть счет")
            AccountDetailsEffect.OnCloseAccountSuccess -> snackbar.show("Счет закрыт")
        }
    }

    AccountDetailsScreen(
        uiState = uiState,
        onEvent = onEvent,
        listState = readyListState,
    )
}

@Composable
private fun AccountDetailsScreen(
    uiState: BankUiState<AccountDetailsScreenModel>,
    onEvent: (AccountDetailsEvent) -> Unit,
    listState: LazyListState,
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
                listState = listState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDetailsScreenReady(
    model: AccountDetailsScreenModel,
    onEvent: (AccountDetailsEvent) -> Unit,
    listState: LazyListState,
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
        floatingActionButton = {
            if (model.status != BankAccountStatusEntity.CLOSED) {
                BankButton.Outlined(
                    text = "Закрыть счет",
                    onClick = { onEvent.invoke(AccountDetailsEvent.OnOpenCloseAccountDialog) },
                    icon = ImageVector.vectorResource(id = R.drawable.ic_block_18),
                    enabled = !model.isUserBlocked,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.error,
                        disabledContentColor = MaterialTheme.colorScheme.outline,
                        disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    borderColor = if (!model.isUserBlocked) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                )
            }
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .padding(horizontal = 16.dp),
            state = listState,
        ) {
            item {
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
                if (model.status != BankAccountStatusEntity.CLOSED) {
                    Row(
                        modifier = Modifier.height(40.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        BankButton.Outlined(
                            modifier = Modifier.weight(1f),
                            text = "Пополнить",
                            onClick = { onEvent.invoke(AccountDetailsEvent.OnOpenTopUpDialog) },
                            icon = ImageVector.vectorResource(id = R.drawable.ic_top_up_18),
                            enabled = !model.isUserBlocked,
                            borderColor = if (!model.isUserBlocked) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outline
                            },
                        )
                        BankButton.Regular(
                            modifier = Modifier.weight(1f),
                            text = "Вывести",
                            onClick = { onEvent.invoke(AccountDetailsEvent.OnOpenWithdrawDialog) },
                            icon = ImageVector.vectorResource(id = R.drawable.ic_withdraw_18),
                            enabled = !model.isUserBlocked,
                        )
                    }
                }
                32.dp.verticalSpacer()
                Text(
                    text = "История операций",
                    style = S24_W600,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            item {
                16.dp.verticalSpacer()
            }

            if (
                model.data.isEmpty() &&
                (model.paginationState == PaginationState.Idle || model.paginationState == PaginationState.EndReached)
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = "Пока тут ничего нет",
                        style = S16_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            items(model.data, key = { it.id }) { item ->
                ListItem(
                    padding = PaddingValues(horizontal = 0.dp, vertical = 12.dp),
                    divider = Divider.None,
                    icon = ListItemIcon.SingleChar(
                        char = '₽',
                        backgroundColor = colorResource(id = item.leftPartBackgroundColorId),
                        charColor = colorResource(id = item.contentColorId),
                    ),
                    title = item.title,
                    subtitle = item.description,
                    end = ListItemEnd.Text(
                        text = item.amountText,
                        textColor = colorResource(id = item.contentColorId),
                    ),
                )
            }

            item {
                when (model.paginationState) {
                    PaginationState.Loading -> {
                        PaginationLoadingContent()
                    }

                    PaginationState.Error -> {
                        PaginationErrorContent {
                            onEvent(AccountDetailsEvent.OnPaginationEvent(PaginationEvent.LoadNextPage))
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    if (model.topUpDialog.isShown) {
        TopUpDialog(
            model = model.topUpDialog,
            onEvent = onEvent,
        )
    }

    if (model.withdrawDialog.isShown) {
        WithdrawDialog(
            model = model.withdrawDialog,
            onEvent = onEvent,
        )
    }

    if (model.closeAccountDialog.isShown) {
        CloseAccountDialog(onEvent)
    }

    if (model.isOverlayLoading) {
        LoadingContentOverlay()
    }
}