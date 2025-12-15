package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drivenext.app.data.repository.AuthRepository
import com.drivenext.app.data.repository.AuthRepositoryImpl
import com.drivenext.app.data.repository.CarRepository
import com.drivenext.app.data.repository.CarRepositoryImpl
import com.drivenext.app.domain.model.Car
import com.drivenext.app.presentation.adapter.CarAdapter
import com.drivenext.app.presentation.viewmodel.MainViewModel
import com.drivenext.app.presentation.viewmodel.ViewModelFactory
import com.drivenext.app.utils.Prefs
import kotlinx.coroutines.launch

/**
 * Главный экран приложения
 * Отображает список доступных автомобилей и позволяет выполнять поиск
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var rvCars: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutError: LinearLayout
    private lateinit var etSearch: EditText
    private lateinit var btnRetry: Button
    private lateinit var layoutHome: LinearLayout
    private lateinit var layoutBookmarks: LinearLayout
    private lateinit var layoutSettings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Проверка авторизации
        val prefs = Prefs(this)
        val authRepository: AuthRepository = AuthRepositoryImpl(prefs)
        if (!authRepository.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        // Инициализация ViewModel
        val carRepository: CarRepository = CarRepositoryImpl()
        val factory = ViewModelFactory(authRepository, carRepository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        initViews()
        setupObservers()
        setupClickListeners()
    }

    private fun initViews() {
        rvCars = findViewById(R.id.rvCars)
        progressBar = findViewById(R.id.progressBar)
        layoutError = findViewById(R.id.layoutError)
        etSearch = findViewById(R.id.etSearch)
        btnRetry = findViewById(R.id.btnRetry)
        layoutHome = findViewById(R.id.layoutHome)
        layoutBookmarks = findViewById(R.id.layoutBookmarks)
        layoutSettings = findViewById(R.id.layoutSettings)

        rvCars.layoutManager = LinearLayoutManager(this)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Индикация загрузки
                if (state.isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }

                // Отображение ошибок
                state.error?.let { error ->
                    showError(error)
                }

                // Отображение списка автомобилей
                if (state.cars.isNotEmpty()) {
                    showCars(state.cars)
                }
            }
        }
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

        findViewById<Button>(R.id.btnSearch)?.setOnClickListener {
            performSearch()
        }

        btnRetry.setOnClickListener {
            viewModel.loadCars()
        }

        // Нижняя навигация
        layoutHome.setOnClickListener {
            // Уже на главной
        }

        layoutBookmarks.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        layoutSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        findViewById<Button>(R.id.btnViewAll)?.setOnClickListener {
            // Показать все автомобили (уже отображаются)
            Toast.makeText(this, "Все автомобили отображаются", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performSearch() {
        val query = etSearch.text.toString().trim()
        if (query.isEmpty()) {
            Toast.makeText(this, "Введите марку автомобиля", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.searchCars(query)
        
        // Переход на экран результатов поиска
        val intent = Intent(this, SearchResultsActivity::class.java).apply {
            putExtra("search_query", query)
        }
        startActivity(intent)
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
        rvCars.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showError(error: String) {
        layoutError.visibility = View.VISIBLE
        rvCars.visibility = View.GONE
    }

    private fun showCars(cars: List<Car>) {
        layoutError.visibility = View.GONE
        rvCars.visibility = View.VISIBLE

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
