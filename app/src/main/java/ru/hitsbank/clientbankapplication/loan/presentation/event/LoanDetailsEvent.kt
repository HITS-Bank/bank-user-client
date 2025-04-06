package ru.hitsbank.clientbankapplication.loan.presentation.event

sealed interface LoanDetailsEvent {

    data object Back : LoanDetailsEvent

    data class OpenBankAccount(val accountId: String) : LoanDetailsEvent

    data object MakeLoanPaymentDialogOpen : LoanDetailsEvent

    data object MakeLoanPaymentDialogClose : LoanDetailsEvent

    data class ChangeLoanPaymentAmount(val amount: String) : LoanDetailsEvent

    data object ConfirmLoanPayment : LoanDetailsEvent
}