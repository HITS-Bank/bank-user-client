package ru.hitsbank.clientbankapplication.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ru.hitsbank.clientbankapplication.loan.presentation.compose.LoanDetailsScreen
import ru.hitsbank.clientbankapplication.loan.presentation.viewmodel.LoanDetailsViewModel
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

    object LoanDetails : Destination() {
        const val OPTIONAL_LOAN_ENTITY_JSON_ARG = "loanEntity"
        const val OPTIONAL_LOAN_NUMBER_ARG = "loanId"
        const val IS_USER_BLOCKED_ARG = "IS_USER_BLOCKED_ARG"

        fun withArgs(
            loanEntityJson: String?,
            loanNumber: String?,
            isUserBlocked: Boolean,
        ): String {
            return destinationWithArgs(
                args = listOf(isUserBlocked),
                optionalArgs = mapOf(
                    OPTIONAL_LOAN_ENTITY_JSON_ARG to loanEntityJson,
                    OPTIONAL_LOAN_NUMBER_ARG to loanNumber,
                )
            )
        }

        override var arguments = listOf(
            IS_USER_BLOCKED_ARG,
        )

        override var optionalArguments = listOf(
            OPTIONAL_LOAN_ENTITY_JSON_ARG,
            OPTIONAL_LOAN_NUMBER_ARG
        )
    }

    object CreateLoan : Destination()
}


@Composable
fun RootNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = RootDestinations.BottomBarRoot.route,
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
        composable(route = RootDestinations.CreateLoan.route) {

        }
        composable(route = RootDestinations.LoanDetails.route,
            arguments = listOf(
                navArgument(RootDestinations.LoanDetails.IS_USER_BLOCKED_ARG) {
                    type = NavType.BoolType
                },
                navArgument(RootDestinations.LoanDetails.OPTIONAL_LOAN_ENTITY_JSON_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(RootDestinations.LoanDetails.OPTIONAL_LOAN_NUMBER_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            )
        ) {
            val isUserBlocked = it.arguments?.getBoolean(
                RootDestinations.LoanDetails.IS_USER_BLOCKED_ARG
            )
            val loanEntityJson = it.arguments?.getString(
                RootDestinations.LoanDetails.OPTIONAL_LOAN_ENTITY_JSON_ARG
            )
            val loanNumber = it.arguments?.getString(
                RootDestinations.LoanDetails.OPTIONAL_LOAN_NUMBER_ARG
            )
            if (isUserBlocked != null) {
                val viewModel: LoanDetailsViewModel = koinViewModel(
                    parameters = { parametersOf(loanNumber, loanEntityJson, isUserBlocked) }
                )

                LoanDetailsScreen(viewModel)
            } else {
                LaunchedEffect(Unit) {
                    navHostController.popBackStack()
                }
            }
        }
    }
}