package ru.hitsbank.clientbankapplication.bank_account.presentation.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountDetailsEvent

@Composable
internal fun CloseAccountDialog(
    onEvent: (AccountDetailsEvent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onEvent(AccountDetailsEvent.OnDismissCloseAccountDialog) },
        confirmButton = {
            Button(onClick = { onEvent(AccountDetailsEvent.OnCloseAccount) }) {
                Text(text = "ОК")
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(AccountDetailsEvent.OnDismissCloseAccountDialog) }) {
                Text(text = "Отмена")
            }
        },
        title = {
            Text(text = "Закрыть счет")
        },
        text = {
            Text(text = "Вы уверены, что хотите закрыть счет?")
        }
    )
}