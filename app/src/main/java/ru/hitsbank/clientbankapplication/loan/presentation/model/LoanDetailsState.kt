package ru.hitsbank.clientbankapplication.loan.presentation.model

data class LoanDetailsState(
    val detailItems: List<LoanDetailsListItem>,
    val dialogState: LoanDetailsDialogState,
    val isPerformingAction: Boolean,
    val isUserBlocked: Boolean,
) {
    companion object {
        fun default(detailItems: List<LoanDetailsListItem>, isBlocked: Boolean) = LoanDetailsState(
            detailItems = detailItems,
            dialogState = LoanDetailsDialogState.None,
            isPerformingAction = false,
            isUserBlocked = isBlocked,
        )
    }
}
