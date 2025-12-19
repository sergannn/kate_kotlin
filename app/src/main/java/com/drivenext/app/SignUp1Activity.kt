package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.utils.Prefs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignUp1Activity : AppCompatActivity() {
    
    private lateinit var prefs: Prefs
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnNext: Button
    private lateinit var btnBack: ImageButton
        
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup1)
        
        prefs = Prefs(this)
        
        initViews()
        setupClickListeners()
        loadSavedData()
    }
    
    private fun initViews() {
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        cbTerms = findViewById(R.id.cbTerms)
        btnNext = findViewById(R.id.btnNext)
        btnBack = findViewById(R.id.btnBack)
    }
    
    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            onBackPressed()
        }
        
        btnNext.setOnClickListener {
            if (validateFields()) {
                saveData()
                navigateToNext()
            }
        }
        
        // Валидация в реальном времени
        etEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateEmail()
        }
        etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validatePassword()
        }
        etConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateConfirmPassword()
        }
    }
    
    private fun loadSavedData() {
        prefs.registrationEmail?.let {
            etEmail.setText(it)
        }
        prefs.registrationPassword?.let {
            etPassword.setText(it)
            etConfirmPassword.setText(it)
        }
    }
    
    private fun saveData() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        
        prefs.registrationEmail = email
        prefs.registrationPassword = password
    }
    
    private fun validateFields(): Boolean {
        val emailValid = validateEmail()
        val passwordValid = validatePassword()
        val confirmValid = validateConfirmPassword()
        val termsChecked = cbTerms.isChecked
        
        if (!termsChecked) {
            Toast.makeText(this, "Необходимо согласие с условиями", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return emailValid && passwordValid && confirmValid
    }
    
    private fun validateEmail(): Boolean {
        val email = etEmail.text.toString().trim()
        return when {
            email.isEmpty() -> {
                tilEmail.error = "Введите email"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                tilEmail.error = "Неверный формат email"
                false
            }
            else -> {
                tilEmail.error = null
                true
            }
        }
    }
    
    private fun validatePassword(): Boolean {
        val password = etPassword.text.toString()
        return when {
            password.isEmpty() -> {
                tilPassword.error = "Введите пароль"
                false
            }
            password.length < 6 -> {
                tilPassword.error = "Пароль должен содержать минимум 6 символов"
                false
            }
            else -> {
                tilPassword.error = null
                true
            }
        }
    }
    
    private fun validateConfirmPassword(): Boolean {
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()
        return when {
            confirmPassword.isEmpty() -> {
                tilConfirmPassword.error = "Повторите пароль"
                false
            }
            password != confirmPassword -> {
                tilConfirmPassword.error = "Пароли не совпадают"
                false
            }
            else -> {
                tilConfirmPassword.error = null
                true
            }
        }
    }
    
    private fun navigateToNext() {
        val intent = Intent(this, SignUp2Activity::class.java)
        startActivity(intent)
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
