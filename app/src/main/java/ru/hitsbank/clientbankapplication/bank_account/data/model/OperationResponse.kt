package ru.hitsbank.clientbankapplication.bank_account.data.model

import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode

data class OperationResponse(
    val id: String,
    val executedAt: String,
    val type: OperationTypeResponse,
    val amount: String,
    val currencyCode: CurrencyCode,
)

// TODO с сервера должен приходить WITHDRAW
enum class OperationTypeResponse {
    WITHDRAW,
    TOP_UP,
    LOAN_PAYMENT,
    TRANSFER_INCOMING,
    TRANSFER_OUTGOING,
}