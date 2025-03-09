package ru.hitsbank.clientbankapplication.bank_account.data.model

import com.google.gson.annotations.SerializedName
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.core.domain.model.PageInfo

data class AccountListResponse(
    @SerializedName("accounts")
    val bankAccounts: List<BankAccountResponse>,
    val pageInfo: PageInfo,
)