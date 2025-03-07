package ru.hitsbank.clientbankapplication.bank_account.data.mapper

import ru.hitsbank.clientbankapplication.bank_account.data.model.BankAccountResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.AccountListResponse
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity

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
}