package ru.hitsbank.clientbankapplication.loan.presentation.mapper

import ru.hitsbank.bank_common.presentation.common.toSymbol
import ru.hitsbank.bank_common.presentation.common.utcDateTimeToReadableFormat
import ru.hitsbank.bank_common.presentation.theme.grayBackground
import ru.hitsbank.bank_common.presentation.theme.grayForeground
import ru.hitsbank.bank_common.presentation.theme.topUpBackground
import ru.hitsbank.bank_common.presentation.theme.topUpForeground
import ru.hitsbank.bank_common.presentation.theme.withdrawBackground
import ru.hitsbank.bank_common.presentation.theme.withdrawForeground
import ru.hitsbank.clientbankapplication.core.presentation.common.formatToSum
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanPaymentEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanPaymentStatus
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanPaymentItem
import javax.inject.Inject

class LoanPaymentsMapper @Inject constructor() {

    fun map(loanPaymentEntity: LoanPaymentEntity): LoanPaymentItem {
        return LoanPaymentItem(
            id = loanPaymentEntity.id,
            title = loanPaymentEntity.dateTime.utcDateTimeToReadableFormat(),
            description = loanPaymentEntity.amount.formatToSum(loanPaymentEntity.currencyCode, true),
            status = when (loanPaymentEntity.status) {
                LoanPaymentStatus.PLANNED -> "Запланирован"
                LoanPaymentStatus.OVERDUE -> "Просрочен"
                LoanPaymentStatus.EXECUTED -> "Оплачен"
            },
            currencyChar = loanPaymentEntity.currencyCode.toSymbol(),
            foregroundColor = when (loanPaymentEntity.status) {
                LoanPaymentStatus.PLANNED -> grayForeground
                LoanPaymentStatus.OVERDUE -> withdrawForeground
                LoanPaymentStatus.EXECUTED -> topUpForeground
            },
            backgroundColor = when (loanPaymentEntity.status) {
                LoanPaymentStatus.PLANNED -> grayBackground
                LoanPaymentStatus.OVERDUE -> withdrawBackground
                LoanPaymentStatus.EXECUTED -> topUpBackground
            },
        )
    }
}