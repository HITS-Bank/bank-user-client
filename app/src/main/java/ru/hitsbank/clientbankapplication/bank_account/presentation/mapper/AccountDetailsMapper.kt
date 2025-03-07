package ru.hitsbank.clientbankapplication.bank_account.presentation.mapper

import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsItem
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsScreenModel
import ru.hitsbank.clientbankapplication.core.presentation.common.formatToSum

class AccountDetailsMapper {

    fun map(
        bankAccountEntity: BankAccountEntity,
        operationsHistoryEntity: List<OperationEntity>,
    ): AccountDetailsScreenModel {
        return AccountDetailsScreenModel(
            accountDetails = AccountDetailsModel(
                items = buildList {
                    add(getAccountInfoItem(bankAccountEntity))
                    add(getBalanceOrStatusInfo(bankAccountEntity))
                }
            ),
            operationsHistory = listOf(), // TODO
        )
    }

    private fun getAccountInfoItem(bankAccountEntity: BankAccountEntity): AccountDetailsItem {
        return AccountDetailsItem(
            title = bankAccountEntity.number,
            subtitle = "Номер счета",
        )
    }

    private fun getBalanceOrStatusInfo(bankAccountEntity: BankAccountEntity): AccountDetailsItem {
        return AccountDetailsItem(
            title = if (bankAccountEntity.status != BankAccountStatusEntity.CLOSED) {
                bankAccountEntity.balance.formatToSum()
            } else {
                "Закрыт"
            },
            subtitle = if (bankAccountEntity.status != BankAccountStatusEntity.CLOSED) {
                "Баланс"
            } else {
                "Статус"
            },
        )
    }
}