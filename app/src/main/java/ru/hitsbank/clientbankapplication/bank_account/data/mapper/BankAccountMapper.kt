package ru.hitsbank.clientbankapplication.bank_account.data.mapper

import ru.hitsbank.clientbankapplication.bank_account.data.model.AccountListResponse
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity

class BankAccountMapper {

    fun map(response: AccountListResponse): AccountListEntity {
        return AccountListEntity(
            bankAccounts = response.bankAccounts.map { entity ->
                BankAccountEntity(
                    number = entity.number,
                    balance = entity.balance,
                    status = entity.status,
                )
            },
            pageInfo = response.pageInfo,
        )
    }
}