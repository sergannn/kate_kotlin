
package com.example.drivenext

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyBookingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Мои бронирования"

        val recyclerView: RecyclerView = findViewById(R.id.bookingsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // TODO: Create and set adapter for the RecyclerView
        // Example of how to handle item click:
        // val adapter = BookingsAdapter { booking ->
        //     if (booking.isActive) {
        //         val intent = Intent(this, BookingDetailsActivity::class.java)
        //         intent.putExtra("bookingId", booking.id)
        //         startActivity(intent)
        //     }
        // }
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
