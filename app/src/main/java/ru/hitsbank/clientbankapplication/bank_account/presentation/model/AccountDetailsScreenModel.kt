package ru.hitsbank.clientbankapplication.bank_account.presentation.model

import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationStateHolder

data class AccountDetailsScreenModel(
    override val paginationState: PaginationState,
    override val data: List<OperationHistoryItem>,
    override val pageNumber: Int,
    override val pageSize: Int,
    val balance: String,
    val number: String,
    val isUserBlocked: Boolean,
    val status: BankAccountStatusEntity,
    val accountDetails: AccountDetailsModel,
    val topUpDialog: AccountDetailsTopUpDialogModel,
    val withdrawDialog: AccountDetailsWithdrawDialogModel,
    val closeAccountDialog: CloseAccountDialog,
    val isOverlayLoading: Boolean,
) : PaginationStateHolder<OperationHistoryItem> {

    override fun copyWith(
        paginationState: PaginationState,
        data: List<OperationHistoryItem>,
        pageNumber: Int,
    ): PaginationStateHolder<OperationHistoryItem> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<OperationHistoryItem> {
        return copy(data = emptyList(), pageNumber = 1)
    }
}

data class AccountDetailsTopUpDialogModel(
    val isShown: Boolean,
    val amount: String,
    val isDataValid: Boolean,
) {

    companion object {
        const val DEFAULT_AMOUNT = 10000
    }
}

data class AccountDetailsWithdrawDialogModel(
    val isShown: Boolean,
    val amount: String,
    val isDataValid: Boolean,
) {

    companion object {
        const val DEFAULT_AMOUNT = 10000
    }
}

data class CloseAccountDialog(
    val isShown: Boolean,
)

data class AccountDetailsModel(
    val items: List<AccountDetailsItem>,
) {

    enum class AccountStatus {
        OPEN,
        CLOSED,
        BLOCKED,
        ;
    }
}

data class AccountDetailsItem(
    val title: String,
    val subtitle: String,
)

data class OperationHistoryItem(
    val title: String,
    val description: String,
    val operationType: OperationType,
    val amountText: String,
    val leftPartBackgroundColorId: Int,
    val contentColorId: Int,
) {

    enum class OperationType {
        IN,
        OUT,
        ;
    }
}
