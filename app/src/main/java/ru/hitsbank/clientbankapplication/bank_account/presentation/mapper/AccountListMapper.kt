package ru.hitsbank.clientbankapplication.bank_account.presentation.mapper

import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.presentation.common.formatToSum
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountItem
import javax.inject.Inject

class AccountListMapper @Inject constructor() {

    fun map(entity: AccountListEntity, hiddenAccountIds: List<String>): List<AccountItem> {
        return entity.bankAccounts.map { accountEntity ->
            AccountItem(
                id = accountEntity.id,
                number = accountEntity.number,
                description = getBankAccountDescription(
                    accountEntity.balance,
                    accountEntity.currencyCode,
                    accountEntity.status,
                ),
                descriptionColorId = getBankAccountColorId(accountEntity.status),
                isHidden = hiddenAccountIds.contains(accountEntity.id),
            )
        }
    }

    private fun getBankAccountDescription(
        balance: String,
        currencyCode: CurrencyCode,
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
                "Баланс: ${balance.formatToSum(currencyCode)}"
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