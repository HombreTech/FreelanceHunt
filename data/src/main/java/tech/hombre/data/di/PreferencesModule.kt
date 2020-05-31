package tech.hombre.data.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tech.hombre.data.local.LocalProperties

val preferencesModule = module {
    factory {
        LocalProperties(
            androidContext().getSharedPreferences(
                "preferences",
                Context.MODE_PRIVATE
            )
        )
    }
}