package ru.hitsbank.clientbankapplication.loan.presentation.model.create

import ru.hitsbank.bank_common.presentation.common.BankUiState

data class LoanCreateState(
    val amount: String,
    val termInMonths: String,
    val tariffId: String?,
    val tariffName: String?,
    val accountNumber: String?,
    val accountId: String?,
    val loanRatingState: BankUiState<Int>,
    val isPerformingAction: Boolean,
) {
    val canCreateLoan: Boolean
        get() = amount.isNotBlank() &&
                termInMonths.isNotBlank() &&
                amount.toFloatOrNull() != null &&
                termInMonths.toIntOrNull() != null &&
                amount.toFloat() > 0 &&
                termInMonths.toInt() > 0 &&
                tariffId != null &&
                accountNumber != null &&
                !isPerformingAction

    companion object {
        val EMPTY = LoanCreateState(
            amount = "",
            termInMonths = "",
            tariffId = null,
            tariffName = null,
            accountNumber = null,
            accountId = null,
            isPerformingAction = false,
            loanRatingState = BankUiState.Loading,
        )
    }
}
