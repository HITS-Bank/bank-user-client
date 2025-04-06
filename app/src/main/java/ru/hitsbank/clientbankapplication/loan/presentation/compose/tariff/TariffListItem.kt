package ru.hitsbank.clientbankapplication.loan.presentation.compose.tariff

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.hitsbank.bank_common.presentation.common.component.Divider
import ru.hitsbank.bank_common.presentation.common.component.ListItem
import ru.hitsbank.bank_common.presentation.common.component.ListItemIcon
import ru.hitsbank.clientbankapplication.loan.presentation.event.tariff.TariffsScreenEvent
import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.TariffModel

@Composable
fun TariffListItem(item: TariffModel, onEvent: (TariffsScreenEvent) -> Unit) {
    ListItem(
        modifier = Modifier.clickable { onEvent(TariffsScreenEvent.TariffSelected(item)) },
        icon = ListItemIcon.None,
        title = item.name,
        subtitle = item.interestRate,
        divider = Divider.Default(padding = PaddingValues(horizontal = 16.dp)),
        padding = PaddingValues(vertical = 12.dp),
    )
}