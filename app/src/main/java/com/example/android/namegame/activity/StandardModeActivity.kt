package com.example.android.namegame.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.Toast
import com.example.android.namegame.R
import com.example.android.namegame.model.Profile
import com.example.android.namegame.repository.ProfileRepository
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.supercharge.shimmerlayout.ShimmerLayout
import kotlinx.android.synthetic.main.activity_standard_mode.*
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class StandardModeActivity : AppCompatActivity() {

    @Inject lateinit var profileRepository: ProfileRepository
    private lateinit var shimmerLayouts: List<ShimmerLayout>
    private lateinit var imageViews: List<ImageView>
    private var numberOfProfiles: Int = 6
    private var mode: Int = StandardModeActivity.STANDARD_MODE
    private val random: Random = Random()
    private var selectedIndex: Int = 0
    private val shakeAnimation: TranslateAnimation = TranslateAnimation(0f, 10f, 0f, 0f).apply {
        duration = 500
        interpolator = CycleInterpolator(5f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_standard_mode)
        shimmerLayouts = arrayListOf(image_view_1_container, image_view_2_container, image_view_3_container, image_view_4_container, image_view_5_container, image_view_6_container)
        imageViews = arrayListOf(image_view_1, image_view_2, image_view_3, image_view_4, image_view_5, image_view_6)
        numberOfProfiles = imageViews.size
        mode = intent.getIntExtra(MODE_KEY, StandardModeActivity.STANDARD_MODE)
        getProfiles()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_standard_mode, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.refresh -> {
                refresh()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun refresh() {
        imageViews.forEach { it.visibility = View.VISIBLE }
        getProfiles()
    }

    private fun getProfiles() {
        when (mode) {
            STANDARD_MODE -> profileRepository.getRandomProfiles(numberOfProfiles)
            MATT_MODE -> profileRepository.getMattProfiles(numberOfProfiles)
            HINT_MODE -> profileRepository.getRandomProfiles(numberOfProfiles)
            else -> profileRepository.getRandomProfiles(numberOfProfiles)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { profiles ->
                    if (profiles.size == 6) {
                        setup(profiles)
                    } else {
                        Toast.makeText(this, "Error Loading Profiles", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun getFadeAnimation(imageView: ImageView): AlphaAnimation {
        return AlphaAnimation(1.0f, 0.0f).apply {
            duration = 500
            interpolator = AccelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    imageView.visibility = View.GONE
                }

                override fun onAnimationStart(p0: Animation?) {}
            })
        }
    }

    private fun setup(profiles: List<Profile>) {
        selectedIndex = random.nextInt(numberOfProfiles)
        name_text_view.text = profiles[selectedIndex].fullName
        for ((index, imageView) in imageViews.withIndex()) {
            imageView.setOnClickListener {
                if (selectedIndex == index) {
                    imageViews.forEachIndexed { index, imageView ->
                        if (selectedIndex != index && imageView.visibility == View.VISIBLE) imageView.startAnimation(getFadeAnimation(imageView))
                    }
                } else {
                    imageView.startAnimation(shakeAnimation)
                }
            }
        }

        profiles.forEachIndexed { index, profile ->
            shimmerLayouts[index].startShimmerAnimation()
            Picasso.get()
                    .load("https:${profile.headshot.url}")
                    .resize(150, 150)
                    .error(R.drawable.account)
                    .into(imageViews[index], object : Callback {
                        override fun onSuccess() {
                            shimmerLayouts[index].stopShimmerAnimation()
                        }

                        override fun onError(e: Exception?) {
                            e?.printStackTrace()
                            shimmerLayouts[index].stopShimmerAnimation()
                            imageViews[index].setBackgroundColor(ContextCompat.getColor(this@StandardModeActivity, android.R.color.white))
                        }
                    })
        }

        if (mode == HINT_MODE) {
            imageViews.filterIndexed { index, _ -> index != selectedIndex }
                    .forEachIndexed { index, imageView ->
                        val animation = getFadeAnimation(imageView)
                        animation.startOffset = 2500L * (index + 1)
                        imageView.startAnimation(animation)
                    }
        }
    }

    companion object {
        const val MODE_KEY = "MODE_KEY"
        const val STANDARD_MODE = 0
        const val MATT_MODE = 1
        const val HINT_MODE = 2
    }

}
