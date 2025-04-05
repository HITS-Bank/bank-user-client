package ru.hitsbank.clientbankapplication.bank_account.presentation.model

import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.presentation.common.component.dropdown.DropdownItem
import ru.hitsbank.bank_common.presentation.common.toFullName

data class CurrencyCodeDropdownItem(
    val currencyCode: CurrencyCode,
) : DropdownItem {

    override val title: String = currencyCode.toFullName()
}
