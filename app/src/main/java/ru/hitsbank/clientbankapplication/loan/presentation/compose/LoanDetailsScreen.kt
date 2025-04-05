package ru.hitsbank.clientbankapplication.loan.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.common.LocalSnackbarController
import ru.hitsbank.bank_common.presentation.common.component.BankButton
import ru.hitsbank.bank_common.presentation.common.component.Divider
import ru.hitsbank.bank_common.presentation.common.component.ErrorContent
import ru.hitsbank.bank_common.presentation.common.component.ListItem
import ru.hitsbank.bank_common.presentation.common.component.ListItemEnd
import ru.hitsbank.bank_common.presentation.common.component.ListItemIcon
import ru.hitsbank.bank_common.presentation.common.component.LoadingContent
import ru.hitsbank.bank_common.presentation.common.component.LoadingContentOverlay
import ru.hitsbank.bank_common.presentation.common.observeWithLifecycle
import ru.hitsbank.bank_common.presentation.common.rememberCallback
import ru.hitsbank.bank_common.presentation.theme.S22_W400
import ru.hitsbank.bank_common.presentation.theme.S24_W600
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.loan.presentation.event.LoanDetailsEffect
import ru.hitsbank.clientbankapplication.loan.presentation.event.LoanDetailsEvent
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanDetailsDialogState
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanDetailsListItem
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanDetailsState
import ru.hitsbank.clientbankapplication.loan.presentation.viewmodel.LoanDetailsViewModel

@Composable
fun LoanDetailsScreen(viewModel: LoanDetailsViewModel) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val snackbar = LocalSnackbarController.current

    viewModel.effects.observeWithLifecycle { effect ->
        when (effect) {
            LoanDetailsEffect.ShowLoanPaymentError -> snackbar.show("Не удалось провести платеж")
        }
    }

    when (val state = uiState) {
        BankUiState.Loading -> {
            LoadingContent()
        }

        is BankUiState.Error -> {
            ErrorContent(
                onBack = {
                    onEvent.invoke(LoanDetailsEvent.Back)
                }
            )
        }

        is BankUiState.Ready -> {
            LoanDetailsScreenContent(state.model, onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoanDetailsScreenContent(
    model: LoanDetailsState,
    onEvent: (LoanDetailsEvent) -> Unit,
) {
    when (val dialogState = model.dialogState) {
        is LoanDetailsDialogState.MakePaymentDialog -> LoanPaymentDialog(dialogState, onEvent)
        LoanDetailsDialogState.None -> Unit
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Кредит",
                        style = S22_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable(
                            onClick = {
                                onEvent.invoke(LoanDetailsEvent.Back)
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
        LazyColumn(
            modifier = Modifier.padding(paddings)
        ) {
            items(model.detailItems) { item ->
                when (item) {
                    LoanDetailsListItem.LoanInfoHeader -> Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Информация о кредите",
                        style = S24_W600,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    is LoanDetailsListItem.LoanDetailsProperty -> ListItem(
                        icon = ListItemIcon.None,
                        divider = Divider.None,
                        padding = PaddingValues(top = 12.dp, bottom = 12.dp, end = 16.dp),
                        title = item.value,
                        subtitle = item.name,
                    )

                    is LoanDetailsListItem.LoanBankAccount -> ListItem(
                        icon = ListItemIcon.None,
                        divider = Divider.None,
                        padding = PaddingValues(top = 12.dp, bottom = 12.dp, end = 16.dp),
                        title = item.value,
                        subtitle = item.name,
                        end = ListItemEnd.ClickableChevron(
                            onClick = { onEvent(LoanDetailsEvent.OpenBankAccount(item.accountId)) },
                        ),
                    )

                    LoanDetailsListItem.MakePaymentButton -> Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        BankButton.Outlined(
                            modifier = Modifier.padding(16.dp),
                            text = "Внести платёж",
                            onClick = { onEvent(LoanDetailsEvent.MakeLoanPaymentDialogOpen) },
                            icon = ImageVector.vectorResource(id = R.drawable.ic_top_up_18),
                            enabled = !model.isUserBlocked,
                            borderColor = if (!model.isUserBlocked) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outline
                            },
                        )
                    }
                }
            }
        }
    }

    if (model.isPerformingAction) {
        LoadingContentOverlay()
    }
}