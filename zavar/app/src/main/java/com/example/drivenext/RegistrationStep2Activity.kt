package com.example.drivenext

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.utils.Validator
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistrationStep2Activity : AppCompatActivity() {

    private lateinit var tilFirstName: TextInputLayout
    private lateinit var tilLastName: TextInputLayout
    private lateinit var tilBirthDate: TextInputLayout
    private lateinit var etFirstName: TextInputEditText
    private lateinit var etLastName: TextInputEditText
    private lateinit var etMiddleName: TextInputEditText
    private lateinit var etBirthDate: TextInputEditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var btnNext: MaterialButton

    private var isGenderSelected: Boolean = false
    private var selectedGender: String = "Мужской"
    private lateinit var userEmail: String
    private lateinit var userPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_step2)

        userEmail = intent.getStringExtra("user_email") ?: ""
        userPassword = intent.getStringExtra("user_password") ?: ""

        initViews()
        setupTextWatchers()
        setupClickListeners()
    }

    private fun initViews() {
        val btnBackArrow = findViewById<ImageView>(R.id.btnBack)
        btnBackArrow.setOnClickListener {
            finish()
        }

        tilFirstName = findViewById(R.id.tilFirstName)
        tilLastName = findViewById(R.id.tilLastName)
        tilBirthDate = findViewById(R.id.tilBirthDate)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etMiddleName = findViewById(R.id.etMiddleName)
        etBirthDate = findViewById(R.id.etBirthDate)

        radioGroupGender = findViewById(R.id.radioGroupGender)
        val rbMale = findViewById<MaterialRadioButton>(R.id.rbMale)

        rbMale.isChecked = true
        isGenderSelected = true
        selectedGender = "Мужской"

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

        etFirstName.addTextChangedListener(commonTextWatcher)
        etLastName.addTextChangedListener(commonTextWatcher)
        etBirthDate.addTextChangedListener(commonTextWatcher)

        // Разрешаем ввод русских букв в поле отчества
        etMiddleName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // Отчество не требует валидации, можно вводить любые символы
            }
        })

        radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbMale -> {
                    isGenderSelected = true
                    selectedGender = "Мужской"
                }
                R.id.rbFemale -> {
                    isGenderSelected = true
                    selectedGender = "Женский"
                }
                else -> isGenderSelected = false
            }
            updateButtonState()
        }
    }

    private fun validateAllFieldsInRealTime() {
        validateFirstName()
        validateLastName()
        validateBirthDate()
    }

    private fun validateFirstName() {
        val firstName = etFirstName.text.toString().trim()
        val error = getRussianNameError(firstName, "Имя")
        tilFirstName.error = error
    }

    private fun validateLastName() {
        val lastName = etLastName.text.toString().trim()
        val error = getRussianNameError(lastName, "Фамилия")
        tilLastName.error = error
    }

    private fun validateBirthDate() {
        val birthDate = etBirthDate.text.toString().trim()
        val error = Validator.getDateError(birthDate, "дату рождения")
        tilBirthDate.error = error
    }

    // Новая функция валидации для русских имен
    private fun getRussianNameError(name: String, fieldName: String): String? {
        return when {
            name.isEmpty() -> "Введите $fieldName"
            name.length < 2 -> "$fieldName должен содержать минимум 2 символа"
            !isValidRussianName(name) -> "$fieldName должен содержать только русские буквы, дефисы и пробелы"
            else -> null
        }
    }

    // Функция проверки русских букв, дефисов и пробелов
    private fun isValidRussianName(name: String): Boolean {
        val russianNameRegex = "^[а-яА-ЯёЁ\\s\\-]+$".toRegex()
        return russianNameRegex.matches(name)
    }

    private fun updateButtonState() {
        val isFirstNameValid = tilFirstName.error == null && etFirstName.text.toString().isNotEmpty()
        val isLastNameValid = tilLastName.error == null && etLastName.text.toString().isNotEmpty()
        val isBirthDateValid = tilBirthDate.error == null && etBirthDate.text.toString().isNotEmpty()
        val isGenderValid = isGenderSelected

        btnNext.isEnabled = isFirstNameValid && isLastNameValid && isBirthDateValid && isGenderValid
    }

    private fun setupClickListeners() {
        btnNext.setOnClickListener {
            if (validateAllFieldsFinal()) {
                navigateToNextStep()
            }
        }
    }

    private fun navigateToNextStep() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val middleName = etMiddleName.text.toString().trim()
        val birthDate = etBirthDate.text.toString().trim()

        val intent = Intent(this, RegistrationStep3Activity::class.java).apply {
            putExtra("first_name", firstName)
            putExtra("last_name", lastName)
            putExtra("middle_name", middleName)
            putExtra("birth_date", birthDate)
            putExtra("gender", selectedGender)
            putExtra("user_email", userEmail)
            putExtra("user_password", userPassword)
        }
        startActivity(intent)
    }

    private fun validateAllFieldsFinal(): Boolean {
        validateAllFieldsInRealTime()

        if (!isGenderSelected) {
            Toast.makeText(this, "Выберите пол", Toast.LENGTH_SHORT).show()
            return false
        }

        return tilFirstName.error == null &&
                tilLastName.error == null &&
                tilBirthDate.error == null &&
                isGenderSelected
    }
}