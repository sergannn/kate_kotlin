
package com.example.drivenext

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class CarDetailsActivity : AppCompatActivity() {

    private lateinit var- ProgressBar: ProgressBar
    private lateinit var- errorView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Детали автомобиля"

        progressBar = findViewById(R.id.progressBar)
        errorView = findViewById(R.id.errorView)
        val bookButton: Button = findViewById(R.id.bookButton)
        val favoriteIcon: ImageView = findViewById(R.id.favoriteIcon)

        bookButton.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        favoriteIcon.setOnClickListener {
            // TODO: Handle favorite button click
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
        }

        // TODO: Load car details data
        showLoading()
        // hideLoading()
        // showError()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        // Hide other views
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        // Show other views
    }

    private fun showError() {
        errorView.visibility = View.VISIBLE
        // Hide other views
    }
}
