package ru.hitsbank.clientbankapplication.loan.data.repository

import kotlinx.coroutines.delay
import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.model.PageInfo
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanCreateEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingOrder
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingProperty
import ru.hitsbank.clientbankapplication.loan.domain.repository.ILoanRepository
import java.time.LocalDateTime
import java.util.UUID

class LoanRepository : ILoanRepository {

    override suspend fun getLoanTariffs(
        pageInfo: PageInfo,
        sortingProperty: LoanTariffSortingProperty,
        sortingOrder: LoanTariffSortingOrder,
        query: String?
    ): Result<List<LoanTariffEntity>> {
        //TODO
        delay(1000)
        return Result.Success(data = listOf(
            LoanTariffEntity(
                id = UUID.randomUUID().toString(),
                name = "Тариф 1",
                interestRate = "10.0",
            ),
            LoanTariffEntity(
                id = UUID.randomUUID().toString(),
                name = "Тариф 2",
                interestRate = "15.0",
            ),
            LoanTariffEntity(
                id = UUID.randomUUID().toString(),
                name = "Тариф 3",
                interestRate = "20.0",
            ),
        ))
    }

    override suspend fun getLoans(pageInfo: PageInfo): Result<List<LoanEntity>> {
        //TODO
        delay(1000)
        return Result.Success(data = listOf(
            LoanEntity(
                number = UUID.randomUUID().toString(),
                tariff = LoanTariffEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Тариф 1",
                    interestRate = "10.0",
                ),
                amount = "1000000",
                termInMonths = 12,
                bankAccountNumber = "12345678901234567890",
                paymentAmount = "100000",
                paymentSum = "1200000",
                nextPaymentDateTime = LocalDateTime.now(),
                currentDebt = "900000",
            ),
            LoanEntity(
                number = UUID.randomUUID().toString(),
                tariff = LoanTariffEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Тариф 2",
                    interestRate = "15.0",
                ),
                amount = "2000000",
                termInMonths = 24,
                bankAccountNumber = "12345678901234567890",
                paymentAmount = "100000",
                paymentSum = "2400000",
                nextPaymentDateTime = LocalDateTime.now(),
                currentDebt = "1900000",
            ),
            LoanEntity(
                number = UUID.randomUUID().toString(),
                tariff = LoanTariffEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Тариф 3",
                    interestRate = "20.0",
                ),
                amount = "3000000",
                termInMonths = 36,
                bankAccountNumber = "12345678901234567890",
                paymentAmount = "100000",
                paymentSum = "3600000",
                nextPaymentDateTime = LocalDateTime.now(),
                currentDebt = "2900000",
            ),
        ))
    }

    override suspend fun createLoan(loan: LoanCreateEntity): Result<LoanEntity> {
        //TODO
        delay(1000)
        return Result.Success(data = LoanEntity(
            number = UUID.randomUUID().toString(),
            tariff = LoanTariffEntity(
                id = UUID.randomUUID().toString(),
                name = "Тариф 1",
                interestRate = "10.0",
            ),
            amount = loan.amount,
            termInMonths = loan.termInMonths,
            bankAccountNumber = loan.bankAccountNumber,
            paymentAmount = "100000",
            paymentSum = "1200000",
            nextPaymentDateTime = LocalDateTime.now(),
            currentDebt = "900000",
        ))
    }

    override suspend fun makeLoanPayment(loanId: String, amount: String): Result<LoanEntity> {
        //TODO
        delay(1000)
        return Result.Success(data = LoanEntity(
            number = UUID.randomUUID().toString(),
            tariff = LoanTariffEntity(
                id = UUID.randomUUID().toString(),
                name = "Тариф 1",
                interestRate = "10.0",
            ),
            amount = "1000000",
            termInMonths = 12,
            bankAccountNumber = "12345678901234567890",
            paymentAmount = "100000",
            paymentSum = "1200000",
            nextPaymentDateTime = LocalDateTime.now(),
            currentDebt = "900000",
        ))
    }
}