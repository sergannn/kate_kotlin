package com.example.drivenext

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.utils.NetworkUtils
import com.example.drivenext.utils.Prefs

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkAppState()
        }, 2000)
    }

    private fun checkAppState() {
        // 1. Проверяем интернет
        if (!NetworkUtils.isNetworkAvailable(this)) {
            startActivity(Intent(this, NoConnectionActivity::class.java))
            finish()
            return
        }

        // 2. Проверяем, был ли показан Onboarding
        if (!Prefs.isOnboardingShown(this)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        // 3. Проверяем, есть ли сохранённый токен
        val token = Prefs.getAuthToken(this)
        if (token.isNullOrEmpty()) {
            startActivity(Intent(this, GettingStartedActivity::class.java))
            finish()
        } else {
            // ИСПРАВЛЕНО: Переход на новый MainActivity со списком автомобилей
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
