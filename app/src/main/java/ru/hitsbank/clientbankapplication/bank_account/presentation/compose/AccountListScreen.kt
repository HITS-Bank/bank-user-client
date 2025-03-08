package ru.hitsbank.clientbankapplication.bank_account.presentation.compose

import androidx.compose.foundation.clickable
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
import org.koin.androidx.compose.koinViewModel
import ru.hitsbank.clientbankapplication.LocalSnackbarController
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountListEffect
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountListEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountListPaginationState
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountListViewModel
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.ErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.ListItem
import ru.hitsbank.clientbankapplication.core.presentation.common.ListItemEnd
import ru.hitsbank.clientbankapplication.core.presentation.common.ListItemIcon
import ru.hitsbank.clientbankapplication.core.presentation.common.LoadingContent
import ru.hitsbank.clientbankapplication.core.presentation.common.PaginationErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.PaginationLoadingContent
import ru.hitsbank.clientbankapplication.core.presentation.common.observeWithLifecycle
import ru.hitsbank.clientbankapplication.core.presentation.common.rememberCallback
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationEvent
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.rememberPaginationListState
import ru.hitsbank.clientbankapplication.core.presentation.theme.S14_W400
import ru.hitsbank.clientbankapplication.core.presentation.theme.S22_W400

@Composable
internal fun AccountListScreenWrapper(
    viewModel: AccountListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val readyListState = rememberPaginationListState(viewModel)
    val snackbar = LocalSnackbarController.current

    viewModel.effects.observeWithLifecycle { effect ->
        when (effect) {
            AccountListEffect.OnCreateAccountError -> snackbar.show("Не получилось открыть счет")
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
        if (!model.isSelectionMode) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Счета",
                        style = S22_W400,
                    )
                },
            )
        } else {
            TopAppBar(
                title = {
                    Text(
                        text = "Выбор счета",
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
        if (!model.isUserBlocked && !model.isSelectionMode) {
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
    }
) { paddings ->
    LazyColumn(
        modifier = Modifier.padding(paddings),
        state = listState,
    ) {
        items(model.data) { item ->
            ListItem(
                icon = ListItemIcon.Vector(
                    iconResId = R.drawable.ic_account_balance_wallet_24,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                title = item.number,
                subtitle = item.description,
                subtitleTextStyle = S14_W400.copy(color = colorResource(id = item.descriptionColorId)),
                end = ListItemEnd.Chevron(
                    onClick = { onEvent.invoke(AccountListEvent.OnClickDetails(item.number)) },
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
                        onEvent(AccountListEvent.OnPaginationEvent(PaginationEvent.LoadNextPage))
                    }
                }
                else -> Unit
            }
        }
    }

    if (model.isCreateAccountDialogShown) {
        CreateAccountDialog(onEvent)
    }
}