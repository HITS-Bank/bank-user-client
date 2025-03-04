package ru.hitsbank.clientbankapplication.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.hitsbank.clientbankapplication.bank_account.domain.interactor.BankAccountInteractor
import ru.hitsbank.clientbankapplication.core.domain.interactor.AuthInteractor

fun domainModule() = module {
    singleOf(::AuthInteractor)
    singleOf(::BankAccountInteractor)
}