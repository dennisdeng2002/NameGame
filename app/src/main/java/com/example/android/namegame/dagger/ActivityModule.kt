package com.example.android.namegame.dagger

import com.example.android.namegame.activity.MainActivity
import com.example.android.namegame.activity.StandardModeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindStandardModeActivity(): StandardModeActivity

}
