package com.example.drivenext

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenext.utils.NetworkUtils

//NoConnectionActivity — экран отображается, если отсутствует интернет-соединение

class NoConnectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)

        val btnRetry = findViewById<Button>(R.id.btnRetry)

        btnRetry.setOnClickListener {
            if (NetworkUtils.isNetworkAvailable(this)) {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
        }
    }
}
