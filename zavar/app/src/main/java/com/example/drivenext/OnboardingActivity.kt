package com.example.drivenext

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.drivenext.utils.Prefs

//OnboardingActivity — экран приветствия, 3 шага

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter
    private lateinit var btnNext: Button
    private lateinit var btnSkip: TextView
    private lateinit var dotsContainer: LinearLayout

    private val items = listOf(
        OnboardingItem(
            R.drawable.onboarding_1,
            "Аренда автомобилей",
            "Открой для себя удобный способ передвижения"
        ),
        OnboardingItem(
            R.drawable.onboarding_2,
            "Лучшие предложения",
            "Найди автомобиль по выгодной цене"
        ),
        OnboardingItem(
            R.drawable.onboarding_3,
            "Быстро и удобно",
            "Арендуй машину в несколько кликов"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        initViews()
        setupViewPager()
        setupDots()
        setupClickListeners()
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)
        dotsContainer = findViewById(R.id.dotsContainer)
    }

    private fun setupViewPager() {
        adapter = OnboardingAdapter(items)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)

                if (position == items.size - 1) {
                    animateButtonText("Поехали")
                } else {
                    animateButtonText("Далее")
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                updateDotsWithSmoothTransition(position, positionOffset)
            }
        })
    }

    private fun setupDots() {
        dotsContainer.removeAllViews()

        val dotSize = 20
        val dotMargin = 10

        for (i in items.indices) {
            val dot = ImageView(this)
            val params = LinearLayout.LayoutParams(dotSize, dotSize)
            params.setMargins(dotMargin, 0, dotMargin, 0)
            dot.layoutParams = params

            val scale = if (i == 0) 1.3f else 0.8f
            dot.scaleX = scale
            dot.scaleY = scale

            dot.setImageDrawable(createDotDrawable(i == 0))
            dotsContainer.addView(dot)
        }
    }

    private fun updateDots(position: Int) {
        for (i in 0 until dotsContainer.childCount) {
            val dot = dotsContainer.getChildAt(i) as ImageView
            val isActive = i == position
            dot.setImageDrawable(createDotDrawable(isActive))
        }
    }

    private fun updateDotsWithSmoothTransition(currentPosition: Int, offset: Float) {
        for (i in 0 until dotsContainer.childCount) {
            val dot = dotsContainer.getChildAt(i) as ImageView
            when {
                i == currentPosition -> {
                    val scale = 1.3f - (0.5f * offset)
                    dot.scaleX = scale
                    dot.scaleY = scale
                    dot.alpha = 1f - (0.3f * offset)
                }
                i == currentPosition + 1 -> {
                    val scale = 0.8f + (0.5f * offset)
                    dot.scaleX = scale
                    dot.scaleY = scale
                    dot.alpha = 0.7f + (0.3f * offset)
                }
                else -> {
                    dot.scaleX = 0.8f
                    dot.scaleY = 0.8f
                    dot.alpha = 0.7f
                }
            }
            val isActive = i == currentPosition || (i == currentPosition + 1 && offset > 0.5f)
            dot.setImageDrawable(createDotDrawable(isActive))
        }
    }

    private fun createDotDrawable(isActive: Boolean): GradientDrawable {
        val color = if (isActive) {
            ContextCompat.getColor(this, R.color.indicator_active)
        } else {
            ContextCompat.getColor(this, R.color.indicator_inactive)
        }

        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
            setSize(24, 24)
        }
    }

    private fun setupClickListeners() {
        btnSkip.setOnClickListener {
            completeOnboarding()
        }

        btnNext.setOnClickListener {
            if (viewPager.currentItem + 1 < items.size) {
                viewPager.currentItem++
            } else {
                completeOnboarding()
            }
        }
    }

    private fun animateButtonText(newText: String) {
        btnNext.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                btnNext.text = newText
                btnNext.animate().alpha(1f).setDuration(150).start()
            }.start()
    }

    private fun completeOnboarding() {
        Prefs.setOnboardingShown(this, true)
        startActivity(Intent(this, GettingStartedActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
