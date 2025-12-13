package com.drivenext.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            // Handle login
        }
        
        findViewById<TextView>(R.id.tvForgotPassword).setOnClickListener {
            // Handle forgot password
        }
        
        findViewById<TextView>(R.id.tvRegister).setOnClickListener {
            startActivity(Intent(this, SignUp1Activity::class.java))
        }
    }
}

