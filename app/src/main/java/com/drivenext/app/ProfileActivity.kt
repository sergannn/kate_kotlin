package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.data.repository.AuthRepository
import com.drivenext.app.data.repository.AuthRepositoryImpl
import com.drivenext.app.utils.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Экран профиля пользователя
 */
class ProfileActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val prefs = Prefs(this)
        authRepository = AuthRepositoryImpl(prefs)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Изменение аватара
        findViewById<ImageView>(R.id.avatarImageView)?.setOnClickListener {
            // TODO: Открыть галерею/камеру для выбора фото
        }

        // Выход из профиля
        findViewById<Button>(R.id.logoutButton)?.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выход")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Да") { _, _ ->
                logout()
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun logout() {
        CoroutineScope(Dispatchers.Main).launch {
            authRepository.logout()
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}

