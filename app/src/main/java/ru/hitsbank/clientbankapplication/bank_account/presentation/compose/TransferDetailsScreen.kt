package ru.hitsbank.clientbankapplication.bank_account.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.hitsbank.bank_common.presentation.common.component.BankButton
import ru.hitsbank.bank_common.presentation.common.component.Divider
import ru.hitsbank.bank_common.presentation.common.component.ListItem
import ru.hitsbank.bank_common.presentation.common.component.ListItemIcon
import ru.hitsbank.bank_common.presentation.common.component.LoadingContentOverlay
import ru.hitsbank.bank_common.presentation.common.rememberCallback
import ru.hitsbank.bank_common.presentation.common.verticalSpacer
import ru.hitsbank.bank_common.presentation.theme.S22_W400
import ru.hitsbank.bank_common.presentation.theme.S24_W600
import ru.hitsbank.clientbankapplication.R
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.TransferDetailsEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel.TransferDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferDetailsScreen(viewModel: TransferDetailsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Подтверждение перевода",
                        style = S22_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable(
                            onClick = {
                                onEvent.invoke(TransferDetailsEvent.OnBackClick)
                            }
                        ),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back_48),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        },
        floatingActionButton = {
            BankButton.Outlined(
                text = "Подтвердить перевод",
                onClick = { onEvent.invoke(TransferDetailsEvent.Transfer) },
                icon = ImageVector.vectorResource(id = R.drawable.ic_check_18),
                borderColor = MaterialTheme.colorScheme.outline,
            )
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings),
        ) {
            item {
                16.dp.verticalSpacer()
                Text(
                    text = "Детальная информация",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = S24_W600,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                16.dp.verticalSpacer()
            }
            items(state.items) { item ->
                ListItem(
                    icon = ListItemIcon.None,
                    title = item.value,
                    subtitle = item.subtitle,
                    divider = Divider.None,
                    isTitleCopyable = item.copyable,
                )
            }
        }
    }

    if (state.isPerformingAction) {
        LoadingContentOverlay()
    }
}