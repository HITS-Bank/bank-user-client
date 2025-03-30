package ru.hitsbank.clientbankapplication.loan.presentation.compose.tariff

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.core.presentation.common.ErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.LoadingContent
import ru.hitsbank.clientbankapplication.core.presentation.common.PaginationErrorContent
import ru.hitsbank.clientbankapplication.core.presentation.common.PaginationLoadingContent
import ru.hitsbank.clientbankapplication.core.presentation.common.SearchTextField
import ru.hitsbank.clientbankapplication.core.presentation.common.dropdown.DropdownField
import ru.hitsbank.clientbankapplication.core.presentation.common.getIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.common.horizontalSpacer
import ru.hitsbank.clientbankapplication.core.presentation.common.rememberCallback
import ru.hitsbank.clientbankapplication.core.presentation.common.verticalSpacer
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationEvent
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationReloadState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.reloadState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.rememberPaginationListState
import ru.hitsbank.clientbankapplication.core.presentation.theme.S22_W400
import ru.hitsbank.clientbankapplication.loan.presentation.event.tariff.TariffsScreenEvent
import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.SortingOrder
import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.SortingProperty
import ru.hitsbank.clientbankapplication.loan.presentation.viewmodel.TariffsScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TariffsScreen(viewModel: TariffsScreenViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)
    val onPaginationEvent = rememberCallback(viewModel::onPaginationEvent)
    val listState = rememberPaginationListState(viewModel)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Выбор тарифа",
                        style = S22_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable(
                            onClick = {
                                onEvent.invoke(TariffsScreenEvent.Back)
                            }
                        ),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back_48),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            SearchTextField(
                text = state.getIfSuccess()?.queryDisplayText ?: "",
                onTextChanged = { onEvent(TariffsScreenEvent.QueryChanged(it)) },
                placeholder = "Название тарифа",
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                DropdownField(
                    items = SortingProperty.entries,
                    selectedItem = state.getIfSuccess()?.sortingProperty ?: SortingProperty.NAME,
                    onItemSelected = { onEvent(TariffsScreenEvent.SortingPropertyChanged(it)) },
                    isDropdownOpen = state.getIfSuccess()?.isSortingPropertyMenuOpen ?: false,
                    onOpenDropdown = { onEvent(TariffsScreenEvent.OpenSortingPropertyMenu) },
                    onCloseDropdown = { onEvent(TariffsScreenEvent.CloseSortingPropertyMenu) },
                    label = "Сортировать по",
                    modifier = Modifier.weight(1f),
                )
                16.dp.horizontalSpacer()
                DropdownField(
                    items = SortingOrder.entries,
                    selectedItem = state.getIfSuccess()?.sortingOrder ?: SortingOrder.DESCENDING,
                    onItemSelected = { onEvent(TariffsScreenEvent.SortingOrderChanged(it)) },
                    isDropdownOpen = state.getIfSuccess()?.isSortingOrderMenuOpen ?: false,
                    onOpenDropdown = { onEvent(TariffsScreenEvent.OpenSortingOrderMenu) },
                    onCloseDropdown = { onEvent(TariffsScreenEvent.CloseSortingOrderMenu) },
                    label = "Порядок",
                    modifier = Modifier.weight(1f),
                )
            }
            16.dp.verticalSpacer()
            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = state.getIfSuccess()?.reloadState,
            ) { reloadState ->
                when (reloadState) {
                    PaginationReloadState.Idle -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                        ) {
                            state.getIfSuccess()?.data?.let { data ->
                                items(data, key = { item -> item.id }) { item ->
                                    TariffListItem(item, onEvent)
                                }
                            }

                            when (state.getIfSuccess()?.paginationState) {
                                PaginationState.Loading -> item {
                                    PaginationLoadingContent()
                                }

                                PaginationState.Error -> item {
                                    PaginationErrorContent {
                                        onPaginationEvent(PaginationEvent.LoadNextPage)
                                    }
                                }

                                else -> Unit
                            }
                        }
                    }

                    PaginationReloadState.Reloading -> LoadingContent()

                    PaginationReloadState.Error -> ErrorContent(
                        onReload = {
                            onPaginationEvent(PaginationEvent.Reload)
                        },
                        onBack = {
                            onEvent(TariffsScreenEvent.Back)
                        }
                    )

                    else -> Unit
                }
            }
        }
    }
}