
package com.example.drivenext

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Избранное"

        val recyclerView: RecyclerView = findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // TODO: Create and set adapter for the RecyclerView
        // The adapter should handle "Book" and "Details" button clicks
        // Example:
        // val adapter = FavoritesAdapter(
        //     onBookClick = { car ->
        //         val intent = Intent(this, BookingActivity::class.java)
        //         // pass car id or details
        //         startActivity(intent)
        //     },
        //     onDetailsClick = { car ->
        //         val intent = Intent(this, CarDetailsActivity::class.java)
        //         // pass car id or details
        //         startActivity(intent)
        //     }
        // )
        // recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
