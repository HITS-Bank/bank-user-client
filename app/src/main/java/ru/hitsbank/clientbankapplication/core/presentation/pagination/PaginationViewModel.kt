package ru.hitsbank.clientbankapplication.core.presentation.pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.hitsbank.clientbankapplication.core.domain.common.State
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.getIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.common.updateIfSuccess

@Suppress("UNCHECKED_CAST")
abstract class PaginationViewModel<T, R: PaginationStateHolder<T>>(initState: BankUiState<R>) : ViewModel() {

    protected val _state = MutableStateFlow(initState)
    val state = _state.asStateFlow()

    private val paginationEvents = MutableSharedFlow<PaginationEvent>()

    init {
        subscribeToPaginationEvents()
    }

    fun onPaginationEvent(event: PaginationEvent) {
        viewModelScope.launch {
            paginationEvents.emit(event)
        }
    }

    private fun subscribeToPaginationEvents() {
        viewModelScope.launch {
            paginationEvents.collectLatest { event ->
                processPaginationEvent(event)
            }
        }
    }

    private suspend fun processPaginationEvent(event: PaginationEvent) {
        when (event) {
            is PaginationEvent.LoadNextPage -> {
                _state.updateIfSuccess(onUpdated = { loadPage(true) }) { state ->
                    state.copyWith(paginationState = PaginationState.Loading) as R
                }
            }

            is PaginationEvent.Reload -> {
                _state.updateIfSuccess(onUpdated = { loadPage(false) }) { state ->
                    state.resetPagination().copyWith(paginationState = PaginationState.Loading) as R
                }
            }
        }
    }

    private suspend fun loadPage(incrementPageNumber: Boolean) {
        val stateValue = state.getIfSuccess() ?: return
        val nextPageNumber = stateValue.pageNumber + if (incrementPageNumber) 1 else 0
        getNextPageContents(nextPageNumber).collect { state ->
            when (state) {
                is State.Error -> _state.updateIfSuccess { oldState ->
                    oldState.copyWith(paginationState = PaginationState.Error) as R
                }
                State.Loading -> Unit
                is State.Success<List<T>> -> _state.updateIfSuccess { oldState ->
                    oldState.copyWith(
                        paginationState =
                            if (state.data.size < oldState.pageSize) PaginationState.EndReached
                            else PaginationState.Idle,
                        data = oldState.data + state.data,
                        pageNumber = nextPageNumber,
                    ) as R
                }
            }
        }
    }

    protected abstract suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<T>>>
}