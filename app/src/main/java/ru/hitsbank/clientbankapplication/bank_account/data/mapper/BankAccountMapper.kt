package ru.hitsbank.clientbankapplication.bank_account.data.mapper

import ru.hitsbank.clientbankapplication.bank_account.data.model.BankAccountResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationTypeResponse
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationTypeEntity

class BankAccountMapper {

    fun map(response: List<BankAccountResponse>): AccountListEntity {
        return AccountListEntity(
            bankAccounts = response.map(::map),
        )
    }

    fun map(response: BankAccountResponse): BankAccountEntity {
        return BankAccountEntity(
            id = response.accountId,
            number = response.accountNumber,
            balance = response.balance,
            currencyCode = response.currencyCode,
            status = when {
                response.closed -> BankAccountStatusEntity.CLOSED
                response.blocked -> BankAccountStatusEntity.BLOCKED
                else -> BankAccountStatusEntity.OPEN
            },
        )
    }

    fun map(response: List<OperationResponse>): List<OperationEntity> {
        return response.map { operation ->
            OperationEntity(
                id = operation.id,
                executedAt = operation.executedAt,
                type = when (operation.type) {
                    OperationTypeResponse.WITHDRAW -> OperationTypeEntity.WITHDRAWAL
                    OperationTypeResponse.TOP_UP -> OperationTypeEntity.TOP_UP
                    OperationTypeResponse.LOAN_PAYMENT -> OperationTypeEntity.LOAN_PAYMENT
                },
                amount = operation.amount,
            )
        }
    }
}