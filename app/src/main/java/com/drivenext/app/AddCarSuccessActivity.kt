package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.utils.Prefs

/**
 * Экран успешного добавления автомобиля
 */
class AddCarSuccessActivity : AppCompatActivity() {
    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car_success)

        prefs = Prefs(this)

        val btnHome = findViewById<Button>(R.id.btnHome)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack?.setOnClickListener {
            finish()
        }

        btnHome?.setOnClickListener {
            // Очистить draft после успешного добавления
            prefs.clearCarDraft()

            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }
    }
}

