package ru.hitsbank.clientbankapplication.loan.presentation.mapper

import ru.hitsbank.clientbankapplication.core.presentation.common.formatToSum
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanItem

class LoanListMapper {

    fun map(loan: LoanEntity): LoanItem {
        return LoanItem(
            id = loan.id,
            number = loan.number,
            description = "Долг: ${loan.currentDebt.formatToSum(loan.currencyCode)}",
        )
    }
}