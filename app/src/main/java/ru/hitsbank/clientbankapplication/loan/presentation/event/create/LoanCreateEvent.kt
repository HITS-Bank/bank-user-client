package ru.hitsbank.clientbankapplication.loan.presentation.event.create

sealed interface LoanCreateEvent {

    data object Back : LoanCreateEvent

    data object ConfirmCreate : LoanCreateEvent

    data class ChangeAmount(val amount: String) : LoanCreateEvent

    data class ChangeTerm(val term: String) : LoanCreateEvent

    data object SelectTariff : LoanCreateEvent

    data object SelectAccount : LoanCreateEvent
}