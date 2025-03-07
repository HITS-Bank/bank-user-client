package ru.hitsbank.clientbankapplication.bank_account.presentation.mapper

import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsItem
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsScreenModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsTopUpDialogModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsWithdrawDialogModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.CloseAccountDialog
import ru.hitsbank.clientbankapplication.core.presentation.common.formatToSum

class AccountDetailsMapper {

    fun mapToAccountDetailsScreenModel(
        isUserBlocked: Boolean,
        bankAccountEntity: BankAccountEntity,
        operationsHistoryEntity: List<OperationEntity>,
    ): AccountDetailsScreenModel {
        return AccountDetailsScreenModel(
            balance = bankAccountEntity.balance,
            number = bankAccountEntity.number,
            accountDetails = AccountDetailsModel(
                items = buildList {
                    add(getAccountInfoItem(bankAccountEntity))
                    add(getBalanceOrStatusInfo(bankAccountEntity))
                }
            ),
            operationsHistory = listOf(), // TODO
            topUpDialog = AccountDetailsTopUpDialogModel(
                isShown = false,
                amount = AccountDetailsTopUpDialogModel.DEFAULT_AMOUNT.toString(),
                isDataValid = true,
            ),
            withdrawDialog = AccountDetailsWithdrawDialogModel(
                isShown = false,
                amount = AccountDetailsWithdrawDialogModel.DEFAULT_AMOUNT.toString(),
                isDataValid = true,
            ),
            closeAccountDialog = CloseAccountDialog(isShown = false),
            isOverlayLoading = false,
            isUserBlocked = isUserBlocked
        )
    }

    fun getUpdatedAccountDetails(
        oldModel: AccountDetailsScreenModel,
        bankAccountEntity: BankAccountEntity
    ): AccountDetailsScreenModel {
        return oldModel.copy(
            accountDetails = oldModel.accountDetails.copy(
                items = buildList {
                    add(getAccountInfoItem(bankAccountEntity))
                    add(getBalanceOrStatusInfo(bankAccountEntity))
                },
            ),
        )
    }

    fun mapToTopUpRequest(
        accountNumber: String,
        amount: String,
    ): TopUpRequest {
        return TopUpRequest(
            accountNumber = accountNumber,
            amount = amount,
        )
    }

    fun mapToWithdrawRequest(
        accountNumber: String,
        amount: String,
    ): WithdrawRequest {
        return WithdrawRequest(
            accountNumber = accountNumber,
            amount = amount,
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