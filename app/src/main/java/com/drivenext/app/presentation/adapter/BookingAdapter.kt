package com.drivenext.app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drivenext.app.R
import com.drivenext.app.domain.model.Booking
import com.drivenext.app.domain.model.BookingStatus
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Адаптер для отображения списка бронирований
 */
class BookingAdapter(
    private val bookings: List<Booking>,
    private val onBookingClick: (Booking) -> Unit
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount() = bookings.size

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        
        private val tvCarName: TextView = itemView.findViewById(R.id.carNameTextView)
        private val tvBookingDate: TextView = itemView.findViewById(R.id.bookingDateTextView)

        fun bind(booking: Booking) {
            // TODO: Загрузить информацию об автомобиле по booking.carId
            tvCarName.text = "Mercedes-Benz S 500 Sedan" // Временное значение
            
            val dateFormat = SimpleDateFormat("HH:mm, d MMMM yyyy", Locale("ru"))
            val startDate = try {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(booking.startDate)
            } catch (e: Exception) {
                null
            }
            
            val statusText = when (booking.status) {
                BookingStatus.PENDING -> {
                    startDate?.let { dateFormat.format(it) }?.let { 
                        "Начало аренды: 08:00, $it"
                    } ?: booking.startDate
                }
                BookingStatus.APPROVED -> {
                    startDate?.let { dateFormat.format(it) }?.let { 
                        "Начало аренды: 08:00, $it"
                    } ?: booking.startDate
                }
                BookingStatus.ACTIVE -> {
                    startDate?.let { dateFormat.format(it) }?.let { 
                        "Аренда активна, $it"
                    } ?: "Аренда активна"
                }
                BookingStatus.COMPLETED -> {
                    startDate?.let { dateFormat.format(it) }?.let { 
                        "Аренда завершена, $it"
                    } ?: "Аренда завершена"
                }
                BookingStatus.CANCELLED -> {
                    startDate?.let { dateFormat.format(it) }?.let { 
                        "Аренда отменена, $it"
                    } ?: "Аренда отменена"
                }
            }
            
            tvBookingDate.text = statusText
            
            itemView.setOnClickListener {
                onBookingClick(booking)
            }
        }
    }
}

