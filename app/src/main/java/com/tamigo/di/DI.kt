package com.tamigo.di

import android.content.Context
import android.content.SharedPreferences
import com.tamigo.main.MainViewModel
import com.tamigo.main.MainViewModelImpl
import com.tamigo.managers.HealthConnectManager
import com.tamigo.navigation.MainRouter
import com.tamigo.navigation.Router
import com.tamigo.preferences.Preferences
import com.tamigo.preferences.PreferencesImpl
import com.tamigo.viewModel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val navigationModule = module {
    single {
        MainRouter()
    } bind (Router::class)
}

val prefsRepositoryModule = module {
    single {
        getSharedPreferences(androidApplication())
    }
    single {
        PreferencesImpl(get())
    }.bind(Preferences::class)
}

val viewModelModule = module {
    viewModel<HomeViewModel> { HomeViewModelImpl(get(), get()) }
    viewModel<RegistrationViewModel> { RegistrationViewModelImpl(get(), get()) }
    viewModel<MainViewModel> { MainViewModelImpl(get(), get(), get()) }
    viewModel<TargetsViewModel> { TargetsViewModelImpl(get(), get()) }
}

val managersModule = module {
    single {
        HealthConnectManager(androidApplication())
    }
}

private fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(PreferencesImpl.KEY, Context.MODE_PRIVATE)
}

val appModules = arrayListOf(
    navigationModule,
    prefsRepositoryModule,
    viewModelModule,
    managersModule
)
