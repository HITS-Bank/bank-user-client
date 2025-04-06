package ru.hitsbank.clientbankapplication.bank_account.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.hitsbank.bank_common.domain.Completable
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.domain.toState
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferConfirmation
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferInfo
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.repository.IBankAccountRepository
import javax.inject.Inject

class BankAccountInteractor @Inject constructor(
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
        accountId: String,
        pageSize: Int,
        pageNumber: Int,
    ): Flow<State<List<OperationEntity>>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getOperationHistory(accountId, pageSize, pageNumber).toState())
    }

    fun getTransferInfo(transferRequest: TransferRequest): Flow<State<TransferInfo>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getTransferInfo(transferRequest).toState())
    }

    fun transfer(transferConfirmation: TransferConfirmation): Flow<State<BankAccountEntity>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.transfer(transferConfirmation).toState())
    }

    fun getHiddenAccountIds(): Flow<State<List<String>>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.getHiddenAccountIds().toState())
    }

    fun hideAccount(accountId: String): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.hideAccount(accountId).toState())
    }

    fun unhideAccount(accountId: String): Flow<State<Completable>> = flow {
        emit(State.Loading)
        emit(bankAccountRepository.unhideAccount(accountId).toState())
    }
}