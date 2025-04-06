package ru.hitsbank.clientbankapplication.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.hitsbank.bank_common.di.AuthRetrofit
import ru.hitsbank.clientbankapplication.bank_account.data.api.BankAccountApi
import ru.hitsbank.clientbankapplication.bank_account.data.repository.BankAccountRepository
import ru.hitsbank.clientbankapplication.bank_account.domain.repository.IBankAccountRepository
import ru.hitsbank.clientbankapplication.loan.data.api.LoanApi
import ru.hitsbank.clientbankapplication.loan.data.repository.LoanRepository
import ru.hitsbank.clientbankapplication.loan.domain.repository.ILoanRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BankClientModule {

    companion object {

        @Provides
        @Singleton
        fun provideBankAccountApi(@AuthRetrofit retrofit: Retrofit): BankAccountApi {
            return retrofit.create(BankAccountApi::class.java)
        }

        @Provides
        @Singleton
        fun provideLoanApi(@AuthRetrofit retrofit: Retrofit): LoanApi {
            return retrofit.create(LoanApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindBankAccountRepository(impl: BankAccountRepository): IBankAccountRepository

    @Binds
    @Singleton
    abstract fun bindLoanRepository(impl: LoanRepository): ILoanRepository
}