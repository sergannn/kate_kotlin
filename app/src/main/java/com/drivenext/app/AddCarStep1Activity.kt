package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.utils.Prefs
import com.google.android.material.textfield.TextInputEditText

/**
 * Экран добавления автомобиля - шаг 1 (адрес)
 */
class AddCarStep1Activity : AppCompatActivity() {
    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car_step1)

        prefs = Prefs(this)

        val etAddress = findViewById<TextInputEditText>(R.id.etAddress)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // Загрузить сохраненные данные
        prefs.getCarDraft()?.address?.let {
            etAddress?.setText(it)
        }

        btnBack?.setOnClickListener {
            finish()
        }

        // Кнопка становится активной после ввода адреса
        etAddress?.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                btnNext?.isEnabled = !s.isNullOrBlank()
                if (btnNext != null) {
                    btnNext.alpha = if (s.isNullOrBlank()) 0.2f else 1.0f
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnNext?.setOnClickListener {
            val address = etAddress?.text.toString().trim()
            if (address.isNotEmpty()) {
                // Сохранить адрес в draft
                val draft = prefs.getCarDraft() ?: Prefs.CarDraftData()
                prefs.saveCarDraft(draft.copy(address = address))

                val intent = Intent(this, AddCarStep2Activity::class.java)
                startActivity(intent)
            }
        }
    }
}

