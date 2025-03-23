package ru.hitsbank.clientbankapplication.bank_account.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.repository.IBankAccountRepository
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountNumberRequest
import ru.hitsbank.clientbankapplication.core.data.common.toState
import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode
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

    fun createAccount(currencyCode: CurrencyCode): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.createAccount(currencyCode).toState())
    }

    fun topUp(
        accountId: String,
        topUpRequest: TopUpRequest
    ): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.topUp(accountId, topUpRequest).toState())
    }

    fun withdraw(
        accountId: String,
        withdrawRequest: WithdrawRequest
    ): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.withdraw(accountId, withdrawRequest).toState())
    }

    fun closeAccount(accountId: String): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.closeAccount(accountId).toState())
    }

    fun getBankAccountById(accountId: String): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getBankAccountById(accountId).toState())
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