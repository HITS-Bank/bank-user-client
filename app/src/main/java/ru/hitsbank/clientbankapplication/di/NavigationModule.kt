package ru.hitsbank.clientbankapplication.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager

fun navigationModule() = module {
    singleOf(::NavigationManager)
}