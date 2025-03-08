package ru.hitsbank.clientbankapplication.loan.presentation.compose

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
import ru.hitsbank.clientbankapplication.core.presentation.common.horizontalSpacer
import ru.hitsbank.clientbankapplication.core.presentation.common.verticalSpacer
import ru.hitsbank.clientbankapplication.core.presentation.theme.S16_W400
import ru.hitsbank.clientbankapplication.core.presentation.theme.S16_W500
import ru.hitsbank.clientbankapplication.loan.presentation.event.LoanDetailsEvent
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanDetailsDialogState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoanPaymentDialog(
    model: LoanDetailsDialogState.MakePaymentDialog,
    onEvent: (LoanDetailsEvent) -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = { onEvent.invoke(LoanDetailsEvent.MakeLoanPaymentDialogClose) },
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "Внести платёж по кредиту",
                    style = S16_W500,
                )
                16.dp.verticalSpacer()
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = model.amount,
                    onValueChange = { onEvent(LoanDetailsEvent.ChangeLoanPaymentAmount(it)) },
                    label = {
                        Text(text = "Сумма")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )
                16.dp.verticalSpacer()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { onEvent(LoanDetailsEvent.MakeLoanPaymentDialogClose) },
                    ) {
                        Text(
                            text = "Отмена",
                            style = S16_W400,
                        )
                    }
                    8.dp.horizontalSpacer()
                    Button(
                        onClick = { onEvent(LoanDetailsEvent.ConfirmLoanPayment) },
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