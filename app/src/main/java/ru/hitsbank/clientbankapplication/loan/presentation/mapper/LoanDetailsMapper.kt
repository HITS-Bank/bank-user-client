package ru.hitsbank.clientbankapplication.loan.presentation.mapper

import ru.hitsbank.bank_common.presentation.common.formatToSum
import ru.hitsbank.bank_common.presentation.common.utcDateTimeToReadableFormat
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanDetailsListItem
import javax.inject.Inject

class LoanDetailsMapper @Inject constructor() {

    fun map(loan: LoanEntity): List<LoanDetailsListItem> {
        return listOf(
            LoanDetailsListItem.LoanInfoHeader,
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.number,
                name = "Номер кредита",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.tariff.name,
                name = "Название тарифа",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.tariff.interestRate,
                name = "Процентная ставка",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.termInMonths.toString(),
                name = "Срок кредита (мес.)",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.amount.formatToSum(loan.currencyCode),
                name = "Сумма кредита",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.paymentSum.formatToSum(loan.currencyCode),
                name = "Сумма выплат"
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.paymentAmount.formatToSum(loan.currencyCode),
                name = "Сумма платежа"
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.nextPaymentDateTime.utcDateTimeToReadableFormat(),
                name = "Время следующего платежа",
            ),
            LoanDetailsListItem.LoanDetailsProperty(
                value = loan.currentDebt.formatToSum(loan.currencyCode),
                name = "Текущий долг",
            ),
            LoanDetailsListItem.LoanBankAccount(
                accountId = loan.bankAccountId,
                value = loan.bankAccountNumber,
                name = "Счет кредита",
                accountNumber = loan.bankAccountId,
            ),
            LoanDetailsListItem.MakePaymentButton,
        )
    }
}