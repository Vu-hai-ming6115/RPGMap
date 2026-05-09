package com.example.mymap2

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var btnBack: Button
    private lateinit var btnRoute: Button
    private lateinit var tvCurrentLocation: TextView
    private lateinit var tvDestination: TextView
    private lateinit var tvDistance: TextView

    private val playerIndex = 0        // Làng = index 0
    private var selectedIndex = -1
    private var pathAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView             = findViewById(R.id.mapView)
        btnBack             = findViewById(R.id.btnBack)
        btnRoute            = findViewById(R.id.btnRoute)
        tvCurrentLocation   = findViewById(R.id.tvCurrentLocation)
        tvDestination       = findViewById(R.id.tvDestination)
        tvDistance          = findViewById(R.id.tvDistance)

        updateInfo()

        // Xử lý chạm vào địa điểm trên bản đồ
        mapView.onLocationClick = { index ->
            if (index == playerIndex) {
                Toast.makeText(this, "📍 Bạn đang ở đây!", Toast.LENGTH_SHORT).show()
            } else {
                selectedIndex = index
                mapView.selectedIndex = index
                updateInfo()
                animatePath()           // chạy animation đường đi
                Toast.makeText(this,
                    "🎯 Chọn: ${mapView.locations[index].name}",
                    Toast.LENGTH_SHORT).show()
            }
        }

        // Nút chỉ đường
        btnRoute.setOnClickListener {
            when {
                selectedIndex < 0 ->
                    Toast.makeText(this, "⚠️ Hãy chọn điểm đến trước!", Toast.LENGTH_SHORT).show()
                else -> {
                    val intent = Intent(this, RouteActivity::class.java).apply {
                        putExtra("FROM", mapView.locations[playerIndex].name)
                        putExtra("TO",   mapView.locations[selectedIndex].name)
                        putExtra("DISTANCE", calculateDistance())
                    }
                    startActivity(intent)
                }
            }
        }

        btnBack.setOnClickListener { finish() }
    }

    // Animation đường đi từ 0 → 1
    private fun animatePath() {
        pathAnimator?.cancel()
        mapView.animProgress = 0f
        pathAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1200
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                mapView.animProgress = it.animatedValue as Float
                mapView.invalidate()   // yêu cầu vẽ lại
            }
            start()
        }
    }

    private fun updateInfo() {
        val dest = if (selectedIndex >= 0) mapView.locations[selectedIndex].name else "Chưa chọn"
        tvCurrentLocation.text = "📍 Vị trí: ${mapView.locations[playerIndex].name}"
        tvDestination.text     = "🎯 Đích: $dest"
        tvDistance.text        = if (selectedIndex >= 0)
            "📏 ${calculateDistance()} dặm"
        else
            "📏 --"
    }

    private fun calculateDistance(): String {
        if (selectedIndex < 0) return "?"
        val from = mapView.locations[playerIndex]
        val to   = mapView.locations[selectedIndex]
        // Tính dựa trên tọa độ ratio × 1000 để ra đơn vị dặm giả lập
        val dx = (from.xRatio - to.xRatio) * 1000
        val dy = (from.yRatio - to.yRatio) * 1000
        return maxOf(1, (sqrt(dx * dx + dy * dy) / 60).toInt()).toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        pathAnimator?.cancel()
    }
}