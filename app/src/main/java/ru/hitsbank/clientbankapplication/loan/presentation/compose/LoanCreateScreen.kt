package ru.hitsbank.clientbankapplication.loan.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.common.component.Divider
import ru.hitsbank.bank_common.presentation.common.component.ListItem
import ru.hitsbank.bank_common.presentation.common.component.ListItemIcon
import ru.hitsbank.bank_common.presentation.common.component.PaginationLoadingContent
import ru.hitsbank.clientbankapplication.LocalSnackbarController
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.core.presentation.common.BankButton
import ru.hitsbank.clientbankapplication.core.presentation.common.LoadingContentOverlay
import ru.hitsbank.clientbankapplication.core.presentation.common.PaginationErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.defaultTextFieldColors
import ru.hitsbank.clientbankapplication.core.presentation.common.noRippleClickable
import ru.hitsbank.clientbankapplication.core.presentation.common.observeWithLifecycle
import ru.hitsbank.clientbankapplication.core.presentation.common.rememberCallback
import ru.hitsbank.clientbankapplication.core.presentation.common.verticalSpacer
import ru.hitsbank.clientbankapplication.core.presentation.theme.S16_W400
import ru.hitsbank.clientbankapplication.core.presentation.theme.S22_W400
import ru.hitsbank.clientbankapplication.loan.presentation.event.create.LoanCreateEffect
import ru.hitsbank.clientbankapplication.loan.presentation.event.create.LoanCreateEvent
import ru.hitsbank.clientbankapplication.loan.presentation.viewmodel.LoanCreateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanCreateScreen(viewModel: LoanCreateViewModel) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val snackbar = LocalSnackbarController.current

    viewModel.effects.observeWithLifecycle { effect ->
        when (effect) {
            LoanCreateEffect.ShowLoanCreationError -> snackbar.show("Не удалось оформить кредит")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Оформление кредита",
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
                                onEvent.invoke(LoanCreateEvent.Back)
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
                .fillMaxSize()
                .padding(paddings)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            when (val loanRating = uiState.loanRatingState) {
                is BankUiState.Error -> PaginationErrorContent { onEvent(LoanCreateEvent.ReloadLoanRating) }
                BankUiState.Loading -> PaginationLoadingContent()
                is BankUiState.Ready -> ListItem(
                    icon = ListItemIcon.None,
                    title = loanRating.model.toString(),
                    subtitle = "Кредитный рейтинг",
                    divider = Divider.None,
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = uiState.amount,
                onValueChange = { onEvent(LoanCreateEvent.ChangeAmount(it)) },
                placeholder = {
                    Text(
                        text = "Сумма кредита",
                        style = S16_W400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                singleLine = true,
                colors = defaultTextFieldColors,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            16.dp.verticalSpacer()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = uiState.termInMonths,
                onValueChange = { onEvent(LoanCreateEvent.ChangeTerm(it)) },
                placeholder = {
                    Text(
                        text = "Срок кредита (в месяцах)",
                        style = S16_W400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                singleLine = true,
                colors = defaultTextFieldColors,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            16.dp.verticalSpacer()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        onEvent(LoanCreateEvent.SelectTariff)
                    },
                value = uiState.tariffName ?: "",
                onValueChange = { },
                placeholder = {
                    Text(
                        text = "Тариф",
                        style = S16_W400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                singleLine = true,
                colors = defaultTextFieldColors,
                enabled = false,
            )
            16.dp.verticalSpacer()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        onEvent(LoanCreateEvent.SelectAccount)
                    },
                value = uiState.accountNumber ?: "",
                onValueChange = { },
                placeholder = {
                    Text(
                        text = "Счет для зачисления и погашения",
                        style = S16_W400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                singleLine = true,
                colors = defaultTextFieldColors,
                enabled = false,
            )
            16.dp.verticalSpacer()
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                BankButton.Outlined(
                    text = "Оформить",
                    onClick = { onEvent(LoanCreateEvent.ConfirmCreate) },
                    icon = ImageVector.vectorResource(id = R.drawable.ic_credit_24),
                    enabled = uiState.canCreateLoan,
                    borderColor = if (uiState.canCreateLoan) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                )
            }
        }
    }

    if (uiState.isPerformingAction) {
        LoadingContentOverlay()
    }
}