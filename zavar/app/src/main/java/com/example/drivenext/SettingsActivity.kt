package com.example.drivenext

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    // Объявляем переменные для View
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var layoutProfile: LinearLayout
    private lateinit var layoutMyBookings: LinearLayout
    private lateinit var layoutTheme: LinearLayout
    private lateinit var layoutNotifications: LinearLayout
    private lateinit var layoutAddCar: LinearLayout
    private lateinit var layoutHelp: LinearLayout
    private lateinit var layoutInviteFriend: LinearLayout
    private lateinit var layoutHome: LinearLayout
    private lateinit var layoutBookmarks: LinearLayout
    private lateinit var layoutSettings: LinearLayout
    private lateinit var ivAvatar: ImageView

    companion object {
        private const val PREFS_NAME = "drive_next_settings"
        private const val KEY_THEME = "theme"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_AVATAR = "user_avatar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        initViews()
        setupClickListeners()
        loadAndUpdateUI()
    }

    private fun initViews() {
        tvUserName = findViewById(R.id.tvUserName)
        tvUserEmail = findViewById(R.id.tvUserEmail)
        layoutProfile = findViewById(R.id.layoutProfile)
        layoutMyBookings = findViewById(R.id.layoutMyBookings)
        layoutTheme = findViewById(R.id.layoutTheme)
        layoutNotifications = findViewById(R.id.layoutNotifications)
        layoutAddCar = findViewById(R.id.layoutAddCar)
        layoutHelp = findViewById(R.id.layoutHelp)
        layoutInviteFriend = findViewById(R.id.layoutInviteFriend)
        layoutHome = findViewById(R.id.layoutHome)
        layoutBookmarks = findViewById(R.id.layoutBookmarks)
        layoutSettings = findViewById(R.id.layoutSettings)
        ivAvatar = findViewById(R.id.ivAvatar)
    }

    private fun loadAndUpdateUI() {
        val userName = sharedPreferences.getString(KEY_USER_NAME, "Иван Иванов") ?: "Иван Иванов"
        val userEmail = sharedPreferences.getString(KEY_USER_EMAIL, "user@example.com") ?: "user@example.com"
        val avatarUri = sharedPreferences.getString(KEY_USER_AVATAR, "")

        tvUserName.text = userName
        tvUserEmail.text = userEmail

        // Загружаем аватар если есть
        if (!avatarUri.isNullOrEmpty()) {
            try {
                ivAvatar.setImageURI(android.net.Uri.parse(avatarUri))
            } catch (e: Exception) {
                // В случае ошибки оставляем дефолтную аватарку
                android.util.Log.e("SettingsActivity", "Ошибка загрузки аватара", e)
            }
        }
    }

    private fun setupClickListeners() {
        // 1. Профиль - ИСПРАВЛЕН ПЕРЕХОД
        layoutProfile.setOnClickListener {
            // Проверяем авторизацию перед переходом
            val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
            if (isLoggedIn) {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Сначала войдите в аккаунт", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        // 2. Мои бронирования
        layoutMyBookings.setOnClickListener {
            Toast.makeText(this, "Мои бронирования", Toast.LENGTH_SHORT).show()
        }

        // 3. Тема
        layoutTheme.setOnClickListener {
            showThemeDialog()
        }

        // 4. Уведомления
        layoutNotifications.setOnClickListener {
            Toast.makeText(this, "Настройки уведомлений", Toast.LENGTH_SHORT).show()
        }

        // 5. Подключить автомобиль
        layoutAddCar.setOnClickListener {
            Toast.makeText(this, "Добавление автомобиля", Toast.LENGTH_SHORT).show()
        }

        // 6. Помощь
        layoutHelp.setOnClickListener {
            Toast.makeText(this, "Раздел помощи", Toast.LENGTH_SHORT).show()
        }

        // 7. Пригласи друга
        layoutInviteFriend.setOnClickListener {
            shareApp()
        }

        // Нижняя навигация
        layoutHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layoutBookmarks.setOnClickListener {
            Toast.makeText(this, "Закладки", Toast.LENGTH_SHORT).show()
        }

        layoutSettings.setOnClickListener {
            // Уже в настройках
        }
    }

    private fun showThemeDialog() {
        val themes = arrayOf("Светлая", "Темная", "Авто")
        val themeValues = arrayOf("light", "dark", "auto")

        val currentTheme = sharedPreferences.getString(KEY_THEME, "light") ?: "light"
        val currentIndex = themeValues.indexOf(currentTheme)

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Выберите тему")
            .setSingleChoiceItems(themes, currentIndex) { dialog, which ->
                val selectedTheme = themeValues[which]
                sharedPreferences.edit().putString(KEY_THEME, selectedTheme).apply()
                applyTheme(selectedTheme)
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun applyTheme(theme: String) {
        Toast.makeText(this, "Тема изменена на: ${getThemeName(theme)}", Toast.LENGTH_SHORT).show()
    }

    private fun getThemeName(theme: String): String {
        return when (theme) {
            "light" -> "Светлая"
            "dark" -> "Темная"
            "auto" -> "Авто"
            else -> "Светлая"
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Присоединяйся к DriveNext! Скачай приложение по ссылке: ...")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Приглашение в DriveNext")
        startActivity(Intent.createChooser(shareIntent, "Поделиться приложением"))
    }

    override fun onResume() {
        super.onResume()
        // Обновляем данные при возвращении на экран
        loadAndUpdateUI()
    }
}