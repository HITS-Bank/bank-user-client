package ru.hitsbank.clientbankapplication.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountListScreenWrapper
import ru.hitsbank.clientbankapplication.core.navigation.base.BottomBarDestination

object BottomBarDestinations {

    object Accounts : BottomBarDestination() {
        override val icon = R.drawable.ic_briefcase_24
        override val title = "Счета"
    }

    object Tariffs : BottomBarDestination() {
        override val icon = R.drawable.ic_credit_24
        override val title = "Кредиты"
    }
}

@Composable
fun BottomBarNavHost() {
    val navController = rememberNavController()
    val selectedItem by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = BottomBarDestinations.Accounts.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(BottomBarDestinations.Accounts.title) },
                    selected = selectedItem?.destination?.route == BottomBarDestinations.Accounts.route,
                    onClick = { navController.navigate(BottomBarDestinations.Accounts.destination) }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = BottomBarDestinations.Tariffs.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(BottomBarDestinations.Tariffs.title) },
                    selected = selectedItem?.destination?.route == BottomBarDestinations.Tariffs.route,
                    onClick = { navController.navigate(BottomBarDestinations.Tariffs.destination) }
                )
            }
        },
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = BottomBarDestinations.Accounts.route,
        ) {
            composable(route = BottomBarDestinations.Accounts.route) {
                AccountListScreenWrapper()
            }
            composable(route = BottomBarDestinations.Tariffs.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Кредиты")
                }
            }
        }
    }
}