package ru.hitsbank.clientbankapplication.bank_account.presentation.model

data class AccountDetailsScreenModel(
    val balance: String,
    val number: String,
    val isUserBlocked: Boolean,
    val accountDetails: AccountDetailsModel,
    val operationsHistory: List<OperationsHistoryItem>,
    val topUpDialog: AccountDetailsTopUpDialogModel,
    val withdrawDialog: AccountDetailsWithdrawDialogModel,
    val closeAccountDialog: CloseAccountDialog,
    val isOverlayLoading: Boolean,
)

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

data class OperationsHistoryItem(
    val title: String,
    val description: String,
    val operationType: OperationType,
) {

    enum class OperationType {
        IN,
        OUT,
        ;
    }
}
