package ru.hitsbank.clientbankapplication.bank_account.presentation.mapper

import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountItem
import ru.hitsbank.clientbankapplication.core.presentation.common.formatToSum

class AccountListMapper {

    fun map(entity: AccountListEntity): List<AccountItem> {
        return entity.bankAccounts.map { accountEntity ->
            AccountItem(
                number = accountEntity.number,
                description = getBankAccountDescription(accountEntity.balance, accountEntity.status),
                descriptionColorId = getBankAccountColorId(accountEntity.status),
            )
        }
    }

    private fun getBankAccountDescription(
        balance: String,
        statusEntity: BankAccountStatusEntity,
    ): String {
        return when (statusEntity) {
            BankAccountStatusEntity.CLOSED -> {
                "Счет закрыт"
            }

            BankAccountStatusEntity.BLOCKED -> {
                "Счет заблокирован"
            }

            else -> {
                "Баланс: ${balance.formatToSum()}"
            }
        }
    }

    private fun getBankAccountColorId(
        statusEntity: BankAccountStatusEntity,
    ): Int {
        return when (statusEntity) {
            BankAccountStatusEntity.CLOSED -> {
                R.color.onPrimaryContainer
            }

            BankAccountStatusEntity.BLOCKED -> {
                R.color.onErrorContainer
            }

            else -> {
                R.color.onSurfaceVariant
            }
        }
    }
}