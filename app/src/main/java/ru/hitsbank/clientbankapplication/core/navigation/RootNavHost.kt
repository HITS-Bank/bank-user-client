package ru.hitsbank.clientbankapplication.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountDetailsScreenWrapper
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountDetailsViewModel
import ru.hitsbank.clientbankapplication.core.navigation.base.Destination
import ru.hitsbank.clientbankapplication.login.compose.LoginScreenWrapper

object RootDestinations {

    object Auth : Destination()

    object BottomBarRoot : Destination()

    object AccountDetails : Destination() {
        const val OPTIONAL_BANK_ACCOUNT_ENTITY_JSON_ARG = "bankAccountEntity"
        const val OPTIONAL_ACCOUNT_NUMBER_ARG = "accountNumber"
        const val IS_USER_BLOCKED_ARG = "IS_USER_BLOCKED_ARG"

        fun withArgs(
            bankAccountEntityJson: String?,
            accountNumber: String?,
            isUserBlocked: Boolean,
        ): String {
            return destinationWithArgs(
                args = listOf(isUserBlocked),
                optionalArgs = mapOf(
                    OPTIONAL_BANK_ACCOUNT_ENTITY_JSON_ARG to bankAccountEntityJson,
                    OPTIONAL_ACCOUNT_NUMBER_ARG to accountNumber,
                )
            )
        }

        override var arguments = listOf(
            IS_USER_BLOCKED_ARG,
        )

        override var optionalArguments = listOf(
            OPTIONAL_BANK_ACCOUNT_ENTITY_JSON_ARG,
            OPTIONAL_ACCOUNT_NUMBER_ARG
        )
    }
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
        composable(
            route = RootDestinations.AccountDetails.route,
            arguments = listOf(
                navArgument(RootDestinations.AccountDetails.IS_USER_BLOCKED_ARG) {
                    type = NavType.BoolType
                },
                navArgument(RootDestinations.AccountDetails.OPTIONAL_BANK_ACCOUNT_ENTITY_JSON_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(RootDestinations.AccountDetails.OPTIONAL_ACCOUNT_NUMBER_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { backStackEntry ->
            val isUserBlocked = backStackEntry.arguments?.getBoolean(
                RootDestinations.AccountDetails.IS_USER_BLOCKED_ARG
            )
            val bankAccountEntityJson = backStackEntry.arguments?.getString(
                RootDestinations.AccountDetails.OPTIONAL_BANK_ACCOUNT_ENTITY_JSON_ARG
            )
            val accountNumber = backStackEntry.arguments?.getString(
                RootDestinations.AccountDetails.OPTIONAL_ACCOUNT_NUMBER_ARG
            )
            val viewModel: AccountDetailsViewModel = koinViewModel(
                parameters = { parametersOf(bankAccountEntityJson, accountNumber, isUserBlocked) },
            )

            AccountDetailsScreenWrapper(viewModel)
        }
    }
}