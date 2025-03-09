package ru.hitsbank.clientbankapplication.loan.presentation.event.create

sealed interface LoanCreateEffect {

    data object ShowLoanCreationError : LoanCreateEffect
}