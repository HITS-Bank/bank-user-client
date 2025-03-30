package ru.hitsbank.clientbankapplication.bank_account.presentation.mapper

import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationTypeEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsItem
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsScreenModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsTopUpDialogModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsWithdrawDialogModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.CloseAccountDialog
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.OperationHistoryItem
import ru.hitsbank.clientbankapplication.core.constants.Constants.DEFAULT_PAGE_SIZE
import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode
import ru.hitsbank.clientbankapplication.core.presentation.common.formatToSum
import ru.hitsbank.clientbankapplication.core.presentation.common.utcDateTimeToReadableFormat
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationState
import javax.inject.Inject

class AccountDetailsMapper @Inject constructor() {

    fun mapToAccountDetailsScreenModel(
        isUserBlocked: Boolean,
        bankAccountEntity: BankAccountEntity,
    ): AccountDetailsScreenModel {
        return AccountDetailsScreenModel(
            id = bankAccountEntity.id,
            balance = bankAccountEntity.balance,
            number = bankAccountEntity.number,
            status = bankAccountEntity.status,
            accountDetails = AccountDetailsModel(
                items = buildList {
                    add(getAccountInfoItem(bankAccountEntity))
                    add(getBalanceOrStatusInfo(bankAccountEntity))
                }
            ),
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
            isUserBlocked = isUserBlocked,
            paginationState = PaginationState.Idle,
            data = emptyList(),
            pageNumber = 1,
            pageSize = DEFAULT_PAGE_SIZE,
        )
    }

    fun mapToOperationHistoryItems(operationHistory: List<OperationEntity>): List<OperationHistoryItem> {
        return operationHistory.map { operation ->
            OperationHistoryItem(
                id = operation.id,
                title = when (operation.type) {
                    OperationTypeEntity.WITHDRAWAL -> "Вывод"
                    OperationTypeEntity.TOP_UP -> "Пополнение"
                    OperationTypeEntity.LOAN_PAYMENT -> "Выплата по кредиту"
                    OperationTypeEntity.TRANSFER_INCOMING -> "Входящий перевод"
                    OperationTypeEntity.TRANSFER_OUTGOING -> "Исходящий перевод"
                },
                description = operation.executedAt.utcDateTimeToReadableFormat(),
                operationType = OperationHistoryItem.OperationType.IN,
                amountText = if (operation.type == OperationTypeEntity.TOP_UP || operation.type == OperationTypeEntity.TRANSFER_INCOMING) {
                    "+${operation.amount.formatToSum(operation.currencyCode, true)}"
                } else {
                    "-${operation.amount.formatToSum(operation.currencyCode, true)}"
                },
                leftPartBackgroundColorId = if (operation.type == OperationTypeEntity.TOP_UP || operation.type == OperationTypeEntity.TRANSFER_INCOMING) {
                    R.color.operationInContainer
                } else {
                    R.color.operationOutContainer
                },
                contentColorId = if (operation.type == OperationTypeEntity.TOP_UP || operation.type == OperationTypeEntity.TRANSFER_INCOMING) {
                    R.color.operationInContent
                } else {
                    R.color.operationOutContent
                },
            )
        }
    }

    fun getUpdatedAccountDetailsScreen(
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
            balance = bankAccountEntity.balance,
            status = bankAccountEntity.status,
        )
    }

    fun mapToTopUpRequest(
        currencyCode: CurrencyCode,
        amount: String,
    ): TopUpRequest {
        return TopUpRequest(
            currencyCode = currencyCode,
            amount = amount,
        )
    }

    fun mapToWithdrawRequest(
        currencyCode: CurrencyCode,
        amount: String,
    ): WithdrawRequest {
        return WithdrawRequest(
            currencyCode = currencyCode,
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
                bankAccountEntity.balance.formatToSum(bankAccountEntity.currencyCode)
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