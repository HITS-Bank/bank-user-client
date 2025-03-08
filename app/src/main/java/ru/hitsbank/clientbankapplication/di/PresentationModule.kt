package ru.hitsbank.clientbankapplication.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.hitsbank.clientbankapplication.bank_account.presentation.mapper.AccountDetailsMapper
import ru.hitsbank.clientbankapplication.bank_account.presentation.mapper.AccountListMapper
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountItem
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountDetailsViewModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountListPaginationState
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountListViewModel
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationViewModel
import ru.hitsbank.clientbankapplication.loan.presentation.mapper.LoanDetailsMapper
import ru.hitsbank.clientbankapplication.loan.presentation.mapper.LoanListMapper
import ru.hitsbank.clientbankapplication.loan.presentation.viewmodel.LoanDetailsViewModel
import ru.hitsbank.clientbankapplication.loan.presentation.viewmodel.LoanListViewModel
import ru.hitsbank.clientbankapplication.login.mapper.LoginScreenModelMapper
import ru.hitsbank.clientbankapplication.login.viewmodel.LoginViewModel

fun presentationModule() = module {
    singleOf(::LoginScreenModelMapper)
    singleOf(::AccountListMapper)
    singleOf(::AccountDetailsMapper)
    singleOf(::LoanListMapper)
    singleOf(::LoanDetailsMapper)

    viewModelOf(::LoginViewModel)
    viewModelOf(::AccountListViewModel) { bind<PaginationViewModel<AccountItem, AccountListPaginationState>>() }
    viewModelOf(::LoanListViewModel)
    viewModel<AccountDetailsViewModel> { parameters ->
        AccountDetailsViewModel(
            bankAccountEntityJson = parameters[0],
            accountNumber = parameters[1],
            isUserBlocked = parameters[2],
            get(), get(), get(), get(),
        )
    }
    viewModel { parameters ->
        LoanDetailsViewModel(
            loanNumber = parameters[0],
            loanEntityJson = parameters[1],
            isUserBlocked = parameters[2],
            get(), get(), get(), get(),
        )
    }
}