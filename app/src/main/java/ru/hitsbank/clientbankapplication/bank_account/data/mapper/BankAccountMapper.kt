package ru.hitsbank.clientbankapplication.bank_account.data.mapper

import ru.hitsbank.clientbankapplication.bank_account.data.model.AccountListResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.BankAccountResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationHistoryResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationTypeResponse
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationTypeEntity

class BankAccountMapper {

    fun map(response: AccountListResponse): AccountListEntity {
        return AccountListEntity(
            bankAccounts = response.bankAccounts.map { response ->
                BankAccountEntity(
                    number = response.number,
                    balance = response.balance,
                    status = response.status,
                )
            },
            pageInfo = response.pageInfo,
        )
    }

    fun map(response: BankAccountResponse): BankAccountEntity {
        return BankAccountEntity(
            number = response.number,
            balance = response.balance,
            status = response.status,
        )
    }

    fun map(response: OperationHistoryResponse): List<OperationEntity> {
        return response.operations.map { operation ->
            OperationEntity(
                id = operation.id,
                executedAt = operation.executedAt,
                type = when (operation.type) {
                    OperationTypeResponse.WITHDRAWAL -> OperationTypeEntity.WITHDRAWAL
                    OperationTypeResponse.TOP_UP -> OperationTypeEntity.TOP_UP
                    OperationTypeResponse.LOAN_PAYMENT -> OperationTypeEntity.LOAN_PAYMENT
                },
                balance = operation.balance,
            )
        }
    }
}