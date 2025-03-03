package ru.hitsbank.clientbankapplication.login.event

sealed interface LoginEffect {

    data object OnError : LoginEffect
}