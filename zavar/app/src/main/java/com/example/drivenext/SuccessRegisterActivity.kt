package com.example.drivenext

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SuccessRegisterActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var registeredEmail: String
    private lateinit var registeredPassword: String

    companion object {
        private const val PREFS_NAME = "drive_next_settings"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_register)

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        registeredEmail = intent.getStringExtra("registered_email") ?: ""
        registeredPassword = intent.getStringExtra("registered_password") ?: ""

        // Флаг авторизации НЕ устанавливаем - пользователь должен войти
        setupClickListeners()
    }

    private fun setupClickListeners() {
        val btnGoToLogin: Button = findViewById(R.id.btnGoToLogin)

        btnGoToLogin.setOnClickListener {
            // После регистрации переходим на ЛОГИН, а не на главную
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            // Передаем зарегистрированные данные для автозаполнения
            putExtra("prefilled_email", registeredEmail)
            putExtra("registered_password", registeredPassword)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}