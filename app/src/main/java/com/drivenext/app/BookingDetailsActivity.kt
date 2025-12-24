package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
 * Экран детальной информации о бронировании
 */
class BookingDetailsActivity : AppCompatActivity() {

    private lateinit var bookingRepository: BookingRepository
    private lateinit var carRepository: CarRepository
    private var bookingId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        bookingId = intent.getStringExtra("booking_id")
        bookingRepository = BookingRepositoryImpl(applicationContext)
        carRepository = CarRepositoryImpl(applicationContext)

        setupClickListeners()
        loadBookingDetails()
    }

    private fun setupClickListeners() {
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.btnClose)?.setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.cancelButton)?.setOnClickListener {
            showCancelDialog()
        }

        // Bottom navigation
        findViewById<LinearLayout>(R.id.layoutHome)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.layoutBookmarks)?.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.layoutSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun showCancelDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.booking_cancel_dialog_title))
            .setMessage(getString(R.string.booking_cancel_dialog_message))
            .setPositiveButton(getString(R.string.booking_cancel_dialog_positive)) { _, _ ->
                cancelBooking()
            }
            .setNegativeButton(getString(R.string.booking_cancel_dialog_negative), null)
            .show()
    }

    private fun cancelBooking() {
        val id = bookingId ?: return

        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                bookingRepository.cancelBooking(id)
            }

            result.onSuccess {
                Toast.makeText(this@BookingDetailsActivity, "Бронирование отменено", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { error ->
                Toast.makeText(this@BookingDetailsActivity, "Ошибка отмены бронирования: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadBookingDetails() {
        val id = bookingId ?: return

        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                bookingRepository.getBookingById(id)
            }

            result.onSuccess { booking ->
                displayBooking(booking)
                // Load car details separately
                val carResult = withContext(Dispatchers.IO) {
                    carRepository.getCarById(booking.carId)
                }
                carResult.onSuccess { car ->
                    findViewById<TextView>(R.id.carNameTextView)?.text = "${car.brand} ${car.model}"
                    findViewById<TextView>(R.id.carBrandTextView)?.text = car.brand
                    findViewById<TextView>(R.id.carPriceTextView)?.text = "${car.pricePerDay} ₽/день"
                    findViewById<TextView>(R.id.locationTextView)?.text = car.address ?: ""
                    
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
            }.onFailure { error ->
                Toast.makeText(this@BookingDetailsActivity, "Ошибка загрузки бронирования: ${error.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun displayBooking(booking: com.drivenext.app.domain.model.Booking) {
        findViewById<TextView>(R.id.tvBookingTitle)?.text = "Бронирование # ${booking.id.takeLast(6)}"
        findViewById<TextView>(R.id.startDateTextView)?.text = booking.startDate
        findViewById<TextView>(R.id.endDateTextView)?.text = booking.endDate
        findViewById<TextView>(R.id.totalAmountValue)?.text = "${booking.totalPrice}₽"
        findViewById<TextView>(R.id.rentPriceTextView)?.text = "${booking.totalPrice / 3}₽/день" // Mock calculation
        findViewById<TextView>(R.id.insurancePriceTextView)?.text = "${booking.insurancePrice / 3}₽/день" // Mock calculation
        findViewById<TextView>(R.id.statusTextView)?.text = when (booking.status) {
            com.drivenext.app.domain.model.BookingStatus.PENDING -> "Ожидает"
            com.drivenext.app.domain.model.BookingStatus.APPROVED -> "Одобрено"
            com.drivenext.app.domain.model.BookingStatus.ACTIVE -> "Активно"
            com.drivenext.app.domain.model.BookingStatus.CANCELLED -> "Отменено"
            com.drivenext.app.domain.model.BookingStatus.COMPLETED -> "Завершено"
        }
        // TODO: Populate driver and license from user data
        findViewById<TextView>(R.id.driverTextView)?.text = "Иван Иванов" // Placeholder
        findViewById<TextView>(R.id.licenseTextView)?.text = "45164634" // Placeholder
    }
}

