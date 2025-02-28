package ru.hitsbank.clientbankapplication.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.hitsbank.clientbankapplication.login.mapper.LoginScreenModelMapper
import ru.hitsbank.clientbankapplication.login.viewmodel.LoginViewModel

fun presentationModule() = module {
    singleOf(::LoginScreenModelMapper)

    viewModelOf(::LoginViewModel)
}