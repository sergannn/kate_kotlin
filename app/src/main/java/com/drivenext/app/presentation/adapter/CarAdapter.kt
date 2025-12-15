package com.drivenext.app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drivenext.app.R
import com.drivenext.app.domain.model.Car

/**
 * Адаптер для отображения списка автомобилей
 */
class CarAdapter(
    private val cars: List<Car>,
    private val onBookClick: (Car) -> Unit,
    private val onDetailsClick: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    override fun getItemCount() = cars.size

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCarTitle: TextView = itemView.findViewById(R.id.tvCarTitle)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        private val tvFuel: TextView = itemView.findViewById(R.id.tvFuel)
        private val tvTransmission: TextView = itemView.findViewById(R.id.tvTransmission)
        private val tvSeats: TextView = itemView.findViewById(R.id.tvSeats)
        private val ivCarImage: ImageView = itemView.findViewById(R.id.ivCarImage)
        private val btnBook: Button = itemView.findViewById(R.id.btnBook)
        private val btnDetails: Button = itemView.findViewById(R.id.btnDetails)

        fun bind(car: Car) {
            tvCarTitle.text = "${car.brand} ${car.model}"
            tvPrice.text = "${car.pricePerDay} ₽/день"
            tvYear.text = car.year.toString()
            tvFuel.text = car.fuelType
            tvTransmission.text = car.transmission
            tvSeats.text = "${car.seats} мест"
            
            // TODO: Загрузка изображения через Glide/Picasso
            
            btnBook.setOnClickListener { onBookClick(car) }
            btnDetails.setOnClickListener { onDetailsClick(car) }
        }
    }
}

