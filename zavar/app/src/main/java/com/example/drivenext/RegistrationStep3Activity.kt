package com.example.drivenext

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class RegistrationStep3Activity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var currentImageType: String = ""

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var middleName: String
    private lateinit var birthDate: String
    private lateinit var gender: String
    private lateinit var userEmail: String
    private lateinit var userPassword: String

    private var profilePhotoUri: String = ""
    private lateinit var ivProfilePhoto: ImageView

    companion object {
        private const val PREFS_NAME = "drive_next_settings"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PASSWORD = "user_password"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_AVATAR = "user_avatar"
        private const val KEY_JOIN_DATE = "join_date"
        private const val KEY_USER_GENDER = "user_gender"
    }

    // Современный способ обработки выбора изображения
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImage: Uri? = result.data?.data
            selectedImage?.let { uri ->
                handleSelectedImage(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_step3)

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Получаем данные из предыдущих шагов
        firstName = intent.getStringExtra("first_name") ?: ""
        lastName = intent.getStringExtra("last_name") ?: ""
        middleName = intent.getStringExtra("middle_name") ?: ""
        birthDate = intent.getStringExtra("birth_date") ?: ""
        gender = intent.getStringExtra("gender") ?: "Мужской"
        userEmail = intent.getStringExtra("user_email") ?: ""
        userPassword = intent.getStringExtra("user_password") ?: ""

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto)
    }

    private fun setupClickListeners() {
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<MaterialButton>(R.id.btnAddProfilePhoto).setOnClickListener {
            currentImageType = "profile"
            openImagePicker()
        }

        findViewById<MaterialButton>(R.id.btnAddLicensePhoto).setOnClickListener {
            currentImageType = "license"
            openImagePicker()
        }

        findViewById<MaterialButton>(R.id.btnAddPassportPhoto).setOnClickListener {
            currentImageType = "passport"
            openImagePicker()
        }

        findViewById<MaterialButton>(R.id.btnNext).setOnClickListener {
            completeRegistration()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        pickImageLauncher.launch(intent)
    }

    private fun completeRegistration() {
        // Сохраняем все данные пользователя
        saveUserData()

        Toast.makeText(this, "Регистрация завершена!", Toast.LENGTH_SHORT).show()
        navigateToSuccess()
    }

    private fun saveUserData() {
        val fullName = if (middleName.isNotEmpty()) {
            "$firstName $middleName $lastName"
        } else {
            "$firstName $lastName"
        }

        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_EMAIL, userEmail)
            putString(KEY_USER_PASSWORD, userPassword)
            putString(KEY_USER_NAME, fullName)
            putString(KEY_USER_GENDER, gender)
            putString(KEY_USER_AVATAR, profilePhotoUri)
            putString(KEY_JOIN_DATE, "июле 2024")
            apply()
        }

        // Проверяем сохранение
        val savedAvatar = sharedPreferences.getString(KEY_USER_AVATAR, "")
        android.util.Log.d("RegistrationStep3", "Аватар сохранен: $savedAvatar")
        Toast.makeText(this, "Аватар сохранен: ${!savedAvatar.isNullOrEmpty()}", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToSuccess() {
        val intent = Intent(this, SuccessRegisterActivity::class.java).apply {
            putExtra("registered_email", userEmail)
            putExtra("registered_password", userPassword)
        }
        startActivity(intent)
        finish()
    }

    private fun handleSelectedImage(uri: Uri) {
        when (currentImageType) {
            "profile" -> {
                ivProfilePhoto.setImageURI(uri)
                profilePhotoUri = uri.toString()

                // СРАЗУ СОХРАНЯЕМ В SHAREDPREFERENCES
                sharedPreferences.edit()
                    .putString(KEY_USER_AVATAR, uri.toString())
                    .apply()

                // Проверяем сохранение
                val savedUri = sharedPreferences.getString(KEY_USER_AVATAR, "")
                android.util.Log.d("RegistrationStep3", "Фото профиля сохранено: $savedUri")

                Toast.makeText(this, "Фото профиля загружено и сохранено", Toast.LENGTH_SHORT).show()
            }
            "license" -> {
                Toast.makeText(this, "Фото водительского удостоверения загружено", Toast.LENGTH_SHORT).show()
            }
            "passport" -> {
                Toast.makeText(this, "Фото паспорта загружено", Toast.LENGTH_SHORT).show()
            }
        }
    }
}