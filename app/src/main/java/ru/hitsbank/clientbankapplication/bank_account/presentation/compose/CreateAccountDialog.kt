package ru.hitsbank.clientbankapplication.bank_account.presentation.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountListEvent

@Composable
internal fun CreateAccountDialog(
    onEvent: (AccountListEvent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onEvent(AccountListEvent.OnDismissCreateAccountDialog) },
        confirmButton = {
            Button(onClick = { onEvent(AccountListEvent.OnCreateAccount) }) {
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
            Text(text = "Вы уверены, что хотите открыть новый счет?")
        }
    )
}