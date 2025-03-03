package ru.hitsbank.clientbankapplication.core.navigation.base

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationCommand

class NavigationManager {

    private val _commands = MutableSharedFlow<NavigationCommand>(replay = 1)
    val commands = _commands.asSharedFlow()

    suspend fun addCommand(command: NavigationCommand) {
        _commands.emit(command)
    }

    fun tryAddCommand(command: NavigationCommand): Boolean {
        return _commands.tryEmit(command)
    }
}

fun NavigationManager.navigate(destination: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    tryAddCommand(NavigationCommand.Navigate(destination, builder))
}

fun NavigationManager.replace(destination: String) {
    tryAddCommand(NavigationCommand.Replace(destination))
}

fun NavigationManager.forward(destination: String) {
    tryAddCommand(NavigationCommand.Forward(destination))
}

fun NavigationManager.forwardWithCallbackResult(destination: String, callback: () -> Unit) {
    tryAddCommand(NavigationCommand.ForwardWithCallback(destination, callback))
}

fun NavigationManager.back() {
    tryAddCommand(NavigationCommand.Back)
}