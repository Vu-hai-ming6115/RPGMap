package com.example.mymap2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var btnStart: Button
    private lateinit var btnGuide: Button   // ← NÚT MỚI
    private lateinit var btnAbout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTitle    = findViewById(R.id.tvTitle)
        tvSubtitle = findViewById(R.id.tvSubtitle)
        btnStart   = findViewById(R.id.btnStart)
        btnGuide   = findViewById(R.id.btnGuide)   // ← MỚI
        btnAbout   = findViewById(R.id.btnAbout)

        // Giữ nguyên: Intent cũ chuyển sang MapActivity
        btnStart.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        // MỚI: Intent mở GuideActivity (bên trong dùng Navigation Graph)
        btnGuide.setOnClickListener {
            startActivity(Intent(this, GuideActivity::class.java))
        }

        // Giữ nguyên: Intent cũ chuyển sang AboutActivity
        btnAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}