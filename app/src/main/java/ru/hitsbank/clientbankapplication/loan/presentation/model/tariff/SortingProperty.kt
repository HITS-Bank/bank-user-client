package ru.hitsbank.clientbankapplication.loan.presentation.model.tariff

import ru.hitsbank.bank_common.presentation.common.component.dropdown.DropdownItem
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingProperty

enum class SortingProperty(override val title: String) : DropdownItem {
    NAME("Названию"),
    INTEREST_RATE("Процентной ставке"),
    CREATED_AT("Времени создания"),
}

fun SortingProperty.toDomain(): LoanTariffSortingProperty {
    return when (this) {
        SortingProperty.NAME -> LoanTariffSortingProperty.NAME
        SortingProperty.INTEREST_RATE -> LoanTariffSortingProperty.INTEREST_RATE
        SortingProperty.CREATED_AT -> LoanTariffSortingProperty.CREATED_AT
    }
}