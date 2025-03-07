package ru.hitsbank.clientbankapplication.bank_account.domain.model

data class OperationEntity(
    val id: String,
    val executedAt: String,
    val type: OperationType,
    val balance: String,
)

enum class OperationType {
    WITHDRAWAL,
    TOP_UP,
    LOAN_PAYMENT,
}
