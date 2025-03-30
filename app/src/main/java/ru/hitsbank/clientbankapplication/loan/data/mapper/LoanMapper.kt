package ru.hitsbank.clientbankapplication.loan.data.mapper

import ru.hitsbank.clientbankapplication.loan.data.model.LoanCreateRequest
import ru.hitsbank.clientbankapplication.loan.data.model.LoanResponse
import ru.hitsbank.clientbankapplication.loan.data.model.LoanTariffResponse
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanCreateEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

class LoanMapper {

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
            nextPaymentDateTime = LocalDateTime.parse(loanResponse.nextPaymentDateTime).atZone(ZoneOffset.UTC).toLocalDateTime(),
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
}