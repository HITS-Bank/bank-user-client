package ru.hitsbank.clientbankapplication.loan.presentation.model

sealed interface LoanDetailsListItem {

    data class LoanDetailsProperty(
        val value: String,
        val name: String,
    ) : LoanDetailsListItem

    data class LoanBankAccount(
        val accountId: String,
        val accountNumber: String,
        val value: String,
        val name: String,
    ) : LoanDetailsListItem

    data object LoanInfoHeader : LoanDetailsListItem

    data object MakePaymentButton : LoanDetailsListItem
}