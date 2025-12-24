package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drivenext.app.data.repository.CarRepository
import com.drivenext.app.data.repository.CarRepositoryImpl
import com.drivenext.app.domain.model.Car
import com.drivenext.app.presentation.adapter.CarAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Экран избранных автомобилей
 */
class FavoritesActivity : AppCompatActivity() {

    private lateinit var carRepository: CarRepository
    private lateinit var rvCars: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        carRepository = CarRepositoryImpl(this)
        rvCars = findViewById(R.id.rvCars)
        rvCars.layoutManager = LinearLayoutManager(this)

        setupClickListeners()
        loadFavorites()
    }
    
    private fun setupClickListeners() {
        findViewById<LinearLayout>(R.id.layoutHome)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        
        findViewById<LinearLayout>(R.id.layoutBookmarks)?.setOnClickListener {
            // Уже на экране избранного
        }
        
        findViewById<LinearLayout>(R.id.layoutSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun loadFavorites() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                carRepository.getFavoriteCars()
            }

            result.onSuccess { cars ->
                if (cars.isEmpty()) {
                    // Показать сообщение об отсутствии избранных
                } else {
                    showCars(cars)
                }
            }
        }
    }

    private fun showCars(cars: List<Car>) {
        val adapter = CarAdapter(
            cars = cars,
            onBookClick = { car ->
                val intent = Intent(this, BookingActivity::class.java).apply {
                    putExtra("car_id", car.id)
                }
                startActivity(intent)
            },
            onDetailsClick = { car ->
                val intent = Intent(this, CarDetailsActivity::class.java).apply {
                    putExtra("car_id", car.id)
                }
                startActivity(intent)
            }
        )
        rvCars.adapter = adapter
    }
}

