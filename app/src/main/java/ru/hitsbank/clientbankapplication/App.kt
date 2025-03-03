package ru.hitsbank.clientbankapplication

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.hitsbank.clientbankapplication.di.applicationModule
import ru.hitsbank.clientbankapplication.di.dataModule
import ru.hitsbank.clientbankapplication.di.domainModule
import ru.hitsbank.clientbankapplication.di.navigationModule
import ru.hitsbank.clientbankapplication.di.networkModule
import ru.hitsbank.clientbankapplication.di.presentationModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                applicationModule(),
                presentationModule(),
                navigationModule(),
                domainModule(),
                dataModule(),
                networkModule(),
            )
        }
    }
}