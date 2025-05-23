package ru.hitsbank.clientbankapplication.loan.presentation.model.tariff

import ru.hitsbank.bank_common.presentation.common.component.dropdown.DropdownItem
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingOrder

enum class SortingOrder(override val title: String) : DropdownItem {
    ASCENDING("По возрастанию"),
    DESCENDING("По убыванию"),
}

fun SortingOrder.toDomain(): LoanTariffSortingOrder {
    return when (this) {
        SortingOrder.ASCENDING -> LoanTariffSortingOrder.ASCENDING
        SortingOrder.DESCENDING -> LoanTariffSortingOrder.DESCENDING
    }
}