package com.drivenext.app

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CongratulationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulations)
        
        findViewById<Button>(R.id.btnNext).setOnClickListener {
            // Navigate to main app
        }
    }
}

