package com.example.android.namegame

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import com.example.android.namegame.dagger.*
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class NameGameApp : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject lateinit var dispatchingAndroidInjectorActivity: DispatchingAndroidInjector<Activity>
    @Inject lateinit var dispatchingAndroidInjectorFragment: DispatchingAndroidInjector<Fragment>

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .netModule(NetModule("https://willowtreeapps.com/"))
                .repositoryModule(RepositoryModule())
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjectorActivity
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjectorFragment
    }

}
