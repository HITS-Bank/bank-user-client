package ru.hitsbank.clientbankapplication.loan.presentation.event.tariff

import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.TariffModel
import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.SortingOrder
import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.SortingProperty

sealed interface TariffsScreenEvent {

    data class QueryChanged(
        val query: String,
    ) : TariffsScreenEvent

    data class SortingOrderChanged(
        val sortingOrder: SortingOrder
    ) : TariffsScreenEvent

    data object OpenSortingOrderMenu : TariffsScreenEvent

    data object CloseSortingOrderMenu : TariffsScreenEvent

    data class SortingPropertyChanged(
        val sortingProperty: SortingProperty
    ) : TariffsScreenEvent

    data object OpenSortingPropertyMenu : TariffsScreenEvent

    data object CloseSortingPropertyMenu : TariffsScreenEvent

    data class TariffSelected(
        val tariff: TariffModel
    ) : TariffsScreenEvent

    data object Back : TariffsScreenEvent
}