package com.example.android.namegame.api

import com.example.android.namegame.model.Profile
import io.reactivex.Maybe
import retrofit2.http.GET

interface ProfileApi {

    @GET("/api/v1.0/profiles")
    fun getProfiles(): Maybe<List<Profile>>

}
