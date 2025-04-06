package ru.hitsbank.clientbankapplication.bank_account.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.hitsbank.bank_common.presentation.common.component.dropdown.DropdownField
import ru.hitsbank.bank_common.presentation.common.horizontalSpacer
import ru.hitsbank.bank_common.presentation.common.verticalSpacer
import ru.hitsbank.bank_common.presentation.theme.S16_W400
import ru.hitsbank.bank_common.presentation.theme.S16_W500
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountDetailsEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsWithdrawDialogModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.CurrencyCodeDropdownItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WithdrawDialog(
    items: List<CurrencyCodeDropdownItem>,
    model: AccountDetailsWithdrawDialogModel,
    onEvent: (AccountDetailsEvent) -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = { onEvent.invoke(AccountDetailsEvent.OnDismissWithdrawDialog) },
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "Вывести средства",
                    style = S16_W500,
                )
                16.dp.verticalSpacer()
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = model.amount,
                    onValueChange = { onEvent(AccountDetailsEvent.OnWithdrawAmountChange(it)) },
                    label = {
                        Text(text = "Сумма")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )
                16.dp.verticalSpacer()
                DropdownField(
                    items = items,
                    selectedItem = model.currencyCodeDropdownItem,
                    onItemSelected = { onEvent(AccountDetailsEvent.OnWithdrawCurrencyChange(it.currencyCode)) },
                    isDropdownOpen = model.isDropdownExpanded,
                    onOpenDropdown = {
                        onEvent(AccountDetailsEvent.OnWithdrawDropdownExpanded(true))
                    },
                    onCloseDropdown = {
                        onEvent(AccountDetailsEvent.OnWithdrawDropdownExpanded(false))
                    },
                    label = "Валюта вывода",
                )
                16.dp.verticalSpacer()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { onEvent(AccountDetailsEvent.OnDismissWithdrawDialog) },
                    ) {
                        Text(
                            text = "Отмена",
                            style = S16_W400,
                        )
                    }
                    8.dp.horizontalSpacer()
                    Button(
                        onClick = { onEvent(AccountDetailsEvent.Withdraw) },
                        enabled = model.isDataValid,
                    ) {
                        Text(
                            text = "ОК",
                            style = S16_W400,
                        )
                    }
                }
            }
        }
    }
}