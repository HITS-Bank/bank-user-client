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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.hitsbank.clientbankapplication.core.navigation.RootNavHost
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager
import ru.hitsbank.clientbankapplication.core.presentation.theme.AppTheme

class MainActivity : ComponentActivity() {

    private val navigationManager by inject<NavigationManager>()

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
                    }
                }
            }
        }
    }
}

val LocalSnackbarController = compositionLocalOf<SnackbarController> { error("SnackbarController not provided") }

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
