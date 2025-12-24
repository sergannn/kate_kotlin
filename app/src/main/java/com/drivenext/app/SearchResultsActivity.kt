package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
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
 * Экран результатов поиска автомобилей
 */
class SearchResultsActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var rvCars: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutError: LinearLayout
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var btnRetry: Button
    private lateinit var btnBack: android.widget.ImageView
    private lateinit var etSearch: android.widget.EditText
    private lateinit var btnSearch: android.widget.Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        val prefs = Prefs(this)
        val authRepository: AuthRepository = AuthRepositoryImpl(prefs)
        val carRepository: CarRepository = CarRepositoryImpl(this)
        val factory = ViewModelFactory(authRepository, carRepository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        initViews()
        setupClickListeners()
        
        // Выполнить поиск по переданному запросу ДО подписки на изменения
        val query = intent.getStringExtra("search_query") ?: ""
        if (query.isNotEmpty()) {
            etSearch.setText(query)
            // Очищаем состояние перед поиском
            viewModel.searchCars(query)
        }
        
        // Подписываемся на изменения после инициализации поиска
        setupObservers()
    }

    private fun initViews() {
        rvCars = findViewById(R.id.rvSearchResults)
        progressBar = findViewById(R.id.progressBar)
        layoutError = findViewById(R.id.layoutError)
        layoutEmpty = findViewById(R.id.layoutEmpty)
        btnRetry = findViewById(R.id.btnRetry)
        btnBack = findViewById(R.id.btnBack)
        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)

        rvCars.layoutManager = LinearLayoutManager(this)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }

                state.error?.let { error ->
                    showError(error)
                } ?: run {
                    // Если нет ошибки, скрываем layoutError
                    layoutError.visibility = View.GONE
                }

                // Показываем результаты или пустое состояние
                if (!state.isLoading && state.error == null) {
                    if (state.cars.isNotEmpty()) {
                        showCars(state.cars)
                    } else {
                        showEmpty()
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnRetry.setOnClickListener {
            val query = etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchCars(query)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnSearch.setOnClickListener {
            performSearch()
        }

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
    }

    private fun performSearch() {
        val query = etSearch.text.toString().trim()
        if (query.isEmpty()) {
            android.widget.Toast.makeText(this, "Введите марку автомобиля", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.searchCars(query)
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.GONE
        rvCars.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showError(@Suppress("UNUSED_PARAMETER") error: String) {
        layoutError.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        rvCars.visibility = View.GONE
    }

    private fun showEmpty() {
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.VISIBLE
        rvCars.visibility = View.GONE
    }

    private fun showCars(cars: List<Car>) {
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.GONE
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

