package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.drivenext.app.data.repository.AuthRepository
import com.drivenext.app.data.repository.AuthRepositoryImpl
import com.drivenext.app.presentation.viewmodel.LoginViewModel
import com.drivenext.app.utils.Prefs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

/**
 * Экран входа в аккаунт
 * Позволяет пользователю войти через email/пароль или Google
 */
class LoginActivity : AppCompatActivity() {
    
    private lateinit var viewModel: LoginViewModel
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        val prefs = Prefs(this)
        val authRepository: AuthRepository = AuthRepositoryImpl(prefs)
        val factory = com.drivenext.app.presentation.viewmodel.ViewModelFactory(authRepository, null)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        
        initViews()
        setupObservers()
        setupClickListeners()
    }
    
    private fun initViews() {
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        
        // Валидация в реальном времени
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.validateEmail(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.validatePassword(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
    
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Обновление ошибок полей
                tilEmail.error = state.emailError
                tilPassword.error = state.passwordError
                
                // Индикация загрузки
                btnLogin.isEnabled = !state.isLoading
                
                // Обработка успешного входа
                if (state.isSuccess) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                
                // Обработка ошибок
                state.error?.let { error ->
                    AlertDialog.Builder(this@LoginActivity)
                        .setTitle("Ошибка")
                        .setMessage(error)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            viewModel.login(email, password)
        }
        
        findViewById<TextView>(R.id.btnForgotPassword)?.setOnClickListener {
            Toast.makeText(this, "Функция восстановления пароля в разработке", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<TextView>(R.id.btnRegister)?.setOnClickListener {
            startActivity(Intent(this, SignUp1Activity::class.java))
        }
        
        // Google Sign In - временно отключено
        findViewById<Button>(R.id.btnGoogleLogin)?.setOnClickListener {
            Toast.makeText(this, "Вход через Google (временно отключен)", Toast.LENGTH_SHORT).show()
        }
    }
}

