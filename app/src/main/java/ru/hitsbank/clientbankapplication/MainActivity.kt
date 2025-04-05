package ru.hitsbank.clientbankapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.Constants.DEEPLINK_APP_SCHEME
import ru.hitsbank.bank_common.Constants.DEEPLINK_AUTH_HOST
import ru.hitsbank.clientbankapplication.core.navigation.RootDestinations
import ru.hitsbank.clientbankapplication.core.navigation.RootNavHost
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.replace
import ru.hitsbank.bank_common.presentation.theme.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                navigationManager.commands.collect { command ->
                    command.execute(navController, this@MainActivity)
                }
            }

            AppTheme {
                CompositionLocalProvider(
                    LocalSnackbarController provides SnackbarController(
                        snackbarHostState = snackbarHostState,
                        coroutineScope = rememberCoroutineScope(),
                    )
                ) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) {
                        RootNavHost(navController)

                        LaunchedEffect(Unit) {
                            handleDeeplink()
                        }
                    }
                }
            }
        }
    }

    private fun handleDeeplink() {
        intent?.data?.let { uri ->
            if (uri.scheme == DEEPLINK_APP_SCHEME && uri.host == DEEPLINK_AUTH_HOST) {
                val code = uri.getQueryParameter("code")
                navigationManager.replace(RootDestinations.Auth.withArgs(code))
            }
        }
    }
}

val LocalSnackbarController =
    compositionLocalOf<SnackbarController> { error("SnackbarController not provided") }

class SnackbarController(
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
) {

    fun show(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }
}
