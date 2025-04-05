package ru.hitsbank.clientbankapplication.bank_account.presentation.model

data class TransferDetailsScreenState(
    val items: List<TransferDetailsScreenItem>,
    val isPerformingAction: Boolean,
)

data class TransferDetailsScreenItem(
    val subtitle: String,
    val value: String,
    val copyable: Boolean,
)