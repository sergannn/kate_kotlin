package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Экран успешного бронирования
 */
class BookingSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_success)

        findViewById<TextView>(R.id.myBookingsButton)?.setOnClickListener {
            startActivity(Intent(this, MyBookingsActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.homeButton)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Bottom navigation
        findViewById<LinearLayout>(R.id.layoutHome)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.layoutBookmarks)?.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.layoutSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}

