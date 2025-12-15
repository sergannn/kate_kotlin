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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        carId = intent.getStringExtra("car_id")
        carRepository = CarRepositoryImpl()

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

        // Избранное - элемент может отсутствовать в layout
        findViewById<ImageView>(R.id.favoriteIcon)?.setOnClickListener {
            // TODO: Добавить обработку избранного
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
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
            }.onFailure { error ->
                Toast.makeText(this@CarDetailsActivity, 
                    error.message ?: "Ошибка загрузки", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayCar(car: Car) {
        // Используем ID, которые есть в layout
        findViewById<TextView>(R.id.carNameTextView)?.text = "${car.brand} ${car.model}"
        findViewById<TextView>(R.id.carPriceTextView)?.text = "${car.pricePerDay} ₽/день"
        findViewById<TextView>(R.id.descriptionTextView)?.text = car.description ?: ""
        findViewById<TextView>(R.id.locationTextView)?.text = car.address ?: "Адрес не указан"
        
        // Элементы, которых может не быть в layout - используем безопасный доступ
        // TODO: Добавить эти элементы в layout при необходимости
    }
}

