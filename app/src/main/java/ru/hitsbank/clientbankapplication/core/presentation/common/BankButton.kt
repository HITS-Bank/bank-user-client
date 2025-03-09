package ru.hitsbank.clientbankapplication.core.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ru.hitsbank.clientbankapplication.core.presentation.theme.S14_W500

object BankButton {

    @Composable
    fun Regular(
        text: String,
        onClick: () -> Unit,
        colors: ButtonColors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
        ),
        icon: ImageVector? = null,
        enabled: Boolean = true,
        modifier: Modifier = Modifier,
    ) {
        ButtonBase(
            text = text,
            onClick = onClick,
            icon = icon,
            enabled = enabled,
            colors = colors,
            modifier = modifier.height(40.dp),
        )
    }

    @Composable
    fun Outlined(
        text: String,
        onClick: () -> Unit,
        icon: ImageVector? = null,
        enabled: Boolean = true,
        colors: ButtonColors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        borderColor: Color? = null,
        modifier: Modifier = Modifier,
    ) {
        OutlinedButtonBase(
            text = text,
            onClick = onClick,
            icon = icon,
            enabled = enabled,
            colors = colors,
            borderColor = borderColor,
            modifier = modifier.height(40.dp),
        )
    }

    @Composable
    private fun ButtonBase(
        text: String,
        onClick: () -> Unit,
        colors: ButtonColors,
        icon: ImageVector? = null,
        enabled: Boolean = true,
        modifier: Modifier = Modifier,
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            colors = colors,
            enabled = enabled,
        ) {
            icon?.let { icon ->
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = icon,
                    contentDescription = null,
                )
                8.dp.horizontalSpacer()
            }
            Text(
                text = text,
                style = S14_W500,
            )
        }
    }

    @Composable
    private fun OutlinedButtonBase(
        text: String,
        onClick: () -> Unit,
        colors: ButtonColors,
        icon: ImageVector? = null,
        enabled: Boolean = true,
        borderColor: Color? = null,
        modifier: Modifier = Modifier,
    ) {
        OutlinedButton(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            colors = colors,
            enabled = enabled,
            border = borderColor?.let {
                color -> BorderStroke(width = 1.dp, color = color)
            } ?: ButtonDefaults.outlinedButtonBorder,
        ) {
            icon?.let { icon ->
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = icon,
                    contentDescription = null,
                )
                8.dp.horizontalSpacer()
            }
            Text(
                text = text,
                style = S14_W500,
            )
        }
    }
}