package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.utils.Prefs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignUp2Activity : AppCompatActivity() {
    
    private lateinit var prefs: Prefs
    private lateinit var tilLastName: TextInputLayout
    private lateinit var tilFirstName: TextInputLayout
    private lateinit var tilMiddleName: TextInputLayout
    private lateinit var tilBirthDate: TextInputLayout
    private lateinit var etLastName: TextInputEditText
    private lateinit var etFirstName: TextInputEditText
    private lateinit var etMiddleName: TextInputEditText
    private lateinit var etBirthDate: TextInputEditText
    private lateinit var rgGender: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var btnNext: Button
    private lateinit var btnBack: ImageButton
        
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2)
        
        prefs = Prefs(this)
        
        initViews()
        setupClickListeners()
        loadSavedData()
    }
    
    private fun initViews() {
        tilLastName = findViewById(R.id.tilLastName)
        tilFirstName = findViewById(R.id.tilFirstName)
        tilMiddleName = findViewById(R.id.tilMiddleName)
        tilBirthDate = findViewById(R.id.tilBirthDate)
        etLastName = findViewById(R.id.etLastName)
        etFirstName = findViewById(R.id.etFirstName)
        etMiddleName = findViewById(R.id.etMiddleName)
        etBirthDate = findViewById(R.id.etBirthDate)
        rgGender = findViewById(R.id.rgGender)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
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
        etLastName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateLastName()
        }
        etFirstName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateFirstName()
        }
        etBirthDate.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateBirthDate()
        }
    }
    
    private fun loadSavedData() {
        prefs.registrationLastName?.let {
            etLastName.setText(it)
        }
        prefs.registrationFirstName?.let {
            etFirstName.setText(it)
        }
        prefs.registrationMiddleName?.let {
            etMiddleName.setText(it)
        }
        prefs.registrationBirthDate?.let {
            etBirthDate.setText(it)
        }
        prefs.registrationGender?.let {
            when (it) {
                "Мужской" -> rbMale.isChecked = true
                "Женский" -> rbFemale.isChecked = true
            }
        } ?: run {
            // По умолчанию выбран мужской
            rbMale.isChecked = true
        }
    }
    
    private fun saveData() {
        val lastName = etLastName.text.toString().trim()
        val firstName = etFirstName.text.toString().trim()
        val middleName = etMiddleName.text.toString().trim()
        val birthDate = etBirthDate.text.toString().trim()
        val gender = when {
            rbMale.isChecked -> "Мужской"
            rbFemale.isChecked -> "Женский"
            else -> "Мужской"
        }
        
        prefs.registrationLastName = lastName
        prefs.registrationFirstName = firstName
        prefs.registrationMiddleName = middleName.ifEmpty { null }
        prefs.registrationBirthDate = birthDate
        prefs.registrationGender = gender
    }
    
    private fun validateFields(): Boolean {
        val lastNameValid = validateLastName()
        val firstNameValid = validateFirstName()
        val birthDateValid = validateBirthDate()
        val genderSelected = rbMale.isChecked || rbFemale.isChecked
        
        if (!genderSelected) {
            Toast.makeText(this, "Выберите пол", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return lastNameValid && firstNameValid && birthDateValid
    }
    
    private fun validateLastName(): Boolean {
        val lastName = etLastName.text.toString().trim()
        return when {
            lastName.isEmpty() -> {
                tilLastName.error = "Введите фамилию"
                false
            }
            lastName.length < 2 -> {
                tilLastName.error = "Фамилия должна содержать минимум 2 символа"
                false
            }
            else -> {
                tilLastName.error = null
                true
            }
        }
    }
    
    private fun validateFirstName(): Boolean {
        val firstName = etFirstName.text.toString().trim()
        return when {
            firstName.isEmpty() -> {
                tilFirstName.error = "Введите имя"
                false
            }
            firstName.length < 2 -> {
                tilFirstName.error = "Имя должно содержать минимум 2 символа"
                false
            }
            else -> {
                tilFirstName.error = null
                true
            }
        }
    }
    
    private fun validateBirthDate(): Boolean {
        val birthDate = etBirthDate.text.toString().trim()
        return when {
            birthDate.isEmpty() -> {
                tilBirthDate.error = "Введите дату рождения"
                false
            }
            else -> {
                tilBirthDate.error = null
                true
            }
        }
    }
    
    private fun navigateToNext() {
        val intent = Intent(this, SignUp3Activity::class.java)
        startActivity(intent)
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
