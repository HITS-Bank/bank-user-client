package ru.hitsbank.clientbankapplication.bank_account.domain.model

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class OperationEntity(
    val id: String,
    val executedAt: String,
    val type: OperationTypeEntity,
    val amount: String,
    val currencyCode: CurrencyCode,
)

enum class OperationTypeEntity {
    WITHDRAWAL,
    TOP_UP,
    LOAN_PAYMENT,
    TRANSFER_INCOMING,
    TRANSFER_OUTGOING,
}
