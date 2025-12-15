package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/**
 * Экран онбординга с 3 слайдами
 */
class OnboardingActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        
        val tvSkip = findViewById<TextView>(R.id.btnSkip)
        val btnNext = findViewById<MaterialButton>(R.id.btnNext)
        val viewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPager)
        
        tvSkip?.setOnClickListener {
            // Сохранить факт прохождения онбординга
            com.drivenext.app.utils.Prefs(this).isOnboardingCompleted = true
            startActivity(Intent(this, GettingStartedActivity::class.java))
            finish()
        }
        
        // Упрощенная логика - просто переходим на следующий экран
        btnNext?.setOnClickListener {
            // Сохранить факт прохождения онбординга
            com.drivenext.app.utils.Prefs(this).isOnboardingCompleted = true
            startActivity(Intent(this, GettingStartedActivity::class.java))
            finish()
        }
    }
}
