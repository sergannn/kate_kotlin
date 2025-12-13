package com.drivenext.app

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class NoConnectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)
        
        findViewById<Button>(R.id.btnRetry).setOnClickListener {
            // Handle retry
        }
    }
}

