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
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountListScreenWrapper
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.AccountDetailsViewModel
import ru.hitsbank.clientbankapplication.core.navigation.base.Destination
import ru.hitsbank.clientbankapplication.loan.presentation.compose.LoanCreateScreen
import ru.hitsbank.clientbankapplication.loan.presentation.compose.LoanDetailsScreen
import ru.hitsbank.clientbankapplication.loan.presentation.compose.tariff.TariffsScreen
import ru.hitsbank.clientbankapplication.loan.presentation.viewmodel.LoanDetailsViewModel
import ru.hitsbank.clientbankapplication.login.compose.LoginScreenWrapper
import ru.hitsbank.clientbankapplication.login.viewmodel.LoginViewModel

object RootDestinations {

    object Auth : Destination() {
        const val OPTIONAL_AUTH_CODE_ARG = "authCode"

        fun withArgs(authCode: String?): String {
            return destinationWithArgs(
                args = emptyList(),
                optionalArgs = mapOf(
                    OPTIONAL_AUTH_CODE_ARG to authCode,
                )
            )
        }

        override var optionalArguments = listOf(
            OPTIONAL_AUTH_CODE_ARG,
        )
    }

    object BottomBarRoot : Destination()

    object AccountDetails : Destination() {
        const val OPTIONAL_BANK_ACCOUNT_ENTITY_JSON_ARG = "bankAccountEntity"
        const val OPTIONAL_ACCOUNT_ID_ARG = "accountId"
        const val IS_USER_BLOCKED_ARG = "IS_USER_BLOCKED_ARG"

        fun withArgs(
            bankAccountEntityJson: String?,
            accountId: String?,
            isUserBlocked: Boolean,
        ): String {
            return destinationWithArgs(
                args = listOf(isUserBlocked),
                optionalArgs = mapOf(
                    OPTIONAL_BANK_ACCOUNT_ENTITY_JSON_ARG to bankAccountEntityJson,
                    OPTIONAL_ACCOUNT_ID_ARG to accountId,
                )
            )
        }

        override var arguments = listOf(
            IS_USER_BLOCKED_ARG,
        )

        override var optionalArguments = listOf(
            OPTIONAL_BANK_ACCOUNT_ENTITY_JSON_ARG,
            OPTIONAL_ACCOUNT_ID_ARG
        )
    }

    object LoanDetails : Destination() {
        const val OPTIONAL_LOAN_ENTITY_JSON_ARG = "loanEntity"
        const val OPTIONAL_LOAN_ID_ARG = "loanId"
        const val IS_USER_BLOCKED_ARG = "IS_USER_BLOCKED_ARG"

        fun withArgs(
            loanEntityJson: String?,
            loanId: String?,
            isUserBlocked: Boolean,
        ): String {
            return destinationWithArgs(
                args = listOf(isUserBlocked),
                optionalArgs = mapOf(
                    OPTIONAL_LOAN_ENTITY_JSON_ARG to loanEntityJson,
                    OPTIONAL_LOAN_ID_ARG to loanId,
                )
            )
        }

        override var arguments = listOf(
            IS_USER_BLOCKED_ARG,
        )

        override var optionalArguments = listOf(
            OPTIONAL_LOAN_ENTITY_JSON_ARG,
            OPTIONAL_LOAN_ID_ARG
        )
    }

    object CreateLoan : Destination() {
        const val IS_USER_BLOCKED_ARG = "IS_USER_BLOCKED_ARG"

        override var arguments = listOf(
            IS_USER_BLOCKED_ARG,
        )
    }

    object TariffSelection : Destination()

    object AccountSelection : Destination()
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
        composable(
            route = RootDestinations.Auth.route,
            arguments = listOf(
                navArgument(RootDestinations.Auth.OPTIONAL_AUTH_CODE_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            )
        ) { backStackEntry ->
            val authCode = backStackEntry.arguments?.getString(
                RootDestinations.Auth.OPTIONAL_AUTH_CODE_ARG
            )
            val viewModel: LoginViewModel = koinViewModel(
                parameters = { parametersOf(authCode) },
            )
            LoginScreenWrapper(viewModel)
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
                navArgument(RootDestinations.AccountDetails.OPTIONAL_ACCOUNT_ID_ARG) {
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
                RootDestinations.AccountDetails.OPTIONAL_ACCOUNT_ID_ARG
            )
            val viewModel: AccountDetailsViewModel = koinViewModel(
                parameters = { parametersOf(bankAccountEntityJson, accountNumber, isUserBlocked) },
            )

            AccountDetailsScreenWrapper(viewModel)
        }
        composable(
            route = RootDestinations.CreateLoan.route,
            arguments = listOf(
                navArgument(RootDestinations.CreateLoan.IS_USER_BLOCKED_ARG) {
                    type = NavType.BoolType
                }
            ),
        ) { backStackEntry ->
            val isUserBlocked = backStackEntry.arguments?.getBoolean(
                RootDestinations.CreateLoan.IS_USER_BLOCKED_ARG
            )

            LoanCreateScreen(
                viewModel = koinViewModel(
                    parameters = { parametersOf(isUserBlocked ?: true) }
                )
            )
        }
        composable(
            route = RootDestinations.LoanDetails.route,
            arguments = listOf(
                navArgument(RootDestinations.LoanDetails.IS_USER_BLOCKED_ARG) {
                    type = NavType.BoolType
                },
                navArgument(RootDestinations.LoanDetails.OPTIONAL_LOAN_ENTITY_JSON_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(RootDestinations.LoanDetails.OPTIONAL_LOAN_ID_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) {
            val isUserBlocked = it.arguments?.getBoolean(
                RootDestinations.LoanDetails.IS_USER_BLOCKED_ARG
            )
            val loanEntityJson = it.arguments?.getString(
                RootDestinations.LoanDetails.OPTIONAL_LOAN_ENTITY_JSON_ARG
            )
            val loanId = it.arguments?.getString(
                RootDestinations.LoanDetails.OPTIONAL_LOAN_ID_ARG
            )
            if (isUserBlocked != null) {
                val viewModel: LoanDetailsViewModel = koinViewModel(
                    parameters = { parametersOf(loanId, loanEntityJson, isUserBlocked) }
                )

                LoanDetailsScreen(viewModel)
            } else {
                LaunchedEffect(Unit) {
                    navHostController.popBackStack()
                }
            }
        }
        composable(route = RootDestinations.TariffSelection.route) {
            TariffsScreen()
        }
        composable(route = RootDestinations.AccountSelection.route) {
            AccountListScreenWrapper(
                viewModel = koinViewModel(
                    parameters = { parametersOf(true) }
                )
            )
        }
    }
}