package ru.hitsbank.clientbankapplication.loan.data.mapper

import ru.hitsbank.clientbankapplication.loan.data.model.LoanCreateRequest
import ru.hitsbank.clientbankapplication.loan.data.model.LoanPaymentResponse
import ru.hitsbank.clientbankapplication.loan.data.model.LoanResponse
import ru.hitsbank.clientbankapplication.loan.data.model.LoanTariffResponse
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanCreateEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanPaymentEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffEntity
import javax.inject.Inject

class LoanMapper @Inject constructor() {

    fun map(loanTariffResponse: LoanTariffResponse): LoanTariffEntity {
        return LoanTariffEntity(
            id = loanTariffResponse.id,
            name = loanTariffResponse.name,
            interestRate = loanTariffResponse.interestRate,
        )
    }

    fun map(loanResponse: LoanResponse): LoanEntity {
        return LoanEntity(
            id = loanResponse.id,
            number = loanResponse.number,
            tariff = map(loanResponse.tariff),
            amount = loanResponse.amount,
            termInMonths = loanResponse.termInMonths,
            bankAccountId = loanResponse.bankAccountId,
            bankAccountNumber = loanResponse.bankAccountNumber,
            paymentAmount = loanResponse.paymentAmount,
            paymentSum = loanResponse.paymentSum,
            currencyCode = loanResponse.currencyCode,
            nextPaymentDateTime = loanResponse.nextPaymentDateTime,
            currentDebt = loanResponse.currentDebt,
        )
    }

    fun map(loanCreateEntity: LoanCreateEntity): LoanCreateRequest {
        return LoanCreateRequest(
            tariffId = loanCreateEntity.tariffId,
            amount = loanCreateEntity.amount,
            termInMonths = loanCreateEntity.termInMonths,
            bankAccountId = loanCreateEntity.bankAccountId,
            bankAccountNumber = loanCreateEntity.bankAccountNumber,
        )
    }

    fun map(loanPaymentResponse: LoanPaymentResponse): LoanPaymentEntity {
        return LoanPaymentEntity(
            id = loanPaymentResponse.id,
            status = loanPaymentResponse.status,
            dateTime = loanPaymentResponse.dateTime,
            amount = loanPaymentResponse.amount,
            currencyCode = loanPaymentResponse.currencyCode,
        )
    }
}