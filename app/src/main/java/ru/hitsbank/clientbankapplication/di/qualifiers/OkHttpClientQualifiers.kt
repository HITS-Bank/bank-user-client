package ru.hitsbank.clientbankapplication.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuthOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient