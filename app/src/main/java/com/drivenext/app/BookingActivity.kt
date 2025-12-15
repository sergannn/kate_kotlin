package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.data.repository.BookingRepository
import com.drivenext.app.data.repository.BookingRepositoryImpl
import com.drivenext.app.data.repository.CarRepository
import com.drivenext.app.data.repository.CarRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Экран оформления аренды автомобиля
 */
class BookingActivity : AppCompatActivity() {

    private lateinit var carRepository: CarRepository
    private lateinit var bookingRepository: BookingRepository
    private var carId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        carId = intent.getStringExtra("car_id")
        carRepository = CarRepositoryImpl()
        bookingRepository = BookingRepositoryImpl(this)

        setupClickListeners()
        loadCarInfo()
    }

    private fun setupClickListeners() {
        findViewById<Button>(R.id.continueButton)?.setOnClickListener {
            createBooking()
        }
    }
    
    private fun createBooking() {
        val id = carId ?: return
        
        CoroutineScope(Dispatchers.Main).launch {
            // Временные значения для демонстрации
            val startDate = "2024-09-27"
            val endDate = "2024-09-30"
            val totalPrice = 8400
            val insurancePrice = 900
            val deposit = 15000
            
            val result = withContext(Dispatchers.IO) {
                bookingRepository.createBooking(
                    carId = id,
                    startDate = startDate,
                    endDate = endDate,
                    totalPrice = totalPrice,
                    insurancePrice = insurancePrice,
                    deposit = deposit
                )
            }
            
            result.onSuccess { booking ->
                val intent = Intent(this@BookingActivity, BookingSuccessActivity::class.java).apply {
                    putExtra("booking_id", booking.id)
                }
                startActivity(intent)
                finish()
            }.onFailure { error ->
                // Показать ошибку
            }
        }
    }

    private fun loadCarInfo() {
        val id = carId ?: return

        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                carRepository.getCarById(id)
            }

            result.onSuccess { car ->
                findViewById<TextView>(R.id.carNameTextView)?.text = "${car.brand} ${car.model}"
                findViewById<TextView>(R.id.carPriceTextView)?.text = "${car.pricePerDay} ₽/день"
                findViewById<TextView>(R.id.locationTextView)?.text = car.address ?: ""
            }
        }
    }
}

