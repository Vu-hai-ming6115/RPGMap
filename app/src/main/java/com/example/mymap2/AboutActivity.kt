package com.example.mymap2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    private lateinit var btnBackAbout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        btnBackAbout = findViewById(R.id.btnBackAbout)
        btnBackAbout.setOnClickListener { finish() }
    }
}