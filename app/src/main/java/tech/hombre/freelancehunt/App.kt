package tech.hombre.freelancehunt

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import tech.hombre.data.di.databaseModule
import tech.hombre.data.di.networkingModule
import tech.hombre.data.di.preferencesModule
import tech.hombre.data.di.repositoryModule
import tech.hombre.domain.di.interactionModule
import tech.hombre.freelancehunt.di.appModule
import tech.hombre.freelancehunt.di.presentationModule

class App : Application() {

    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@App)
            if (BuildConfig.DEBUG) androidLogger(Level.DEBUG)
            modules(appModules + domainModules + dataModules)
        }
    }
}

val appModules = listOf(presentationModule, appModule)
val domainModules = listOf(interactionModule)
val dataModules = listOf(networkingModule, repositoryModule, databaseModule, preferencesModule)
