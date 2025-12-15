package com.example.drivenext

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenext.data.Car
import com.example.drivenext.ui.CarAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rvCars: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutError: LinearLayout
    private lateinit var etSearch: EditText
    private lateinit var btnRetry: Button

    // ✅ ДОБАВЛЕНО: Объявляем переменные для навигации
    private lateinit var layoutHome: LinearLayout
    private lateinit var layoutBookmarks: LinearLayout
    private lateinit var layoutSettings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ПРОВЕРКА АВТОРИЗАЦИИ
        sharedPreferences = getSharedPreferences("drive_next_settings", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        initViews()
        setupClickListeners()
        loadCars()
    }

    private fun initViews() {
        rvCars = findViewById(R.id.rvCars)
        progressBar = findViewById(R.id.progressBar)
        layoutError = findViewById(R.id.layoutError)
        etSearch = findViewById(R.id.etSearch)
        btnRetry = findViewById(R.id.btnRetry)

        // ✅ ДОБАВЛЕНО: Инициализация навигации
        layoutHome = findViewById(R.id.layoutHome)
        layoutBookmarks = findViewById(R.id.layoutBookmarks)
        layoutSettings = findViewById(R.id.layoutSettings)

        // Настройка RecyclerView
        rvCars.layoutManager = LinearLayoutManager(this)
    }

    private fun setupClickListeners() {
        // Поиск
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        // Кнопка поиска
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            performSearch()
        }

        // Повторная загрузка
        btnRetry.setOnClickListener {
            loadCars()
        }

        // ✅ ИСПРАВЛЕНО: Нижняя навигация с объявленными переменными
        layoutHome.setOnClickListener {
            // Уже на главной
            Toast.makeText(this, "Вы уже на главной странице", Toast.LENGTH_SHORT).show()
        }

        layoutBookmarks.setOnClickListener {
            Toast.makeText(this, "Закладки", Toast.LENGTH_SHORT).show()
        }

        layoutSettings.setOnClickListener {
            // ✅ ПЕРЕХОД В НАСТРОЙКИ
            try {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Переход в настройки", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Ошибка перехода: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private fun loadCars() {
        showLoading()

        // Имитация загрузки с сервера
        Handler(Looper.getMainLooper()).postDelayed({
            showCars(mockCars)
        }, 1500)
    }

    private fun performSearch() {
        val query = etSearch.text.toString().trim()
        if (query.isEmpty()) {
            Toast.makeText(this, "Введите марку автомобиля", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading()

        // Имитация поиска
        Handler(Looper.getMainLooper()).postDelayed({
            val filteredCars = mockCars.filter {
                it.brand.contains(query, ignoreCase = true)
            }

            if (filteredCars.isEmpty()) {
                Toast.makeText(this, "Не удалось выполнить поиск. Попробуйте снова.", Toast.LENGTH_SHORT).show()
                showCars(mockCars)
            } else {
                // переход на экран результатов поиска
                val intent = Intent(this, SearchResultsActivity::class.java).apply {
                    putExtra("search_query", query)
                }
                startActivity(intent)
            }
        }, 1000)
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
        rvCars.visibility = View.GONE
    }

    private fun showCars(cars: List<Car>) {
        progressBar.visibility = View.GONE
        layoutError.visibility = View.GONE
        rvCars.visibility = View.VISIBLE

        val adapter = CarAdapter(
            cars = cars,
            onBookClick = { car ->
                Toast.makeText(this, "Бронирование: ${car.brand} ${car.model}", Toast.LENGTH_SHORT).show()
            },
            onDetailsClick = { car ->
                Toast.makeText(this, "Детали: ${car.brand} ${car.model}", Toast.LENGTH_SHORT).show()
            }
        )
        rvCars.adapter = adapter
    }

    // Временные данные для демонстрации
    private val mockCars = listOf(
        Car(
            id = "1",
            brand = "Toyota",
            model = "Camry",
            year = 2023,
            pricePerDay = 2500,
            fuelType = "Бензин",
            transmission = "Автомат",
            seats = 5,
            imageUrl = ""
        ),
        Car(
            id = "2",
            brand = "BMW",
            model = "X5",
            year = 2022,
            pricePerDay = 4500,
            fuelType = "Дизель",
            transmission = "Автомат",
            seats = 5,
            imageUrl = ""
        ),
        Car(
            id = "3",
            brand = "Hyundai",
            model = "Solaris",
            year = 2023,
            pricePerDay = 1800,
            fuelType = "Бензин",
            transmission = "Механика",
            seats = 5,
            imageUrl = ""
        ),
        Car(
            id = "4",
            brand = "Mercedes",
            model = "E-Class",
            year = 2023,
            pricePerDay = 5200,
            fuelType = "Бензин",
            transmission = "Автомат",
            seats = 5,
            imageUrl = ""
        )
    )

    private fun showError() {
        progressBar.visibility = View.GONE
        layoutError.visibility = View.VISIBLE
        rvCars.visibility = View.GONE
    }
}