package ru.hitsbank.clientbankapplication.bank_account.presentation.mapper

import ru.hitsbank.bank_common.presentation.common.formatToSum
import ru.hitsbank.bank_common.presentation.common.toFullName
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferInfo
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.TransferDetailsScreenItem
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.TransferDetailsScreenState
import javax.inject.Inject

class TransferDetailsMapper @Inject constructor() {

    fun map(info: TransferInfo): TransferDetailsScreenState {
        return TransferDetailsScreenState(
            items = listOf(
                TransferDetailsScreenItem(
                    subtitle = "Номер счета списания",
                    value = info.senderAccountInfo.accountNumber,
                    copyable = true,
                ),
                TransferDetailsScreenItem(
                    subtitle = "Валюта счета списания",
                    value = info.senderAccountInfo.accountCurrencyCode.toFullName(),
                    copyable = false,
                ),
                TransferDetailsScreenItem(
                    subtitle = "Номер счета получателя",
                    value = info.receiverAccountInfo.accountNumber,
                    copyable = true,
                ),
                TransferDetailsScreenItem(
                    subtitle = "Валюта счета получателя",
                    value = info.receiverAccountInfo.accountCurrencyCode.toFullName(),
                    copyable = false,
                ),
                TransferDetailsScreenItem(
                    subtitle = "Сумма перевода до конвертации",
                    value = info.transferAmountBeforeConversion.formatToSum(
                        currencyCode = info.senderAccountInfo.accountCurrencyCode
                    ),
                    copyable = false,
                ),
                TransferDetailsScreenItem(
                    subtitle = "Сумма перевода после конвертации",
                    value = info.transferAmountAfterConversion.formatToSum(
                        currencyCode = info.receiverAccountInfo.accountCurrencyCode
                    ),
                    copyable = false,
                ),
            ),
            isPerformingAction = false,
        )
    }
}