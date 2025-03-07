package ru.hitsbank.clientbankapplication.bank_account.domain.model

data class OperationEntity(
    val id: String,
    val executedAt: String,
    val type: OperationTypeEntity,
    val balance: String,
)

enum class OperationTypeEntity {
    WITHDRAWAL,
    TOP_UP,
    LOAN_PAYMENT,
}
