package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.drivenext.app.utils.Prefs
import com.google.android.material.textfield.TextInputEditText

/**
 * Экран добавления автомобиля - шаг 2 (информация об автомобиле)
 */
class AddCarStep2Activity : AppCompatActivity() {
    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car_step2)

        prefs = Prefs(this)

        val etYear = findViewById<TextInputEditText>(R.id.etYear)
        val etBrand = findViewById<TextInputEditText>(R.id.etBrand)
        val etModel = findViewById<TextInputEditText>(R.id.etModel)
        val etTransmission = findViewById<TextInputEditText>(R.id.etTransmission)
        val etMileage = findViewById<TextInputEditText>(R.id.etMileage)
        val etDescription = findViewById<TextInputEditText>(R.id.etDescription)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // Загрузить сохраненные данные
        prefs.getCarDraft()?.let { draft ->
            draft.year?.let { etYear?.setText(it) }
            draft.brand?.let { etBrand?.setText(it) }
            draft.model?.let { etModel?.setText(it) }
            draft.transmission?.let { etTransmission?.setText(it) }
            draft.mileage?.let { etMileage?.setText(it) }
            draft.description?.let { etDescription?.setText(it) }
        }

        btnBack?.setOnClickListener {
            finish()
        }

        // Валидация полей
        val textWatcher = object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                validateFields()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        etYear?.addTextChangedListener(textWatcher)
        etBrand?.addTextChangedListener(textWatcher)
        etModel?.addTextChangedListener(textWatcher)
        etTransmission?.addTextChangedListener(textWatcher)
        etMileage?.addTextChangedListener(textWatcher)

        btnSubmit?.setOnClickListener {
            val year = etYear?.text.toString().trim()
            val brand = etBrand?.text.toString().trim()
            val model = etModel?.text.toString().trim()
            val transmission = etTransmission?.text.toString().trim()
            val mileage = etMileage?.text.toString().trim()
            val description = etDescription?.text.toString().trim()

            if (year.isNotEmpty() && brand.isNotEmpty() && model.isNotEmpty() && transmission.isNotEmpty() && mileage.isNotEmpty()) {
                // Сохранить данные в draft
                val draft = prefs.getCarDraft() ?: Prefs.CarDraftData()
                prefs.saveCarDraft(draft.copy(
                    year = year,
                    brand = brand,
                    model = model,
                    transmission = transmission,
                    mileage = mileage,
                    description = description
                ))

                val intent = Intent(this, AddCarStep3Activity::class.java)
                startActivity(intent)
            }
        }

        validateFields()
    }

    private fun validateFields() {
        val etYear = findViewById<TextInputEditText>(R.id.etYear)
        val etBrand = findViewById<TextInputEditText>(R.id.etBrand)
        val etModel = findViewById<TextInputEditText>(R.id.etModel)
        val etTransmission = findViewById<TextInputEditText>(R.id.etTransmission)
        val etMileage = findViewById<TextInputEditText>(R.id.etMileage)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        val isValid = !etYear?.text.isNullOrBlank() &&
                !etBrand?.text.isNullOrBlank() &&
                !etModel?.text.isNullOrBlank() &&
                !etTransmission?.text.isNullOrBlank() &&
                !etMileage?.text.isNullOrBlank()

        btnSubmit?.isEnabled = isValid
        btnSubmit?.alpha = if (isValid) 1.0f else 0.2f
    }
}

