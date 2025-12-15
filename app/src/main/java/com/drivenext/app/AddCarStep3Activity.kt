package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.utils.Prefs

/**
 * Экран добавления автомобиля - шаг 3 (фотографии)
 */
class AddCarStep3Activity : AppCompatActivity() {
    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car_step3)

        prefs = Prefs(this)

        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnClose = findViewById<ImageView>(R.id.btnClose)

        btnBack?.setOnClickListener {
            finish()
        }

        btnClose?.setOnClickListener {
            finish()
        }

        // TODO: Реализовать загрузку фотографий через ImagePicker
        // Пока что просто переходим к следующему экрану
        btnNext?.setOnClickListener {
            // TODO: Сохранить URI фотографий в draft
            // val photoUris = listOf(...)
            // val draft = prefs.getCarDraft() ?: Prefs.CarDraftData()
            // prefs.saveCarDraft(draft.copy(photoUris = photoUris))

            // Переход к экрану успеха
            val intent = Intent(this, AddCarSuccessActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

