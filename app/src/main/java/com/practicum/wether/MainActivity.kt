package com.practicum.wether

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageSettings = findViewById<ImageView>(R.id.imageSettings)
        imageSettings.setOnClickListener{
            val startIntent = Intent(this,Settings :: class.java)
            startActivity(startIntent)
        }
    }
}