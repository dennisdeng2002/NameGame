package com.example.android.namegame.repository

import com.example.android.namegame.api.ProfileApi
import com.example.android.namegame.model.Profile
import io.reactivex.Maybe

class ProfileRepository(
        private val profileApi: ProfileApi
) {

    private var profiles: List<Profile>? = null

    fun getProfiles(): Maybe<List<Profile>> {
        if (profiles != null) return Maybe.just(profiles)
        return profileApi.getProfiles()
                .map { p ->
                    profiles = p
                    return@map profiles
                }
    }

    fun getRandomProfiles(numberOfProfiles: Int): Maybe<List<Profile>> {
        return getProfiles()
                .map { profiles ->
                    if (profiles.size < numberOfProfiles) throw Exception("numberOfProfiles exceeds available profiles")
                    return@map profiles.shuffled().take(numberOfProfiles)
                }
    }

    fun getMattProfiles(numberOfProfiles: Int): Maybe<List<Profile>> {
        return getProfiles()
                .map { profiles ->
                    val mattProfiles = profiles.filter { it.firstName.contains("Matt") }
                    if (mattProfiles.size < numberOfProfiles) throw Exception("numberOfProfiles exceeds available profiles")
                    return@map mattProfiles
                            .shuffled()
                            .take(6)
                }
    }

}
