package ru.hitsbank.clientbankapplication.bank_account.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.hitsbank.clientbankapplication.bank_account.data.model.CloseAccountRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.repository.IBankAccountRepository
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountNumberRequest
import ru.hitsbank.clientbankapplication.core.data.common.toState
import ru.hitsbank.clientbankapplication.core.domain.common.Completable
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

    fun topUp(topUpRequest: TopUpRequest): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.topUp(topUpRequest).toState())
    }

    fun withdraw(withdrawRequest: WithdrawRequest): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.withdraw(withdrawRequest).toState())
    }

    fun closeAccount(closeAccountRequest: CloseAccountRequest): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.closeAccount(closeAccountRequest).toState())
    }

    fun getBankAccountByNumber(accountNumberRequest: AccountNumberRequest): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getBankAccountByNumber(accountNumberRequest).toState())
    }

    fun getOperationHistory(
        accountNumberRequest: AccountNumberRequest,
        pageSize: Int,
        pageNumber: Int,
    ): Flow<State<List<OperationEntity>>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getOperationHistory(accountNumberRequest, pageSize, pageNumber).toState())
    }
}