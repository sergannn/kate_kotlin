package com.drivenext.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.utils.Prefs

/**
 * Экран добавления автомобиля - шаг 3 (фотографии)
 */
class AddCarStep3Activity : AppCompatActivity() {
    private lateinit var prefs: Prefs
    
    private lateinit var ivMainPhoto: ImageView
    private lateinit var ivPhoto1: ImageView
    private lateinit var ivPhoto2: ImageView
    private lateinit var ivPhoto3: ImageView
    private lateinit var ivPhoto4: ImageView
    
    private var mainPhotoUri: Uri? = null
    private var photo1Uri: Uri? = null
    private var photo2Uri: Uri? = null
    private var photo3Uri: Uri? = null
    private var photo4Uri: Uri? = null
    
    companion object {
        private const val REQUEST_CODE_MAIN_PHOTO = 2001
        private const val REQUEST_CODE_PHOTO1 = 2002
        private const val REQUEST_CODE_PHOTO2 = 2003
        private const val REQUEST_CODE_PHOTO3 = 2004
        private const val REQUEST_CODE_PHOTO4 = 2005
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car_step3)

        prefs = Prefs(this)

        initViews()
        setupClickListeners()
        loadSavedPhotos()
    }
    
    private fun initViews() {
        ivMainPhoto = findViewById(R.id.ivMainPhoto)
        ivPhoto1 = findViewById(R.id.ivPhoto1)
        ivPhoto2 = findViewById(R.id.ivPhoto2)
        ivPhoto3 = findViewById(R.id.ivPhoto3)
        ivPhoto4 = findViewById(R.id.ivPhoto4)
        
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnClose = findViewById<ImageView>(R.id.btnClose)

        btnBack?.setOnClickListener {
            finish()
        }

        btnClose?.setOnClickListener {
            finish()
        }

        btnNext?.setOnClickListener {
            if (validatePhotos()) {
                savePhotos()
                navigateToSuccess()
            }
        }
    }
    
    private fun setupClickListeners() {
        ivMainPhoto.setOnClickListener {
            openImagePicker(REQUEST_CODE_MAIN_PHOTO)
        }
        
        ivPhoto1.setOnClickListener {
            openImagePicker(REQUEST_CODE_PHOTO1)
        }
        
        ivPhoto2.setOnClickListener {
            openImagePicker(REQUEST_CODE_PHOTO2)
        }
        
        ivPhoto3.setOnClickListener {
            openImagePicker(REQUEST_CODE_PHOTO3)
        }
        
        ivPhoto4.setOnClickListener {
            openImagePicker(REQUEST_CODE_PHOTO4)
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
                REQUEST_CODE_MAIN_PHOTO -> {
                    selectedImageUri?.let {
                        mainPhotoUri = it
                        ivMainPhoto.setImageURI(it)
                    }
                }
                REQUEST_CODE_PHOTO1 -> {
                    selectedImageUri?.let {
                        photo1Uri = it
                        ivPhoto1.setImageURI(it)
                    }
                }
                REQUEST_CODE_PHOTO2 -> {
                    selectedImageUri?.let {
                        photo2Uri = it
                        ivPhoto2.setImageURI(it)
                    }
                }
                REQUEST_CODE_PHOTO3 -> {
                    selectedImageUri?.let {
                        photo3Uri = it
                        ivPhoto3.setImageURI(it)
                    }
                }
                REQUEST_CODE_PHOTO4 -> {
                    selectedImageUri?.let {
                        photo4Uri = it
                        ivPhoto4.setImageURI(it)
                    }
                }
            }
        }
    }
    
    private fun loadSavedPhotos() {
        val draft = prefs.getCarDraft()
        draft?.photoUris?.let { uris ->
            if (uris.isNotEmpty()) {
                try {
                    mainPhotoUri = Uri.parse(uris[0])
                    ivMainPhoto.setImageURI(mainPhotoUri)
                } catch (e: Exception) {
                    // Игнорируем ошибку
                }
            }
            if (uris.size > 1) {
                try {
                    photo1Uri = Uri.parse(uris[1])
                    ivPhoto1.setImageURI(photo1Uri)
                } catch (e: Exception) {
                    // Игнорируем ошибку
                }
            }
            if (uris.size > 2) {
                try {
                    photo2Uri = Uri.parse(uris[2])
                    ivPhoto2.setImageURI(photo2Uri)
                } catch (e: Exception) {
                    // Игнорируем ошибку
                }
            }
            if (uris.size > 3) {
                try {
                    photo3Uri = Uri.parse(uris[3])
                    ivPhoto3.setImageURI(photo3Uri)
                } catch (e: Exception) {
                    // Игнорируем ошибку
                }
            }
            if (uris.size > 4) {
                try {
                    photo4Uri = Uri.parse(uris[4])
                    ivPhoto4.setImageURI(photo4Uri)
                } catch (e: Exception) {
                    // Игнорируем ошибку
                }
            }
        }
    }
    
    private fun savePhotos() {
        val photoUris = mutableListOf<String>()
        mainPhotoUri?.let { photoUris.add(it.toString()) }
        photo1Uri?.let { photoUris.add(it.toString()) }
        photo2Uri?.let { photoUris.add(it.toString()) }
        photo3Uri?.let { photoUris.add(it.toString()) }
        photo4Uri?.let { photoUris.add(it.toString()) }
        
        val draft = prefs.getCarDraft() ?: Prefs.CarDraftData()
        prefs.saveCarDraft(draft.copy(photoUris = photoUris))
    }
    
    private fun validatePhotos(): Boolean {
        if (mainPhotoUri == null) {
            Toast.makeText(this, "Добавьте главное фото автомобиля", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    
    private fun navigateToSuccess() {
        val intent = Intent(this, AddCarSuccessActivity::class.java)
        startActivity(intent)
        finish()
    }
}
