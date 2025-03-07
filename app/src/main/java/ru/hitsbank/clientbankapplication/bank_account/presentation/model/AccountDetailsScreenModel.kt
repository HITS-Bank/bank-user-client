package ru.hitsbank.clientbankapplication.bank_account.presentation.model

data class AccountDetailsScreenModel(
    val accountDetails: AccountDetailsModel,
    val operationsHistory: List<OperationsHistoryItem>,
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
