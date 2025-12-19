package com.drivenext.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.drivenext.app.utils.Prefs
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream

class SignUp3Activity : AppCompatActivity() {
    
    private lateinit var prefs: Prefs
    private lateinit var ivProfilePhoto: ImageView
    private lateinit var btnAddProfilePhoto: MaterialButton
    private lateinit var btnAddLicensePhoto: MaterialButton
    private lateinit var btnAddPassportPhoto: MaterialButton
    private lateinit var etLicenseNumber: TextInputEditText
    private lateinit var etLicenseIssueDate: TextInputEditText
    private lateinit var btnNext: MaterialButton
    
    private var profilePhotoUri: Uri? = null
    private var licensePhotoUri: Uri? = null
    private var passportPhotoUri: Uri? = null
    
    companion object {
        private const val REQUEST_CODE_PROFILE_PHOTO = 1001
        private const val REQUEST_CODE_LICENSE_PHOTO = 1002
        private const val REQUEST_CODE_PASSPORT_PHOTO = 1003
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_step3)
        
        prefs = Prefs(this)
        
        initViews()
        setupClickListeners()
        loadSavedData()
    }
    
    private fun initViews() {
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto)
        btnAddProfilePhoto = findViewById(R.id.btnAddProfilePhoto)
        btnAddLicensePhoto = findViewById(R.id.btnAddLicensePhoto)
        btnAddPassportPhoto = findViewById(R.id.btnAddPassportPhoto)
        etLicenseNumber = findViewById(R.id.etLicenseNumber)
        etLicenseIssueDate = findViewById(R.id.etLicenseIssueDate)
        btnNext = findViewById(R.id.btnNext)
    }
    
    private fun setupClickListeners() {
        btnAddProfilePhoto.setOnClickListener {
            openImagePicker(REQUEST_CODE_PROFILE_PHOTO)
        }
        
        btnAddLicensePhoto.setOnClickListener {
            openImagePicker(REQUEST_CODE_LICENSE_PHOTO)
        }
        
        btnAddPassportPhoto.setOnClickListener {
            openImagePicker(REQUEST_CODE_PASSPORT_PHOTO)
        }
        
        btnNext.setOnClickListener {
            if (validateFields()) {
                savePhotos()
                navigateToNext()
            }
        }
    }
    
    private fun openImagePicker(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, "Выберите фото"), requestCode)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            
            when (requestCode) {
                REQUEST_CODE_PROFILE_PHOTO -> {
                    selectedImageUri?.let {
                        profilePhotoUri = it
                        ivProfilePhoto.setImageURI(it)
                        btnAddProfilePhoto.text = "Изменить фото"
                    }
                }
                REQUEST_CODE_LICENSE_PHOTO -> {
                    selectedImageUri?.let {
                        licensePhotoUri = it
                        btnAddLicensePhoto.text = "Фото загружено"
                    }
                }
                REQUEST_CODE_PASSPORT_PHOTO -> {
                    selectedImageUri?.let {
                        passportPhotoUri = it
                        btnAddPassportPhoto.text = "Фото загружено"
                    }
                }
            }
        }
    }
    
    private fun loadSavedData() {
        // Загружаем текстовые данные
        prefs.registrationLicenseNumber?.let {
            etLicenseNumber.setText(it)
        }
        prefs.registrationLicenseIssueDate?.let {
            etLicenseIssueDate.setText(it)
        }
        
        // Загружаем фото
        prefs.profilePhotoUri?.let {
            try {
                val uri = Uri.parse(it)
                ivProfilePhoto.setImageURI(uri)
                profilePhotoUri = uri
                btnAddProfilePhoto.text = "Изменить фото"
            } catch (e: Exception) {
                // Игнорируем ошибку
            }
        }
        
        prefs.licensePhotoUri?.let {
            try {
                licensePhotoUri = Uri.parse(it)
                btnAddLicensePhoto.text = "Фото загружено"
            } catch (e: Exception) {
                // Игнорируем ошибку
            }
        }
        
        prefs.passportPhotoUri?.let {
            try {
                passportPhotoUri = Uri.parse(it)
                btnAddPassportPhoto.text = "Фото загружено"
            } catch (e: Exception) {
                // Игнорируем ошибку
            }
        }
    }
    
    private fun savePhotos() {
        val licenseNumber = etLicenseNumber.text.toString().trim()
        val issueDate = etLicenseIssueDate.text.toString().trim()
        
        prefs.registrationLicenseNumber = licenseNumber
        prefs.registrationLicenseIssueDate = issueDate
        
        profilePhotoUri?.let {
            prefs.profilePhotoUri = it.toString()
        }
        licensePhotoUri?.let {
            prefs.licensePhotoUri = it.toString()
        }
        passportPhotoUri?.let {
            prefs.passportPhotoUri = it.toString()
        }
    }
    
    private fun validateFields(): Boolean {
        val licenseNumber = etLicenseNumber.text.toString().trim()
        val issueDate = etLicenseIssueDate.text.toString().trim()
        
        if (licenseNumber.isEmpty()) {
            Toast.makeText(this, "Введите номер водительского удостоверения", Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (issueDate.isEmpty()) {
            Toast.makeText(this, "Введите дату выдачи", Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (profilePhotoUri == null) {
            Toast.makeText(this, "Добавьте фото профиля", Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (licensePhotoUri == null) {
            Toast.makeText(this, "Загрузите фото водительского удостоверения", Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (passportPhotoUri == null) {
            Toast.makeText(this, "Загрузите фото паспорта", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return true
    }
    
    private fun navigateToNext() {
        // Очищаем данные регистрации после успешного завершения
        prefs.clearRegistrationData()
        
        val intent = Intent(this, CongratulationsActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
