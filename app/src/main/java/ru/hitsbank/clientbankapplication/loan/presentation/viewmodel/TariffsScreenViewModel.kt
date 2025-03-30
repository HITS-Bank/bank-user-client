package ru.hitsbank.clientbankapplication.loan.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.hitsbank.clientbankapplication.loan.presentation.event.tariff.TariffsScreenEvent
import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.TariffModel
import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.TariffsPaginationState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.hitsbank.clientbankapplication.core.common.dropFirstBlank
import ru.hitsbank.clientbankapplication.core.domain.common.State
import ru.hitsbank.clientbankapplication.core.domain.common.map
import ru.hitsbank.clientbankapplication.core.domain.model.PageInfo
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager
import ru.hitsbank.clientbankapplication.core.navigation.base.back
import ru.hitsbank.clientbankapplication.core.navigation.base.backWithJsonResult
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.getIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.common.updateIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationEvent
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationViewModel
import ru.hitsbank.clientbankapplication.loan.domain.interactor.LoanInteractor
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingOrder
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingProperty
import ru.hitsbank.clientbankapplication.loan.presentation.mapper.TariffsScreenModelMapper
import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.toDomain
import javax.inject.Inject

@HiltViewModel
class TariffsScreenViewModel @Inject constructor(
    private val loanInteractor: LoanInteractor,
    private val mapper: TariffsScreenModelMapper,
    private val navigationManager: NavigationManager,
    private val gson: Gson,
) : PaginationViewModel<TariffModel, TariffsPaginationState>(BankUiState.Ready(
    TariffsPaginationState.EMPTY)) {

    private val queryFlow = MutableStateFlow("")

    init {
        subscribeToQueryFlow()
        onPaginationEvent(PaginationEvent.Reload)
    }

    fun onEvent(event: TariffsScreenEvent) {
        when (event) {
            is TariffsScreenEvent.QueryChanged -> {
                _state.updateIfSuccess { state -> state.copy(queryDisplayText = event.query) }
                queryFlow.value = event.query
            }
            is TariffsScreenEvent.SortingOrderChanged -> {
                _state.updateIfSuccess { state ->
                    state.copy(
                        sortingOrder = event.sortingOrder,
                        isSortingOrderMenuOpen = false,
                    )
                }
                onPaginationEvent(PaginationEvent.Reload)
            }
            is TariffsScreenEvent.SortingPropertyChanged -> {
                _state.updateIfSuccess { state ->
                    state.copy(
                        sortingProperty = event.sortingProperty,
                        isSortingPropertyMenuOpen = false,
                    )
                }
                onPaginationEvent(PaginationEvent.Reload)
            }
            TariffsScreenEvent.CloseSortingOrderMenu -> {
                _state.updateIfSuccess { state -> state.copy(isSortingOrderMenuOpen = false) }
            }
            TariffsScreenEvent.CloseSortingPropertyMenu -> {
                _state.updateIfSuccess { state -> state.copy(isSortingPropertyMenuOpen = false) }
            }
            TariffsScreenEvent.OpenSortingOrderMenu -> {
                _state.updateIfSuccess { state -> state.copy(isSortingOrderMenuOpen = true) }
            }
            TariffsScreenEvent.OpenSortingPropertyMenu -> {
                _state.updateIfSuccess { state -> state.copy(isSortingPropertyMenuOpen = true) }
            }
            is TariffsScreenEvent.TariffSelected -> {
                navigationManager.backWithJsonResult(gson, mapper.map(event.tariff))
            }
            TariffsScreenEvent.Back -> {
                navigationManager.back()
            }
        }
    }

    override suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<TariffModel>>> {
        val pageInfo = PageInfo(
            pageNumber = pageNumber,
            pageSize = state.getIfSuccess()?.pageSize ?: PAGE_SIZE,
        )
        return loanInteractor.getLoanTariffs(
            pageInfo = pageInfo,
            sortingProperty = state.getIfSuccess()?.sortingProperty?.toDomain() ?: LoanTariffSortingProperty.NAME,
            sortingOrder = state.getIfSuccess()?.sortingOrder?.toDomain() ?: LoanTariffSortingOrder.DESCENDING,
            query = state.getIfSuccess()?.query?.takeIf { it.isNotBlank() },
        ).map { state ->
            state.map { list -> list.map { entity -> mapper.map(entity) } }
        }
    }

    @OptIn(FlowPreview::class)
    private fun subscribeToQueryFlow() {
        viewModelScope.launch {
            queryFlow
                .debounce(1000)
                .distinctUntilChanged()
                .dropFirstBlank()
                .collectLatest { query ->
                    _state.updateIfSuccess { state ->
                        state.copy(query = query)
                    }
                    onPaginationEvent(PaginationEvent.Reload)
                }
        }
    }

    companion object {
        const val PAGE_SIZE = 5
    }
}