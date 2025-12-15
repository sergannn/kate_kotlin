package com.example.drivenext

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.utils.Validator
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistrationStep1Activity : AppCompatActivity() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var cbAgreement: MaterialCheckBox
    private lateinit var btnNext: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_step1)

        initViews()
        setupTextWatchers()
        setupClickListeners()
    }

    private fun initViews() {
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        cbAgreement = findViewById(R.id.cbAgreement)
        btnNext = findViewById(R.id.btnNext)

        btnNext.isEnabled = false
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
        etConfirmPassword.addTextChangedListener(commonTextWatcher)

        cbAgreement.setOnCheckedChangeListener { _, _ ->
            updateButtonState()
        }
    }

    private fun validateAllFieldsInRealTime() {
        validateEmail()
        validatePassword()
        validatePasswordMatch()
    }

    private fun validateEmail() {
        val email = etEmail.text.toString().trim()
        val error = Validator.getEmailError(email)
        tilEmail.error = error
    }

    private fun validatePassword() {
        val password = etPassword.text.toString()
        val error = Validator.getPasswordError(password)
        tilPassword.error = error
    }

    private fun validatePasswordMatch() {
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        val error = when {
            confirmPassword.isEmpty() -> null
            !Validator.doPasswordsMatch(password, confirmPassword) -> "Пароли не совпадают"
            else -> null
        }
        tilConfirmPassword.error = error
    }

    private fun updateButtonState() {
        val isEmailValid = tilEmail.error == null && etEmail.text.toString().isNotEmpty()
        val isPasswordValid = tilPassword.error == null && etPassword.text.toString().isNotEmpty()
        val isConfirmValid = tilConfirmPassword.error == null && etConfirmPassword.text.toString().isNotEmpty()
        val isAgreementChecked = cbAgreement.isChecked

        btnNext.isEnabled = isEmailValid && isPasswordValid && isConfirmValid && isAgreementChecked
    }

    private fun setupClickListeners() {
        btnNext.setOnClickListener {
            if (validateAllFieldsFinal()) {
                navigateToNextStep()
            }
        }
    }

    private fun navigateToNextStep() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        val intent = Intent(this, RegistrationStep2Activity::class.java).apply {
            putExtra("user_email", email)
            putExtra("user_password", password)
        }
        startActivity(intent)
    }

    private fun validateAllFieldsFinal(): Boolean {
        validateAllFieldsInRealTime()

        if (!cbAgreement.isChecked) {
            android.widget.Toast.makeText(
                this,
                "Необходимо согласие с условиями обслуживания и политикой конфиденциальности",
                android.widget.Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return tilEmail.error == null &&
                tilPassword.error == null &&
                tilConfirmPassword.error == null &&
                cbAgreement.isChecked
    }
}