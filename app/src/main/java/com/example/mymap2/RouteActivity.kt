package com.example.mymap2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RouteActivity : AppCompatActivity() {

    private lateinit var tvRouteTitle: TextView
    private lateinit var tvFrom: TextView
    private lateinit var tvTo: TextView
    private lateinit var tvDistance: TextView
    private lateinit var tvStep1: TextView
    private lateinit var tvStep2: TextView
    private lateinit var tvStep3: TextView
    private lateinit var tvTip: TextView
    private lateinit var btnDone: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        tvRouteTitle = findViewById(R.id.tvRouteTitle)
        tvFrom       = findViewById(R.id.tvFrom)
        tvTo         = findViewById(R.id.tvTo)
        tvDistance   = findViewById(R.id.tvDistance)
        tvStep1      = findViewById(R.id.tvStep1)
        tvStep2      = findViewById(R.id.tvStep2)
        tvStep3      = findViewById(R.id.tvStep3)
        tvTip        = findViewById(R.id.tvTip)
        btnDone      = findViewById(R.id.btnDone)

        // Nhận dữ liệu từ MapActivity qua Intent
        val from     = intent.getStringExtra("FROM") ?: ""
        val to       = intent.getStringExtra("TO") ?: ""
        val distance = intent.getStringExtra("DISTANCE") ?: "?"

        tvRouteTitle.text = "🗺️ Chỉ Đường"
        tvFrom.text       = "Từ:  📍 $from"
        tvTo.text         = "Đến: 🎯 $to"
        tvDistance.text   = "Tổng quãng đường: $distance dặm"

        // Tạo các bước đi giả lập
        val steps = generateSteps(from, to)
        tvStep1.text = "1️⃣  ${steps[0]}"
        tvStep2.text = "2️⃣  ${steps[1]}"
        tvStep3.text = "3️⃣  ${steps[2]}"
        tvTip.text   = "💡 Mẹo: ${getTip(to)}"

        btnDone.setOnClickListener { finish() }
    }

    private fun generateSteps(from: String, to: String): List<String> = when (to) {
        "Lâu đài" -> listOf(
            "Đi về hướng Bắc qua $from",
            "Vượt qua cánh rừng phía đông",
            "Lâu đài xuất hiện trên đỉnh đồi ⛰️"
        )
        "Rừng" -> listOf(
            "Rời $from đi về hướng Đông",
            "Theo con đường mòn qua đồng cỏ",
            "Vào Rừng qua cổng cây lớn 🌲"
        )
        "Chợ" -> listOf(
            "Đi về hướng Nam từ $from",
            "Qua cây cầu gỗ nhỏ",
            "Chợ ở cuối con đường lát đá 🏪"
        )
        "Hồ" -> listOf(
            "Đi về hướng Đông-Nam từ $from",
            "Theo bờ suối uốn khúc",
            "Hồ nước xanh xuất hiện trước mặt 🌊"
        )
        else -> listOf(  // Làng
            "Quay về hướng Tây từ $from",
            "Đi qua cánh đồng hoa vàng",
            "Làng ở cuối con đường cái 🏡"
        )
    }

    private fun getTip(destination: String): String = when (destination) {
        "Lâu đài" -> "Cẩn thận với rồng canh cổng! 🐉"
        "Rừng"    -> "Mang theo đuốc vì rừng rất tối! 🔦"
        "Chợ"     -> "Chợ họp vào buổi sáng, đi sớm! ⏰"
        "Hồ"      -> "Nước hồ rất sâu, đừng bơi một mình! 🚫"
        else      -> "Làng rất yên bình, hãy nghỉ ngơi! 😊"
    }
}