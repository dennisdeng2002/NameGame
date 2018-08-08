package com.example.android.namegame.dagger

import com.example.android.namegame.api.ProfileApi
import com.example.android.namegame.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesProfileRepository(profileApi: ProfileApi): ProfileRepository {
        return ProfileRepository(profileApi)
    }

}
