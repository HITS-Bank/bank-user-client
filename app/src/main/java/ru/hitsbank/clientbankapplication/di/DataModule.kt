package ru.hitsbank.clientbankapplication.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.hitsbank.clientbankapplication.bank_account.data.mapper.BankAccountMapper
import ru.hitsbank.clientbankapplication.bank_account.data.repository.BankAccountRepository
import ru.hitsbank.clientbankapplication.bank_account.domain.repository.IBankAccountRepository
import ru.hitsbank.clientbankapplication.core.data.datasource.SessionManager
import ru.hitsbank.clientbankapplication.core.data.mapper.AuthMapper
import ru.hitsbank.clientbankapplication.core.data.mapper.ProfileMapper
import ru.hitsbank.clientbankapplication.core.data.repository.AuthRepository
import ru.hitsbank.clientbankapplication.core.data.repository.ProfileRepository
import ru.hitsbank.clientbankapplication.core.domain.repository.IAuthRepository
import ru.hitsbank.clientbankapplication.core.domain.repository.IProfileRepository

fun dataModule() = module {
    singleOf(::AuthMapper)
    singleOf(::ProfileMapper)
    singleOf(::BankAccountMapper)
    singleOf(::AuthRepository) bind IAuthRepository::class
    singleOf(::ProfileRepository) bind IProfileRepository::class
    singleOf(::BankAccountRepository) bind IBankAccountRepository::class
    singleOf(::SessionManager)
}