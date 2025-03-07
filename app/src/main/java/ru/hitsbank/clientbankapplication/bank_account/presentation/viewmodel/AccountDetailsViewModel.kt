package ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountDetailsEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.mapper.AccountDetailsMapper
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsScreenModel
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager
import ru.hitsbank.clientbankapplication.core.navigation.base.back
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState

class AccountDetailsViewModel(
    private val bankAccountEntityJson: String?,
    private val isUserBlocked: Boolean, // TODO
    private val gson: Gson,
    private val accountDetailsMapper: AccountDetailsMapper,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    private val _uiState: MutableStateFlow<BankUiState<AccountDetailsScreenModel>> =
        MutableStateFlow(BankUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val screenModel = getAccountDetailsModel()
        if (screenModel == null) {
            _uiState.update { BankUiState.Error() }
        } else {
            _uiState.update { BankUiState.Ready(screenModel) }
        }
    }

    fun onEvent(event: AccountDetailsEvent) {
        when (event) {
            AccountDetailsEvent.OnBack -> onBack()
        }
    }

    private fun getAccountDetailsModel(): AccountDetailsScreenModel? {
        val bankAccountEntity = bankAccountEntityJson
            ?.let { json ->
                gson.fromJson(json, BankAccountEntity::class.java)
            }
            ?: return null

        return accountDetailsMapper.map(
            bankAccountEntity = bankAccountEntity,
            operationsHistoryEntity = listOf(),
        ) // TODO operationsHistory
    }

    private fun onBack() {
        // TODO backWithResult или navigateWithCallback, при возврате надо обновлять список
        navigationManager.back()
    }
}