package ru.hitsbank.clientbankapplication.loan.presentation.event.payment

sealed interface LoanPaymentsEvent {

    data object Back : LoanPaymentsEvent

    data object Refresh : LoanPaymentsEvent

    data object LoanInfoClick : LoanPaymentsEvent
}