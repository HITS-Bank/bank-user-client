package ru.hitsbank.clientbankapplication.loan.presentation.event

sealed interface LoanListEvent {

    data object CreateLoan : LoanListEvent

    data class OpenLoanDetails(val loanNumber: String) : LoanListEvent
}