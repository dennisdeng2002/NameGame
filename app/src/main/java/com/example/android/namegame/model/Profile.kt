package com.example.android.namegame.model

data class Profile(
        val id: String,
        val type: String,
        val slug: String,
        val jobTitle: String,
        val firstName: String,
        val lastName: String,
        val headshot: Headshot,
        val socialLinks: List<SocialLink>
) {

    val fullName: String get() = "$firstName $lastName"

}
