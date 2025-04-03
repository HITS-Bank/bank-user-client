package ru.hitsbank.clientbankapplication.bank_account.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountListEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.CreateAccountDialogState
import ru.hitsbank.bank_common.presentation.common.component.dropdown.DropdownField
import ru.hitsbank.bank_common.presentation.common.verticalSpacer
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.CurrencyCodeDropdownItem

@Composable
internal fun CreateAccountDialog(
    items: List<CurrencyCodeDropdownItem>,
    dialogState: CreateAccountDialogState.Shown,
    onEvent: (AccountListEvent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onEvent(AccountListEvent.OnDismissCreateAccountDialog) },
        confirmButton = {
            Button(onClick = { onEvent(AccountListEvent.OnCreateAccount(dialogState.currencyCode)) }) {
                Text(text = "ОК")
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(AccountListEvent.OnDismissCreateAccountDialog) }) {
                Text(text = "Отмена")
            }
        },
        title = {
            Text(text = "Открыть счет")
        },
        text = {
            Column {
                Text(text = "Вы уверены, что хотите открыть новый счет?")
                8.dp.verticalSpacer()
                DropdownField(
                    items = items,
                    selectedItem = dialogState.currencyCodeItem,
                    onItemSelected = { onEvent(AccountListEvent.OnSelectAccountCurrencyCode(it.currencyCode)) },
                    isDropdownOpen = dialogState.isDropdownExpanded,
                    onOpenDropdown = {
                        onEvent(AccountListEvent.OnSetAccountCreateDropdownExpanded(true))
                    },
                    onCloseDropdown = {
                        onEvent(AccountListEvent.OnSetAccountCreateDropdownExpanded(false))
                    },
                    label = "Валюта счета",
                )
            }
        }
    )
}