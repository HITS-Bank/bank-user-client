package ru.hitsbank.clientbankapplication.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.hitsbank.clientbankapplication.core.navigation.base.Destination
import ru.hitsbank.clientbankapplication.login.compose.LoginScreenWrapper

object RootDestinations {

    object Auth : Destination()

    object BottomBarRoot : Destination()
}



@Composable
fun RootNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = RootDestinations.Auth.route,
        modifier = modifier,
    ) {
        composable(route = RootDestinations.Auth.route) {
            LoginScreenWrapper()
        }
        composable(route = RootDestinations.BottomBarRoot.route) {
            BottomBarNavHost()
        }
    }
}