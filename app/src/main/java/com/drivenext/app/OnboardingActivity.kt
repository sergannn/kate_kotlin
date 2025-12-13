package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnboardingActivity : AppCompatActivity() {
    private var currentPage = 0
    
    private val titles = listOf(
        R.string.onboarding_title_1,
        R.string.onboarding_title_2,
        R.string.onboarding_title_3
    )
    
    private val descriptions = listOf(
        R.string.onboarding_desc_1,
        R.string.onboarding_desc_2,
        R.string.onboarding_desc_3
    )
    
    private val buttonTexts = listOf(
        R.string.next,
        R.string.next,
        R.string.go
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        
        val tvSkip = findViewById<TextView>(R.id.tvSkip)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val viewProgress1 = findViewById<View>(R.id.viewProgress1)
        val viewProgress2 = findViewById<View>(R.id.viewProgress2)
        val viewProgress3 = findViewById<View>(R.id.viewProgress3)
        
        tvSkip.setOnClickListener {
            startActivity(Intent(this, GettingStartedActivity::class.java))
            finish()
        }
        
        btnNext.setOnClickListener {
            if (currentPage < 2) {
                currentPage++
                updatePage(tvTitle, tvDescription, btnNext, viewProgress1, viewProgress2, viewProgress3)
            } else {
                startActivity(Intent(this, GettingStartedActivity::class.java))
                finish()
            }
        }
        
        updatePage(tvTitle, tvDescription, btnNext, viewProgress1, viewProgress2, viewProgress3)
    }
    
    private fun updatePage(
        tvTitle: TextView,
        tvDescription: TextView,
        btnNext: Button,
        viewProgress1: View,
        viewProgress2: View,
        viewProgress3: View
    ) {
        tvTitle.setText(titles[currentPage])
        tvDescription.setText(descriptions[currentPage])
        btnNext.setText(buttonTexts[currentPage])
        
        val params1 = viewProgress1.layoutParams
        val params2 = viewProgress2.layoutParams
        val params3 = viewProgress3.layoutParams
        
        when (currentPage) {
            0 -> {
                viewProgress1.setBackgroundColor(getColor(R.color.primary))
                params1.width = resources.getDimensionPixelSize(R.dimen.progress_indicator_active)
                viewProgress2.setBackgroundColor(getColor(R.color.gray_light))
                params2.width = resources.getDimensionPixelSize(R.dimen.progress_indicator_inactive)
                viewProgress3.setBackgroundColor(getColor(R.color.gray_light))
                params3.width = resources.getDimensionPixelSize(R.dimen.progress_indicator_inactive)
            }
            1 -> {
                viewProgress1.setBackgroundColor(getColor(R.color.gray_light))
                params1.width = resources.getDimensionPixelSize(R.dimen.progress_indicator_inactive)
                viewProgress2.setBackgroundColor(getColor(R.color.primary))
                params2.width = resources.getDimensionPixelSize(R.dimen.progress_indicator_active)
                viewProgress3.setBackgroundColor(getColor(R.color.gray_light))
                params3.width = resources.getDimensionPixelSize(R.dimen.progress_indicator_inactive)
            }
            2 -> {
                viewProgress1.setBackgroundColor(getColor(R.color.gray_light))
                params1.width = resources.getDimensionPixelSize(R.dimen.progress_indicator_inactive)
                viewProgress2.setBackgroundColor(getColor(R.color.gray_light))
                params2.width = resources.getDimensionPixelSize(R.dimen.progress_indicator_inactive)
                viewProgress3.setBackgroundColor(getColor(R.color.primary))
                params3.width = resources.getDimensionPixelSize(R.dimen.progress_indicator_active)
            }
        }
        viewProgress1.layoutParams = params1
        viewProgress2.layoutParams = params2
        viewProgress3.layoutParams = params3
    }
}

