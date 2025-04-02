package ru.hitsbank.clientbankapplication.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.hitsbank.clientbankapplication.bank_account.data.api.BankAccountApi
import ru.hitsbank.clientbankapplication.bank_account.data.repository.BankAccountRepository
import ru.hitsbank.clientbankapplication.bank_account.domain.repository.IBankAccountRepository
import ru.hitsbank.clientbankapplication.core.constants.Constants.KEYCLOAK_BASE_URL
import ru.hitsbank.clientbankapplication.core.data.api.AuthApi
import ru.hitsbank.clientbankapplication.core.data.api.ProfileApi
import ru.hitsbank.clientbankapplication.core.data.interceptor.AuthInterceptor
import ru.hitsbank.clientbankapplication.core.data.repository.AuthRepository
import ru.hitsbank.clientbankapplication.core.data.repository.ProfileRepository
import ru.hitsbank.clientbankapplication.core.data.serialization.LocalDateTimeDeserializer
import ru.hitsbank.clientbankapplication.core.data.serialization.LocalDateTimeSerializer
import ru.hitsbank.clientbankapplication.core.domain.repository.IAuthRepository
import ru.hitsbank.clientbankapplication.core.domain.repository.IProfileRepository
import ru.hitsbank.clientbankapplication.di.qualifiers.AuthOkHttpClient
import ru.hitsbank.clientbankapplication.di.qualifiers.AuthRetrofit
import ru.hitsbank.clientbankapplication.di.qualifiers.NoAuthOkHttpClient
import ru.hitsbank.clientbankapplication.di.qualifiers.NoAuthRetrofit
import ru.hitsbank.clientbankapplication.loan.data.api.LoanApi
import ru.hitsbank.clientbankapplication.loan.data.repository.LoanRepository
import ru.hitsbank.clientbankapplication.loan.domain.repository.ILoanRepository
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val TIMEOUT_SEC = 20L

private const val BASE_URL = "http://10.0.2.2:9446/"

@Module
@InstallIn(SingletonComponent::class)
abstract class BankClientModule {

    companion object {

        @Provides
        @Singleton
        fun provideAndroidContext(@ApplicationContext context: Context): Context {
            return context
        }

        @Provides
        @Singleton
        fun provideLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        }

        @Provides
        @Singleton
        fun provideGson(): Gson {
            return GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer)
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer).create()
        }

        fun provideOkHttpClient(
            vararg interceptors: Interceptor,
        ) = OkHttpClient.Builder().apply {
            interceptors.forEach { addInterceptor(it) }
            connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
        }.build()

        @NoAuthOkHttpClient
        @Provides
        @Singleton
        fun provideNoAuthOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
        ): OkHttpClient {
            return provideOkHttpClient(loggingInterceptor)
        }

        @AuthOkHttpClient
        @Provides
        @Singleton
        fun provideAuthOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            authInterceptor: AuthInterceptor,
        ): OkHttpClient {
            return provideOkHttpClient(
                loggingInterceptor,
                authInterceptor,
            )
        }

        @NoAuthRetrofit
        @Provides
        @Singleton
        fun provideNoAuthRetrofit(
            @NoAuthOkHttpClient okHttpClient: OkHttpClient,
            gson: Gson,
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(KEYCLOAK_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        @AuthRetrofit
        @Provides
        @Singleton
        fun provideAuthRetrofit(
            @AuthOkHttpClient okHttpClient: OkHttpClient,
            gson: Gson,
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        @Provides
        @Singleton
        fun provideAuthApi(@NoAuthRetrofit retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }

        @Provides
        @Singleton
        fun provideProfileApi(@AuthRetrofit retrofit: Retrofit): ProfileApi {
            return retrofit.create(ProfileApi::class.java)
        }

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
    abstract fun bindAuthRepository(impl: AuthRepository): IAuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepository): IProfileRepository

    @Binds
    @Singleton
    abstract fun bindLoanRepository(impl: LoanRepository): ILoanRepository
}