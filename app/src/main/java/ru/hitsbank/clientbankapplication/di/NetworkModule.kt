package ru.hitsbank.clientbankapplication.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.hitsbank.clientbankapplication.core.data.api.AuthApi
import ru.hitsbank.clientbankapplication.core.data.interceptor.AuthInterceptor
import java.util.concurrent.TimeUnit

private const val TIMEOUT_SEC = 20L

private const val AUTH_OKHTTP = "AUTH_OKHTTP"
private const val NO_AUTH_OKHTTP = "NO_AUTH_OKHTTP"
private const val AUTH_RETROFIT = "AUTH_RETROFIT"
private const val NO_AUTH_RETROFIT = "NO_AUTH_RETROFIT"

private fun loggingInterceptor() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

private fun gson() = GsonBuilder().create()

private fun noAuthOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
) = buildOkHttpClient(loggingInterceptor)

private fun authOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    authInterceptor: AuthInterceptor,
) = buildOkHttpClient(
    loggingInterceptor,
    authInterceptor,
)

private fun retrofit(
    okHttpClient: OkHttpClient,
    baseUrl: String,
    gson: Gson,
) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

private fun buildOkHttpClient(
    vararg interceptors: Interceptor,
) = OkHttpClient.Builder().apply {
    interceptors.forEach { addInterceptor(it) }
    connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
    writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
    readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
}.build()

private fun authApi(retrofit: Retrofit) = retrofit.create(AuthApi::class.java)

fun networkModule() = module {
    singleOf(::loggingInterceptor)
    singleOf(::AuthInterceptor)
    singleOf(::gson)
    singleOf(::noAuthOkHttpClient) { named(NO_AUTH_OKHTTP) }
    singleOf(::authOkHttpClient) { named(AUTH_OKHTTP) }

    single(named(NO_AUTH_RETROFIT)) {
        retrofit(get(named(NO_AUTH_OKHTTP)), get(), get())
    }
    single(named(AUTH_RETROFIT)) {
        retrofit(get(named(AUTH_OKHTTP)), get(), get())
    }

    single { authApi(get(named(NO_AUTH_RETROFIT))) }
}
