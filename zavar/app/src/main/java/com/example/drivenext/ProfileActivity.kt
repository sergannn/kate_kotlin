package com.example.drivenext

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.content.Context
import androidx.core.content.edit

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var avatarImageView: ImageView
    private lateinit var btnBack: ImageView

    companion object {
        private const val PREFS_NAME = "drive_next_settings"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_AVATAR = "user_avatar"
        private const val KEY_JOIN_DATE = "join_date"
        private const val KEY_USER_GENDER = "user_gender"
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImage: Uri? = result.data?.data
            selectedImage?.let { uri ->
                avatarImageView.setImageURI(uri)
                // Сохраняем новый аватар
                sharedPreferences.edit {
                    putString(KEY_USER_AVATAR, uri.toString())
                }
                Toast.makeText(this, "Аватар обновлен", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        initViews()
        setupClickListeners()
        loadUserData()
    }

    private fun initViews() {
        avatarImageView = findViewById(R.id.avatarImageView)
        btnBack = findViewById(R.id.btnBack)

        val titleTextView: TextView = findViewById(R.id.titleTextView)
        titleTextView.text = "Профиль"
    }

    private fun loadUserData() {
        val userName = sharedPreferences.getString(KEY_USER_NAME, "Иван Иванов") ?: "Иван Иванов"
        val userEmail = sharedPreferences.getString(KEY_USER_EMAIL, "kansv@mail.com") ?: "kansv@mail.com"
        val joinDate = sharedPreferences.getString(KEY_JOIN_DATE, "июле 2024") ?: "июле 2024"
        val gender = sharedPreferences.getString(KEY_USER_GENDER, "Мужской") ?: "Мужской"
        val avatarUri = sharedPreferences.getString(KEY_USER_AVATAR, "")

        // Устанавливаем аватар если есть, иначе оставляем дефолтный из XML
        if (!avatarUri.isNullOrEmpty()) {
            try {
                avatarImageView.setImageURI(Uri.parse(avatarUri))
                android.util.Log.d("ProfileActivity", "Аватар загружен из URI: $avatarUri")
            } catch (e: Exception) {
                android.util.Log.e("ProfileActivity", "Ошибка загрузки аватара", e)
                // В случае ошибки оставляем дефолтный аватар из XML
            }
        } else {
            android.util.Log.d("ProfileActivity", "Аватар не установлен, используется дефолтный")
            // Дефолтный аватар уже установлен в XML, ничего не меняем
        }

        findViewById<TextView>(R.id.userNameTextView).text = userName
        findViewById<TextView>(R.id.joinDateTextView).text = "Присоединился в $joinDate"
        findViewById<TextView>(R.id.emailTextView).text = userEmail
        findViewById<TextView>(R.id.genderTextView).text = gender

        // Google email можно оставить по умолчанию или убрать
        findViewById<TextView>(R.id.googleAccountTextView).text = userEmail
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        avatarImageView.setOnClickListener {
            openImagePicker()
        }

        findViewById<TextView>(R.id.changePasswordTextView).setOnClickListener {
            Toast.makeText(this, "Функция смены пароля в разработке", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            performLogout()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun performLogout() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Выход из профиля")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Выйти") { dialog, which ->
                // Сбрасываем только флаг авторизации, сохраняем данные
                sharedPreferences.edit {
                    putBoolean("is_logged_in", false)
                }

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}