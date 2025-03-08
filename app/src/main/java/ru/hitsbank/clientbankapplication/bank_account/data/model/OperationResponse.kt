package ru.hitsbank.clientbankapplication.bank_account.data.model

data class OperationResponse(
    val id: String,
    val executedAt: String,
    val type: OperationTypeResponse,
    val amount: String,
)

enum class OperationTypeResponse {
    WITHDRAWAL,
    TOP_UP,
    LOAN_PAYMENT,
}