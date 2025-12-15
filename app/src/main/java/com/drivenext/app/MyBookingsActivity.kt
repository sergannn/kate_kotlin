package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drivenext.app.data.repository.BookingRepository
import com.drivenext.app.data.repository.BookingRepositoryImpl
import com.drivenext.app.domain.model.Booking
import com.drivenext.app.presentation.adapter.BookingAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Экран списка бронирований пользователя
 */
class MyBookingsActivity : AppCompatActivity() {

    private lateinit var bookingRepository: BookingRepository
    private lateinit var rvBookings: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        bookingRepository = BookingRepositoryImpl(this)
        rvBookings = findViewById(R.id.bookingsRecyclerView)
        rvBookings.layoutManager = LinearLayoutManager(this)

        setupClickListeners()
        loadBookings()
    }
    
    private fun setupClickListeners() {
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener {
            finish()
        }
        
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

    private fun loadBookings() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                bookingRepository.getUserBookings()
            }

            result.onSuccess { bookings ->
                if (bookings.isEmpty()) {
                    // Показать сообщение об отсутствии бронирований
                } else {
                    showBookings(bookings)
                }
            }.onFailure { error ->
                // Показать ошибку
            }
        }
    }
    
    private fun showBookings(bookings: List<Booking>) {
        val adapter = BookingAdapter(bookings) { booking ->
            val intent = Intent(this, BookingDetailsActivity::class.java).apply {
                putExtra("booking_id", booking.id)
            }
            startActivity(intent)
        }
        rvBookings.adapter = adapter
    }
}

