package ru.hitsbank.clientbankapplication.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.hitsbank.bank_common.domain.entity.RoleType
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountListScreenWrapper
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountListViewModel
import ru.hitsbank.bank_common.presentation.navigation.BottomBarDestination
import ru.hitsbank.bank_common.presentation.theme.settings.compose.ThemeSettingsScreen
import ru.hitsbank.bank_common.presentation.theme.settings.viewmodel.ThemeSettingsViewModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountListMode
import ru.hitsbank.clientbankapplication.loan.presentation.compose.LoanListScreen

object BottomBarDestinations {

    object Accounts : BottomBarDestination() {
        override val icon = R.drawable.ic_briefcase_24
        override val title = "Счета"
    }

    object Tariffs : BottomBarDestination() {
        override val icon = R.drawable.ic_credit_24
        override val title = "Кредиты"
    }

    object Personalization : BottomBarDestination() {
        override val icon = R.drawable.ic_personalization
        override val title = "Персонализация"
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
                val viewModel: AccountListViewModel = hiltViewModel<AccountListViewModel, AccountListViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(
                            accountListMode = AccountListMode.DEFAULT,
                        )
                    }
                )
                AccountListScreenWrapper(viewModel)
            }
            composable(route = BottomBarDestinations.Tariffs.route) {
                LoanListScreen()
            }
            composable(route = BottomBarDestinations.Personalization.route) {
                val viewModel = hiltViewModel<ThemeSettingsViewModel, ThemeSettingsViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(RoleType.CLIENT)
                    }
                )
                ThemeSettingsScreen(viewModel)
            }
        }
    }
}