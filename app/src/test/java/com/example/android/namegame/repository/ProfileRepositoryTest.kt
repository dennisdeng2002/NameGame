package com.example.android.namegame.repository

import com.example.android.namegame.api.ProfileApi
import com.example.android.namegame.model.Profile
import com.google.gson.GsonBuilder
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test

import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class ProfileRepositoryTest {

    private lateinit var profiles: List<Profile>
    private lateinit var profileRepository: ProfileRepository

    @Before
    fun setUp() {
        val profilesUrl = this.javaClass.classLoader.getResource("profiles.json")
        val profilesJson = Files.lines(Paths.get(profilesUrl.toURI())).parallel().collect(Collectors.joining())
        val gson = GsonBuilder().setLenient().create()
        profiles = arrayListOf(*gson.fromJson(profilesJson, Array<Profile>::class.java))

        val profileApi = mock(ProfileApi::class.java)
        `when`(profileApi.getProfiles())
                .thenReturn(Maybe.just(profiles))

        profileRepository = ProfileRepository(profileApi)
    }

    @Test
    fun getProfiles() {
        profileRepository.getProfiles()
                .test()
                .assertResult(profiles)
    }

    @Test
    fun getRandomProfiles() {
        profileRepository.getRandomProfiles(6)
                .test()
                .assertValue { it.size == 6 }
    }

    @Test
    fun getMattProfiles() {
        profileRepository.getMattProfiles(6)
                .test()
                .assertValue { profiles -> profiles.all { it.firstName.contains("Matt") } }
    }

}
