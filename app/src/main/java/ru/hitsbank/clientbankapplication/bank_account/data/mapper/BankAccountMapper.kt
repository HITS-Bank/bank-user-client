package ru.hitsbank.clientbankapplication.bank_account.data.mapper

import ru.hitsbank.clientbankapplication.bank_account.data.model.BankAccountResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationTypeResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.TransferAccountInfoModel
import ru.hitsbank.clientbankapplication.bank_account.data.model.TransferConfirmationModel
import ru.hitsbank.clientbankapplication.bank_account.data.model.TransferInfoModel
import ru.hitsbank.clientbankapplication.bank_account.data.model.TransferRequestModel
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationTypeEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferAccountInfo
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferConfirmation
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferInfo
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferRequest
import ru.hitsbank.bank_common.data.model.OperationResponse as CommonOperationResponse
import ru.hitsbank.bank_common.data.model.OperationTypeResponse as CommonOperationTypeResponse
import javax.inject.Inject

class BankAccountMapper @Inject constructor() {

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
                    OperationTypeResponse.TRANSFER_INCOMING -> OperationTypeEntity.TRANSFER_INCOMING
                    OperationTypeResponse.TRANSFER_OUTGOING -> OperationTypeEntity.TRANSFER_OUTGOING
                },
                amount = operation.amount,
                currencyCode = operation.currencyCode,
            )
        }
    }

    fun map(response: CommonOperationResponse): OperationEntity {
        return OperationEntity(
            id = response.id,
            executedAt = response.executedAt,
            type = when (response.type) {
                CommonOperationTypeResponse.WITHDRAW -> OperationTypeEntity.WITHDRAWAL
                CommonOperationTypeResponse.TOP_UP -> OperationTypeEntity.TOP_UP
                CommonOperationTypeResponse.LOAN_PAYMENT -> OperationTypeEntity.LOAN_PAYMENT
                CommonOperationTypeResponse.TRANSFER_INCOMING -> OperationTypeEntity.TRANSFER_INCOMING
                CommonOperationTypeResponse.TRANSFER_OUTGOING -> OperationTypeEntity.TRANSFER_OUTGOING
            },
            amount = response.amount,
            currencyCode = response.currencyCode,
        )
    }

    fun map(request: TransferRequest): TransferRequestModel {
        return TransferRequestModel(
            senderAccountId = request.senderAccountId,
            receiverAccountNumber = request.receiverAccountNumber,
            transferAmount = request.transferAmount,
        )
    }

    fun map(confirmation: TransferConfirmation): TransferConfirmationModel {
        return TransferConfirmationModel(
            senderAccountId = confirmation.senderAccountId,
            receiverAccountNumber = confirmation.receiverAccountNumber,
            transferAmount = confirmation.transferAmount,
        )
    }

    fun map(model: TransferInfoModel): TransferInfo {
        return TransferInfo(
            senderAccountInfo = map(model.senderAccountInfo),
            receiverAccountInfo = map(model.receiverAccountInfo),
            transferAmountBeforeConversion = model.transferAmountBeforeConversion,
            transferAmountAfterConversion = model.transferAmountAfterConversion,
        )
    }

    fun map(model: TransferAccountInfoModel): TransferAccountInfo {
        return TransferAccountInfo(
            accountNumber = model.accountNumber,
            accountCurrencyCode = model.accountCurrencyCode,
            accountId = model.accountId,
        )
    }
}