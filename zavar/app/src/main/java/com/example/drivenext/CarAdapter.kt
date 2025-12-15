package com.example.drivenext.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenext.R
import com.example.drivenext.data.Car

class CarAdapter(
    private val cars: List<Car>,
    private val onBookClick: (Car) -> Unit,
    private val onDetailsClick: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCarTitle: TextView = itemView.findViewById(R.id.tvCarTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        val tvFuel: TextView = itemView.findViewById(R.id.tvFuel)
        val tvTransmission: TextView = itemView.findViewById(R.id.tvTransmission)
        val tvSeats: TextView = itemView.findViewById(R.id.tvSeats)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val btnBook: Button = itemView.findViewById(R.id.btnBook)
        val btnDetails: Button = itemView.findViewById(R.id.btnDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]

        holder.tvCarTitle.text = "${car.brand} ${car.model}"
        holder.tvYear.text = car.year.toString()
        holder.tvFuel.text = car.fuelType
        holder.tvTransmission.text = car.transmission
        holder.tvSeats.text = "${car.seats} мест"
        holder.tvPrice.text = "${car.pricePerDay} ₽/день"

        holder.btnBook.setOnClickListener {
            onBookClick(car)
        }

        holder.btnDetails.setOnClickListener {
            onDetailsClick(car)
        }
    }

    override fun getItemCount(): Int = cars.size
}