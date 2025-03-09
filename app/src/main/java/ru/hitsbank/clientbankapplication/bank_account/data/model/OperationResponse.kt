package ru.hitsbank.clientbankapplication.bank_account.data.model

data class OperationResponse(
    val id: String,
    val executedAt: String,
    val type: OperationTypeResponse,
    val amount: String,
)

// TODO с сервера должен приходить WITHDRAW
enum class OperationTypeResponse {
    WITHDRAW,
    TOP_UP,
    LOAN_PAYMENT,
}