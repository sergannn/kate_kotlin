package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.data.repository.CarRepository
import com.drivenext.app.data.repository.CarRepositoryImpl
import com.drivenext.app.domain.model.Car
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Экран детальной информации об автомобиле
 */
class CarDetailsActivity : AppCompatActivity() {

    private lateinit var carRepository: CarRepository
    private var carId: String? = null
    private lateinit var favoriteIcon: ImageView
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        carId = intent.getStringExtra("car_id")
        carRepository = CarRepositoryImpl(this)
        favoriteIcon = findViewById(R.id.favoriteIcon)
        
        // Инициализируем иконку как пустую по умолчанию
        updateFavoriteIcon()

        setupClickListeners()
        loadCarDetails()
    }

    private fun setupClickListeners() {
        findViewById<Button>(R.id.bookButton)?.setOnClickListener {
            carId?.let { id ->
                val intent = Intent(this, BookingActivity::class.java).apply {
                    putExtra("car_id", id)
                }
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "Ошибка: ID автомобиля не найден", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка назад в toolbar
        findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener {
            finish()
        }

        // Избранное
        favoriteIcon.setOnClickListener {
            toggleFavorite()
        }
    }

    private fun loadCarDetails() {
        val id = carId ?: return

        CoroutineScope(Dispatchers.Main).launch {
            findViewById<ProgressBar>(R.id.progressBar)?.visibility = android.view.View.VISIBLE

            val result = withContext(Dispatchers.IO) {
                carRepository.getCarById(id)
            }

            findViewById<ProgressBar>(R.id.progressBar)?.visibility = android.view.View.GONE

            result.onSuccess { car ->
                displayCar(car)
                checkFavoriteStatus()
            }.onFailure { error ->
                Toast.makeText(this@CarDetailsActivity, 
                    error.message ?: "Ошибка загрузки", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun checkFavoriteStatus() {
        val id = carId ?: return
        CoroutineScope(Dispatchers.Main).launch {
            val favorite = withContext(Dispatchers.IO) {
                carRepository.isFavorite(id)
            }
            isFavorite = favorite
            updateFavoriteIcon()
        }
    }
    
    private fun toggleFavorite() {
        val id = carId ?: return
        CoroutineScope(Dispatchers.Main).launch {
            val result = if (isFavorite) {
                withContext(Dispatchers.IO) {
                    carRepository.removeFromFavorites(id)
                }
            } else {
                withContext(Dispatchers.IO) {
                    carRepository.addToFavorites(id)
                }
            }
            
            result.onSuccess {
                isFavorite = !isFavorite
                updateFavoriteIcon()
                val message = if (isFavorite) "Добавлено в избранное" else "Удалено из избранного"
                Toast.makeText(this@CarDetailsActivity, message, Toast.LENGTH_SHORT).show()
            }.onFailure { error ->
                Toast.makeText(this@CarDetailsActivity, 
                    "Ошибка: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateFavoriteIcon() {
        // Если в избранном - сердечко заполнено (фиолетовое), иначе пустое (серое/полупрозрачное)
        if (isFavorite) {
            // В избранном - заполненное сердечко (фиолетовое)
            favoriteIcon.setColorFilter(getColor(R.color.brand_purple))
            favoriteIcon.alpha = 1.0f
        } else {
            // Не в избранном - пустое сердечко (серое)
            favoriteIcon.setColorFilter(getColor(R.color.text_gray))
            favoriteIcon.alpha = 0.6f
        }
    }

    private fun displayCar(car: Car) {
        // Используем ID, которые есть в layout
        findViewById<TextView>(R.id.carNameTextView)?.text = "${car.brand} ${car.model}"
        findViewById<TextView>(R.id.carPriceTextView)?.text = "${car.pricePerDay} ₽/день"
        findViewById<TextView>(R.id.descriptionTextView)?.text = car.description ?: ""
        findViewById<TextView>(R.id.locationTextView)?.text = car.address ?: "Адрес не указан"
        
        // Загрузка изображения в зависимости от бренда и модели
        val imageRes = when {
            car.brand.contains("Mercedes", ignoreCase = true) -> {
                when {
                    car.model.contains("GLE", ignoreCase = true) -> R.drawable.car_mercedes_gle350
                    car.model.contains("S 500", ignoreCase = true) || 
                    car.model.contains("S500", ignoreCase = true) ||
                    car.model.contains("Sedan", ignoreCase = true) -> R.drawable.car_iris_sedan
                    else -> R.drawable.car_mercedes_gle350
                }
            }
            else -> R.drawable.car_iris
        }
        findViewById<ImageView>(R.id.carImageView)?.setImageResource(imageRes)
    }
}

