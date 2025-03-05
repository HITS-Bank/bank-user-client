package ru.hitsbank.clientbankapplication.bank_account.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.repository.IBankAccountRepository
import ru.hitsbank.clientbankapplication.core.data.common.toState
import ru.hitsbank.clientbankapplication.core.domain.common.State

class BankAccountInteractor(
    private val bankAccountRepository: IBankAccountRepository,
) {

    fun getAccountList(
        pageSize: Int,
        pageNumber: Int,
    ): Flow<State<AccountListEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getAccountList(pageSize, pageNumber).toState())
    }

    fun createAccount(): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.createAccount().toState())
    }
}