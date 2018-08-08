package com.example.android.namegame.dagger

import com.example.android.namegame.NameGameApp
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(modules = [
    ActivityModule::class,
    AndroidSupportInjectionModule::class,
    AppModule::class,
    NetModule::class,
    RepositoryModule::class
])
@Singleton
interface AppComponent {
    fun inject(nameGameApp: NameGameApp)
}
