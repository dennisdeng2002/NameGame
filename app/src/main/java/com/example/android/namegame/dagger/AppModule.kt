package com.example.android.namegame.dagger

import com.example.android.namegame.NameGameApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val nameGameApp: NameGameApp) {

    @Provides
    @Singleton
    fun providesNameGameApp(): NameGameApp {
        return nameGameApp
    }

}
