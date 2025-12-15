package com.example.drivenext

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.utils.Validator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button

    private var registeredEmail: String = ""
    private var registeredPassword: String = ""

    companion object {
        private const val PREFS_NAME = "drive_next_settings"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Получаем данные из SuccessRegisterActivity
        registeredEmail = intent.getStringExtra("prefilled_email") ?: ""
        registeredPassword = intent.getStringExtra("registered_password") ?: ""

        initViews()
        setupTextWatchers()
        setupClickListeners()

        // Если пришел email из регистрации, заполняем поле
        if (registeredEmail.isNotEmpty()) {
            etEmail.setText(registeredEmail)
            Toast.makeText(this, "Email заполнен. Введите пароль для входа", Toast.LENGTH_LONG).show()
        }
    }

    private fun initViews() {
        val btnRegister = findViewById<TextView>(R.id.btnRegister)
        btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoogleLogin = findViewById<Button>(R.id.btnGoogleLogin)
        val btnForgotPassword = findViewById<TextView>(R.id.btnForgotPassword)

        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistrationStep1Activity::class.java)
            startActivity(intent)
        }

        btnGoogleLogin.setOnClickListener {
            Toast.makeText(this, "Вход через Google", Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
            navigateToMain()
        }

        btnForgotPassword.setOnClickListener {
            Toast.makeText(this, "Восстановление пароля", Toast.LENGTH_SHORT).show()
        }

        btnLogin.isEnabled = false
    }

    private fun setupTextWatchers() {
        val commonTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateAllFieldsInRealTime()
                updateButtonState()
            }
        }

        etEmail.addTextChangedListener(commonTextWatcher)
        etPassword.addTextChangedListener(commonTextWatcher)
    }

    private fun validateAllFieldsInRealTime() {
        validateEmail()
        validatePassword()
    }

    private fun validateEmail() {
        val email = etEmail.text.toString().trim()
        val error = Validator.getEmailError(email)
        tilEmail.error = error
    }

    private fun validatePassword() {
        val password = etPassword.text.toString()
        val error = when {
            password.isEmpty() -> "Введите пароль"
            password.length < 6 -> "Пароль должен быть не менее 6 символов"
            else -> null
        }
        tilPassword.error = error
    }

    private fun updateButtonState() {
        val isEmailValid = tilEmail.error == null && etEmail.text.toString().isNotEmpty()
        val isPasswordValid = tilPassword.error == null && etPassword.text.toString().isNotEmpty()

        btnLogin.isEnabled = isEmailValid && isPasswordValid
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            if (validateAllFieldsFinal()) {
                performLogin()
            }
        }
    }

    private fun validateAllFieldsFinal(): Boolean {
        validateAllFieldsInRealTime()

        if (etEmail.text.toString().trim().isEmpty()) {
            tilEmail.error = "Введите email"
            return false
        }

        if (etPassword.text.toString().isEmpty()) {
            tilPassword.error = "Введите пароль"
            return false
        }

        return tilEmail.error == null && tilPassword.error == null
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        // ПРОВЕРЯЕМ С ДАННЫМИ ИЗ РЕГИСТРАЦИИ
        if (email == registeredEmail && password == registeredPassword) {
            // УСПЕШНЫЙ ВХОД - СОХРАНЯЕМ ФЛАГ АВТОРИЗАЦИИ
            sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()

            Toast.makeText(this, "Вход выполнен успешно!", Toast.LENGTH_SHORT).show()
            navigateToMain()
        } else {
            // Дополнительная проверка с тестовыми данными
            if (email == "test@example.com" && password == "password123") {
                sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
                Toast.makeText(this, "Вход выполнен (тестовый аккаунт)", Toast.LENGTH_SHORT).show()
                navigateToMain()
            } else {
                if (email != registeredEmail && email != "test@example.com") {
                    tilEmail.error = "Пользователь с таким email не найден"
                } else {
                    tilPassword.error = "Неправильный пароль"
                }
                Toast.makeText(this, "Неправильный email или пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}