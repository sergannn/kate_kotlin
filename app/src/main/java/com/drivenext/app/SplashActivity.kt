package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.data.repository.AuthRepository
import com.drivenext.app.data.repository.AuthRepositoryImpl
import com.drivenext.app.utils.Prefs

/**
 * Экран загрузки приложения
 * Отображается 2-3 секунды, затем переходит на Onboarding или Main в зависимости от состояния авторизации
 */
class SplashActivity : AppCompatActivity() {
    
    private lateinit var prefs: Prefs
    private lateinit var authRepository: AuthRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        prefs = Prefs(this)
        authRepository = AuthRepositoryImpl(prefs)
        
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2500) // 2.5 секунды
    }
    
    private fun navigateToNextScreen() {
        // Если пользователь уже прошел онбординг и авторизован - на главный экран
        if (prefs.isOnboardingCompleted && authRepository.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        // Если прошел онбординг, но не авторизован - на экран входа/регистрации
        else if (prefs.isOnboardingCompleted) {
            startActivity(Intent(this, GettingStartedActivity::class.java))
        }
        // Если не прошел онбординг - показать онбординг
        else {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
        finish()
    }
}

