package ru.hitsbank.clientbankapplication.bank_account.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.common.LocalSnackbarController
import ru.hitsbank.bank_common.presentation.common.component.Divider
import ru.hitsbank.bank_common.presentation.common.component.ErrorContent
import ru.hitsbank.bank_common.presentation.common.component.ListItem
import ru.hitsbank.bank_common.presentation.common.component.ListItemEnd
import ru.hitsbank.bank_common.presentation.common.component.ListItemIcon
import ru.hitsbank.bank_common.presentation.common.component.LoadingContent
import ru.hitsbank.bank_common.presentation.common.component.LoadingContentOverlay
import ru.hitsbank.bank_common.presentation.common.component.PaginationErrorContent
import ru.hitsbank.bank_common.presentation.common.component.PaginationLoadingContent
import ru.hitsbank.bank_common.presentation.common.component.SwipeableInfo
import ru.hitsbank.bank_common.presentation.common.component.SwipeableListItem
import ru.hitsbank.bank_common.presentation.common.observeWithLifecycle
import ru.hitsbank.bank_common.presentation.common.rememberCallback
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent
import ru.hitsbank.bank_common.presentation.pagination.PaginationState
import ru.hitsbank.bank_common.presentation.pagination.rememberPaginationListState
import ru.hitsbank.bank_common.presentation.theme.S14_W400
import ru.hitsbank.bank_common.presentation.theme.S22_W400
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountListEffect
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountListEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountListMode
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountListPaginationState
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountListViewModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.CreateAccountDialogState

@Composable
internal fun AccountListScreenWrapper(
    viewModel: AccountListViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val readyListState = rememberPaginationListState(viewModel)
    val snackbar = LocalSnackbarController.current

    viewModel.effects.observeWithLifecycle { effect ->
        when (effect) {
            AccountListEffect.OnCreateAccountError -> snackbar.show("Не получилось открыть счет")
            AccountListEffect.OnFailedToLoadHiddenAccounts -> snackbar.show("Не удалось загрузить скрытые счета")
            AccountListEffect.OnHideAccountError -> snackbar.show("Не удалось скрыть счет")
            AccountListEffect.OnUnhideAccountError -> snackbar.show("Не удалось убрать счет из скрытых")
        }
    }

    AccountListScreen(
        state = state,
        onEvent = onEvent,
        listState = readyListState,
    )
}

@Composable
internal fun AccountListScreen(
    state: BankUiState<AccountListPaginationState>,
    onEvent: (AccountListEvent) -> Unit,
    listState: LazyListState,
) = when (state) {
    BankUiState.Loading -> LoadingContent()
    is BankUiState.Error -> ErrorContent {}
    is BankUiState.Ready -> AccountListScreenReady(
        model = state.model,
        onEvent = onEvent,
        listState = listState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountListScreenReady(
    model: AccountListPaginationState,
    onEvent: (AccountListEvent) -> Unit,
    listState: LazyListState,
) = Scaffold(
    topBar = {
        if (model.accountListMode != AccountListMode.SELECTION) {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0),
                title = {
                    Text(
                        text = model.accountListMode.topBarTitle,
                        style = S22_W400,
                    )
                },
            )
        } else {
            TopAppBar(
                title = {
                    Text(
                        text = model.accountListMode.topBarTitle,
                        style = S22_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable(
                            onClick = {
                                onEvent.invoke(AccountListEvent.Back)
                            }
                        ),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back_48),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        }
    },
    floatingActionButton = {
        if (!model.isUserBlocked && model.accountListMode == AccountListMode.DEFAULT) {
            FloatingActionButton(
                onClick = { onEvent.invoke(AccountListEvent.OnOpenCreateAccountDialog) },
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_add_48),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    },
    contentWindowInsets = WindowInsets(0),
) { paddings ->
    LazyColumn(
        modifier = Modifier.padding(paddings),
        state = listState,
    ) {
        items(model.data) { item ->
            if (model.accountListMode != AccountListMode.SELECTION) {
                SwipeableListItem(
                    icon = ListItemIcon.Vector(
                        iconResId = R.drawable.ic_account_balance_wallet_24,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    title = item.number,
                    subtitle = item.description,
                    subtitleTextStyle = S14_W400.copy(color = colorResource(id = item.descriptionColorId)),
                    end = ListItemEnd.ClickableChevron(
                        onClick = {
                            onEvent.invoke(AccountListEvent.OnClickDetails(item.id, item.number))
                        },
                    ),
                    swipeableInfo = SwipeableInfo(
                        iconResId = if (item.isHidden) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off,
                        onIconClick = {
                            if (item.isHidden) {
                                onEvent(AccountListEvent.UnhideAccount(item.id))
                            } else {
                                onEvent(AccountListEvent.HideAccount(item.id))
                            }
                        },
                    ),
                )
            } else {
                ListItem(
                    icon = ListItemIcon.Vector(
                        iconResId = R.drawable.ic_account_balance_wallet_24,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    title = item.number,
                    subtitle = item.description,
                    subtitleTextStyle = S14_W400.copy(color = colorResource(id = item.descriptionColorId)),
                    end = ListItemEnd.ClickableChevron(
                        onClick = {
                            onEvent.invoke(AccountListEvent.OnClickDetails(item.id, item.number))
                        },
                    ),
                )
            }
        }

        item {
            when (model.paginationState) {
                PaginationState.Loading -> {
                    PaginationLoadingContent()
                }

                PaginationState.Error -> {
                    PaginationErrorContent {
                        onEvent(AccountListEvent.OnPaginationEvent(PaginationEvent.LoadNextPage))
                    }
                }
                else -> {
                    if (model.accountListMode == AccountListMode.DEFAULT) {
                        ListItem(
                            icon = ListItemIcon.Vector(
                                iconResId = R.drawable.ic_visibility_off,
                                backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                iconColor = MaterialTheme.colorScheme.inverseOnSurface,
                            ),
                            title = "Скрытые счета",
                            subtitle = "Нажмите, чтобы посмотреть",
                            divider = Divider.None,
                            modifier = Modifier.clickable { onEvent(AccountListEvent.OpenHiddenAccounts) },
                        )
                    }
                }
            }
        }
    }

    when (val dialogState = model.createAccountDialogState) {
        is CreateAccountDialogState.Shown -> CreateAccountDialog(model.currencyCodeItems, dialogState, onEvent)
        CreateAccountDialogState.Hidden -> Unit
    }

    if (model.isPerformingAction) {
        LoadingContentOverlay()
    }
}