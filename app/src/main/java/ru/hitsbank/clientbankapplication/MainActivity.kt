package ru.hitsbank.clientbankapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.hitsbank.clientbankapplication.core.presentation.theme.AppTheme
import ru.hitsbank.clientbankapplication.login.compose.LoginScreenWrapper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                LoginScreenWrapper()
            }
        }
    }
}
