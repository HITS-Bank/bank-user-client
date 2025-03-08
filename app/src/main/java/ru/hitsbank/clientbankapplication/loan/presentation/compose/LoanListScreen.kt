package ru.hitsbank.clientbankapplication.loan.presentation.compose

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.ErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.ListItem
import ru.hitsbank.clientbankapplication.core.presentation.common.ListItemEnd
import ru.hitsbank.clientbankapplication.core.presentation.common.ListItemIcon
import ru.hitsbank.clientbankapplication.core.presentation.common.LoadingContent
import ru.hitsbank.clientbankapplication.core.presentation.common.PaginationErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.PaginationLoadingContent
import ru.hitsbank.clientbankapplication.core.presentation.common.rememberCallback
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationEvent
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.rememberPaginationListState
import ru.hitsbank.clientbankapplication.core.presentation.theme.S22_W400
import ru.hitsbank.clientbankapplication.loan.presentation.event.LoanListEvent
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanListPaginationState
import ru.hitsbank.clientbankapplication.loan.presentation.viewmodel.LoanListViewModel

@Composable
fun LoanListScreen(viewModel: LoanListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val onPaginationEvent = rememberCallback(viewModel::onPaginationEvent)
    val listState = rememberPaginationListState(viewModel)

    when (val currentState = state) {
        BankUiState.Loading -> {
            LoadingContent()
        }

        is BankUiState.Error -> {
            ErrorContent {}
        }

        is BankUiState.Ready -> {
            LoanListReadyContent(
                onEvent = onEvent,
                onPaginationEvent = onPaginationEvent,
                listState = listState,
                model = currentState.model,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanListReadyContent(
    onEvent: (LoanListEvent) -> Unit,
    onPaginationEvent: (PaginationEvent) -> Unit,
    listState: LazyListState,
    model: LoanListPaginationState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(
                        text = "Кредиты",
                        style = S22_W400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
        },
        floatingActionButton = {
            if (!model.isUserBlocked) {
                FloatingActionButton(
                    onClick = { onEvent.invoke(LoanListEvent.CreateLoan) },
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { paddings ->
        LazyColumn(
            modifier = Modifier.padding(paddings),
            state = listState,
        ) {
            items(model.data) { item ->
                ListItem(
                    icon = ListItemIcon.Vector(iconResId = R.drawable.ic_credit_24),
                    title = item.number,
                    subtitle = item.description,
                    end = ListItemEnd.Chevron(
                        onClick = { onEvent.invoke(LoanListEvent.OpenLoanDetails(item.number)) },
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
                            onPaginationEvent(PaginationEvent.LoadNextPage)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}