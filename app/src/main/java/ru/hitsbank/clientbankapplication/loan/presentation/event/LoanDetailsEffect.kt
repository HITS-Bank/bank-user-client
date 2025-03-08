package ru.hitsbank.clientbankapplication.loan.presentation.event

sealed interface LoanDetailsEffect {

    data object ShowLoanPaymentError : LoanDetailsEffect
}