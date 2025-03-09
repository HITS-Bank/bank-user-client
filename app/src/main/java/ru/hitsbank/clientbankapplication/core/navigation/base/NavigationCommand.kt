package ru.hitsbank.clientbankapplication.core.navigation.base

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.google.gson.Gson

sealed interface NavigationCommand {

    fun execute(navController: NavController, activity: ComponentActivity)

    class Navigate(
        private val destination: String,
        private val builder: NavOptionsBuilder.() -> Unit = {},
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigate(destination, builder)
        }
    }

    class Replace(
        private val destination: String,
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigate(destination) {
                popUpTo(
                    navController.currentBackStackEntry?.destination?.route ?: return@navigate
                ) {
                    inclusive = true
                }
            }
        }
    }

    class Forward(
        private val destination: String,
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigate(destination)
        }
    }

    class ForwardWithCallback(
        private val destination: String,
        private val callback: () -> Unit,
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigate(destination)
            val backstackEntry = navController.getBackStackEntry(destination)

            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    callback()
                }
            }
            backstackEntry.lifecycle.addObserver(observer)
        }
    }

    class ForwardWithJsonResult<T>(
        private val gson: Gson,
        private val destination: String,
        private val type: Class<T>,
        private val callback: (T?) -> Unit,
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigate(destination)
            val backstackEntry = navController.getBackStackEntry(destination)

            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    val json = backstackEntry.savedStateHandle.get<String>(JSON_RESULT_KEY)
                    val result = gson.fromJson(json, type)
                    callback(result)
                }
            }
            backstackEntry.lifecycle.addObserver(observer)
        }
    }

    class BackWithJsonResult<T>(
        private val gson: Gson,
        private val resultData: T,
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.currentBackStackEntry?.savedStateHandle?.set(JSON_RESULT_KEY, gson.toJson(resultData))
            navController.navigateUp().also { navigated ->
                if (!navigated) {
                    activity.finish()
                }
            }
        }
    }

    object Back : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigateUp().also { navigated ->
                if (!navigated) {
                    activity.finish()
                }
            }
        }
    }

    companion object {
        const val JSON_RESULT_KEY = "json"
    }
}