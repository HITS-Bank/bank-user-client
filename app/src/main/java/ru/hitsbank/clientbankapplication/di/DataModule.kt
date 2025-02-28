package ru.hitsbank.clientbankapplication.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.hitsbank.clientbankapplication.core.data.datasource.SessionManager
import ru.hitsbank.clientbankapplication.core.data.mapper.AuthMapper
import ru.hitsbank.clientbankapplication.core.data.repository.AuthRepository
import ru.hitsbank.clientbankapplication.core.domain.repository.IAuthRepository

fun dataModule() = module {
    singleOf(::AuthMapper)
    singleOf(::AuthRepository) bind IAuthRepository::class
    singleOf(::SessionManager)
}