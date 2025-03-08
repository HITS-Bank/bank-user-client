package ru.hitsbank.clientbankapplication.loan.presentation.model

sealed interface LoanDetailsDialogState {

    data object None : LoanDetailsDialogState

    data class MakePaymentDialog(
        val amount: String = "",
    ) : LoanDetailsDialogState {
        val isDataValid: Boolean
            get() = amount.isNotEmpty() && amount.toFloatOrNull() != null && amount.toFloat() > 0
    }
}

fun LoanDetailsDialogState.getAmount(): String? {
    return when (this) {
        is LoanDetailsDialogState.MakePaymentDialog -> amount
        else -> null
    }
}