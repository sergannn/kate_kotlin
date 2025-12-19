package com.drivenext.app

import android.content.Intent
import android.net.Uri
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
    private lateinit var prefs: Prefs
    private lateinit var avatarImageView: ImageView
    
    companion object {
        private const val REQUEST_CODE_AVATAR = 3001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        prefs = Prefs(this)
        authRepository = AuthRepositoryImpl(prefs)

        initViews()
        setupClickListeners()
        loadAvatar()
    }
    
    private fun initViews() {
        avatarImageView = findViewById(R.id.avatarImageView)
    }
    
    private fun setupClickListeners() {
        // Изменение аватара
        avatarImageView.setOnClickListener {
            openImagePicker()
        }

        // Выход из профиля
        findViewById<Button>(R.id.logoutButton)?.setOnClickListener {
            showLogoutDialog()
        }
    }
    
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, "Выберите фото"), REQUEST_CODE_AVATAR)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == REQUEST_CODE_AVATAR && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                avatarImageView.setImageURI(it)
                prefs.profilePhotoUri = it.toString()
            }
        }
    }
    
    private fun loadAvatar() {
        prefs.profilePhotoUri?.let {
            try {
                val uri = Uri.parse(it)
                avatarImageView.setImageURI(uri)
            } catch (e: Exception) {
                // Игнорируем ошибку
            }
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
