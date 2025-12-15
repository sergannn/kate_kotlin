package com.example.drivenext

import android.content.Intent
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

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var rvSearchResults: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutError: LinearLayout
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var tvSearchQuery: TextView
    private lateinit var btnRetry: Button
    private lateinit var btnBack: ImageView
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button

    private var searchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        // Получаем поисковый запрос из Intent
        searchQuery = intent.getStringExtra("search_query") ?: ""

        initViews()
        setupClickListeners()
        performSearch(searchQuery)
    }

    private fun initViews() {
        rvSearchResults = findViewById(R.id.rvSearchResults)
        progressBar = findViewById(R.id.progressBar)
        layoutError = findViewById(R.id.layoutError)
        layoutEmpty = findViewById(R.id.layoutEmpty)
        tvSearchQuery = findViewById(R.id.tvSearchQuery)
        btnRetry = findViewById(R.id.btnRetry)
        btnBack = findViewById(R.id.btnBack)
        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)

        // Настройка RecyclerView
        rvSearchResults.layoutManager = LinearLayoutManager(this)

        // Отображаем поисковый запрос
        tvSearchQuery.text = "Результаты по запросу: \"$searchQuery\""
        etSearch.setText(searchQuery) // Устанавливаем текст в поисковую строку
    }

    private fun setupClickListeners() {
        // Кнопка назад
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Повторная загрузка
        btnRetry.setOnClickListener {
            performSearch(searchQuery)
        }

        // Кнопка поиска
        btnSearch.setOnClickListener {
            val newQuery = etSearch.text.toString().trim()
            if (newQuery.isNotEmpty()) {
                searchQuery = newQuery
                tvSearchQuery.text = "Результаты по запросу: \"$searchQuery\""
                performSearch(searchQuery)
            } else {
                Toast.makeText(this, "Введите поисковый запрос", Toast.LENGTH_SHORT).show()
            }
        }

        // Поиск при нажатии Enter
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                val newQuery = etSearch.text.toString().trim()
                if (newQuery.isNotEmpty()) {
                    searchQuery = newQuery
                    tvSearchQuery.text = "Результаты по запросу: \"$searchQuery\""
                    performSearch(searchQuery)
                } else {
                    Toast.makeText(this, "Введите поисковый запрос", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }

        // Нижняя навигация
        findViewById<LinearLayout>(R.id.layoutHome).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.layoutBookmarks).setOnClickListener {
            Toast.makeText(this, "Закладки", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.layoutSettings).setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) {
            showEmptyResults()
            tvSearchQuery.text = "Введите поисковый запрос"
            return
        }

        showLoading()

        // Имитация поиска с сервера
        Handler(Looper.getMainLooper()).postDelayed({
            // Временные данные для демонстрации
            val allCars = listOf(
                Car("1", "Toyota", "Camry", 2023, 2500, "Бензин", "Автомат", 5, ""),
                Car("2", "Toyota", "Corolla", 2022, 2200, "Бензин", "Автомат", 5, ""),
                Car("3", "BMW", "X5", 2022, 4500, "Дизель", "Автомат", 5, ""),
                Car("4", "BMW", "X3", 2023, 3800, "Бензин", "Автомат", 5, ""),
                Car("5", "Hyundai", "Solaris", 2023, 1800, "Бензин", "Механика", 5, ""),
                Car("6", "Mercedes", "E-Class", 2023, 5200, "Бензин", "Автомат", 5, "")
            )

            // ПРАВИЛЬНАЯ ФИЛЬТРАЦИЯ: ищем по марке и модели
            val searchResults = allCars.filter { car ->
                car.brand.contains(query, ignoreCase = true) ||
                        car.model.contains(query, ignoreCase = true)
            }

            // ПРАВИЛЬНАЯ ПРОВЕРКА РЕЗУЛЬТАТОВ
            if (searchResults.isEmpty()) {
                showEmptyResults()
                Toast.makeText(this, "По запросу \"$query\" ничего не найдено", Toast.LENGTH_LONG).show()
            } else {
                showSearchResults(searchResults)
                Toast.makeText(this, "Найдено ${searchResults.size} автомобилей", Toast.LENGTH_SHORT).show()
            }
        }, 1500)
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.GONE
        rvSearchResults.visibility = View.GONE
    }

    private fun showSearchResults(cars: List<Car>) {
        progressBar.visibility = View.GONE
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.GONE
        rvSearchResults.visibility = View.VISIBLE

        val adapter = CarAdapter(
            cars = cars,
            onBookClick = { car ->
                // Переход на экран оформления аренды
                Toast.makeText(this, "Бронирование: ${car.brand} ${car.model}", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, BookingActivity::class.java).apply {
                //     putExtra("car_id", car.id)
                // })
            },
            onDetailsClick = { car ->
                // Переход на экран деталей
                Toast.makeText(this, "Детали: ${car.brand} ${car.model}", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, CarDetailsActivity::class.java).apply {
                //     putExtra("car_id", car.id)
                // })
            }
        )
        rvSearchResults.adapter = adapter
    }

    private fun showEmptyResults() {
        progressBar.visibility = View.GONE
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.VISIBLE
        rvSearchResults.visibility = View.GONE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        layoutError.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        rvSearchResults.visibility = View.GONE
        Toast.makeText(this, "Не удалось выполнить поиск. Попробуйте снова.", Toast.LENGTH_SHORT).show()
    }
}