package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.data.repository.AuthRepository
import com.drivenext.app.data.repository.AuthRepositoryImpl
import com.drivenext.app.utils.Prefs

/**
 * Экран настроек приложения
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val prefs = Prefs(this)
        authRepository = AuthRepositoryImpl(prefs)

        setupClickListeners()
        loadUserInfo()
    }

    private fun setupClickListeners() {
        // Профиль пользователя
        findViewById<LinearLayout>(R.id.layoutProfile)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Мои бронирования
        findViewById<LinearLayout>(R.id.layoutMyBookings)?.setOnClickListener {
            startActivity(Intent(this, MyBookingsActivity::class.java))
        }

        // Подключить автомобиль
        findViewById<LinearLayout>(R.id.layoutAddCar)?.setOnClickListener {
            startActivity(Intent(this, BecomeOwnerActivity::class.java))
        }

        // Нижняя навигация
        findViewById<LinearLayout>(R.id.layoutHome)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.layoutBookmarks)?.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.layoutSettings)?.setOnClickListener {
            // Уже на экране настроек
        }

        // TODO: Остальные пункты меню
    }

    private fun loadUserInfo() {
        val prefs = Prefs(this)
        findViewById<TextView>(R.id.tvUserName)?.text = prefs.userEmail ?: "Пользователь"
        // TODO: Загрузить полную информацию о пользователе
    }
}

