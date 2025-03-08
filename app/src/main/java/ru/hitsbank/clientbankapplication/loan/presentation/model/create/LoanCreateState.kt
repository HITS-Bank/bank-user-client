package ru.hitsbank.clientbankapplication.loan.presentation.model.create

data class LoanCreateState(
    val amount: String,
    val termInMonths: String,
    val tariffId: String?,
    val tariffName: String?,
    val accountNumber: String?,
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
            isPerformingAction = false,
        )
    }
}
