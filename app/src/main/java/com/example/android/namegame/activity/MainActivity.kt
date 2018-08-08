package com.example.android.namegame.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.android.namegame.R
import com.example.android.namegame.activity.StandardModeActivity.Companion.HINT_MODE
import com.example.android.namegame.activity.StandardModeActivity.Companion.MATT_MODE
import com.example.android.namegame.activity.StandardModeActivity.Companion.MODE_KEY
import com.example.android.namegame.repository.ProfileRepository
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.loading.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var profileRepository: ProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupButtons()
        fetchProfiles()
    }

    private fun setupButtons() {
        standard_mode_button.setOnClickListener { startActivity(Intent(this, StandardModeActivity::class.java)) }
        matt_mode_button.setOnClickListener { startActivity(Intent(this, StandardModeActivity::class.java).putExtra(MODE_KEY, MATT_MODE)) }
        hint_mode_button.setOnClickListener { startActivity(Intent(this, StandardModeActivity::class.java).putExtra(MODE_KEY, HINT_MODE)) }
    }

    private fun fetchProfiles() {
        toggleLoading(true)
        profileRepository.getProfiles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { profiles ->
                    if (profiles.isNotEmpty()) {
                        toggleLoading(false)
                    }
                }
    }

    private fun toggleLoading(loading: Boolean) {
        progress_bar_linear_layout.visibility = if (loading) View.VISIBLE else View.GONE
        logo_image_view.alpha = if (loading) 0.5f else 1.0f
        standard_mode_button.isEnabled = !loading
        matt_mode_button.isEnabled = !loading
        hint_mode_button.isEnabled = !loading
    }

}
