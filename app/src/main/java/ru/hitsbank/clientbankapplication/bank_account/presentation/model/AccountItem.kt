package ru.hitsbank.clientbankapplication.bank_account.presentation.model

data class AccountItem(
    val id: String,
    val number: String,
    val isHidden: Boolean,
    val description: String,
    val descriptionColorId: Int,
)
