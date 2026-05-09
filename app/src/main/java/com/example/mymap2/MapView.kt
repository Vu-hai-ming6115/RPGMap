package com.example.mymap2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class MapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    // ===== Dữ liệu địa điểm =====
    data class Location(val name: String, val icon: String, val xRatio: Float, val yRatio: Float)

    val locations = listOf(
        Location("Làng",     "🏡", 0.18f, 0.65f),
        Location("Rừng",     "🌲", 0.55f, 0.30f),
        Location("Lâu Đài",  "🏰", 0.82f, 0.12f),
        Location("Chợ",      "🏪", 0.25f, 0.82f),
        Location("Hồ",       "🌊", 0.72f, 0.70f)
    )

    // ===== State =====
    var selectedIndex: Int = -1          // địa điểm đích được chọn
    var playerIndex:   Int = 0           // vị trí hiện tại (Làng)
    var animProgress:  Float = 0f        // 0f → 1f cho animation đường đi
    var onLocationClick: ((Int) -> Unit)? = null

    // ===== Paint =====
    private val paintGround = Paint().apply {
        color = Color.parseColor("#2D4A1E")
        style = Paint.Style.FILL
    }
    private val paintPath = Paint().apply {
        color = Color.parseColor("#FFD700")
        style = Paint.Style.STROKE
        strokeWidth = 6f
        pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }
    private val paintPathAnim = Paint().apply {
        color = Color.parseColor("#FF6600")
        style = Paint.Style.STROKE
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }
    private val paintNode = Paint().apply {
        color = Color.parseColor("#2D2010")
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val paintNodeSelected = Paint().apply {
        color = Color.parseColor("#FFD700")
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val paintNodePlayer = Paint().apply {
        color = Color.parseColor("#FF4444")
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val paintNodeBorder = Paint().apply {
        color = Color.parseColor("#FFD700")
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }
    private val paintText = Paint().apply {
        color = Color.parseColor("#F0E6C8")
        textSize = 28f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }
    private val paintEmoji = Paint().apply {
        textSize = 48f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    private val paintGrid = Paint().apply {
        color = Color.parseColor("#1A3010")
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    // ===== Vẽ =====
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        drawBackground(canvas, w, h)
        drawGrid(canvas, w, h)
        drawAllPaths(canvas, w, h)         // đường giữa các node
        drawAnimatedPath(canvas, w, h)     // đường highlight khi chọn
        drawLocations(canvas, w, h)
    }

    // Nền bản đồ gradient
    private fun drawBackground(canvas: Canvas, w: Float, h: Float) {
        val grad = LinearGradient(0f, 0f, w, h,
            Color.parseColor("#1E3A10"),
            Color.parseColor("#2D4A1E"),
            Shader.TileMode.CLAMP)
        paintGround.shader = grad
        canvas.drawRect(0f, 0f, w, h, paintGround)
        paintGround.shader = null
    }

    // Lưới ô vuông nhẹ kiểu game
    private fun drawGrid(canvas: Canvas, w: Float, h: Float) {
        val step = 60f
        var x = 0f
        while (x < w) { canvas.drawLine(x, 0f, x, h, paintGrid); x += step }
        var y = 0f
        while (y < h) { canvas.drawLine(0f, y, w, y, paintGrid); y += step }
    }

    // Vẽ TẤT CẢ đường nối giữa các địa điểm (nét đứt vàng nhạt)
    private fun drawAllPaths(canvas: Canvas, w: Float, h: Float) {
        val edges = listOf(
            0 to 1, // Làng → Rừng
            1 to 2, // Rừng → Lâu Đài
            0 to 3, // Làng → Chợ
            1 to 4, // Rừng → Hồ
            3 to 4, // Chợ → Hồ
            2 to 4  // Lâu Đài → Hồ
        )
        val dimPath = Paint(paintPath).apply { alpha = 80 }
        for ((a, b) in edges) {
            val ax = locations[a].xRatio * w
            val ay = locations[a].yRatio * h
            val bx = locations[b].xRatio * w
            val by = locations[b].yRatio * h
            canvas.drawLine(ax, ay, bx, by, dimPath)
        }
    }

    // Vẽ đường từ player → đích đến có animation
    private fun drawAnimatedPath(canvas: Canvas, w: Float, h: Float) {
        if (selectedIndex < 0 || selectedIndex == playerIndex) return

        val sx = locations[playerIndex].xRatio * w
        val sy = locations[playerIndex].yRatio * h
        val ex = locations[selectedIndex].xRatio * w
        val ey = locations[selectedIndex].yRatio * h

        // Đường đã đi (cam, độ dài theo animProgress)
        val cx = sx + (ex - sx) * animProgress
        val cy = sy + (ey - sy) * animProgress
        canvas.drawLine(sx, sy, cx, cy, paintPathAnim)

        // Vẽ mũi tên tại điểm cuối đường đã đi
        if (animProgress > 0.05f) drawArrow(canvas, sx, sy, cx, cy)
    }

    // Mũi tên nhỏ tại đầu đường
    private fun drawArrow(canvas: Canvas, sx: Float, sy: Float, ex: Float, ey: Float) {
        val angle = Math.atan2((ey - sy).toDouble(), (ex - sx).toDouble()).toFloat()
        val arrowLen = 24f
        val arrowAngle = 0.5f
        val x1 = ex - arrowLen * Math.cos((angle - arrowAngle).toDouble()).toFloat()
        val y1 = ey - arrowLen * Math.sin((angle - arrowAngle).toDouble()).toFloat()
        val x2 = ex - arrowLen * Math.cos((angle + arrowAngle).toDouble()).toFloat()
        val y2 = ey - arrowLen * Math.sin((angle + arrowAngle).toDouble()).toFloat()
        val arrowPaint = Paint(paintPathAnim).apply {
            style = Paint.Style.FILL_AND_STROKE; strokeWidth = 4f
        }
        canvas.drawLine(ex, ey, x1, y1, arrowPaint)
        canvas.drawLine(ex, ey, x2, y2, arrowPaint)
    }

    // Vẽ các node địa điểm
    private fun drawLocations(canvas: Canvas, w: Float, h: Float) {
        locations.forEachIndexed { i, loc ->
            val x = loc.xRatio * w
            val y = loc.yRatio * h
            val radius = if (i == selectedIndex || i == playerIndex) 44f else 36f

            // Shadow
            val shadowPaint = Paint(paintNode).apply { alpha = 80 }
            canvas.drawCircle(x + 4f, y + 4f, radius, shadowPaint)

            // Nền node
            val nodePaint = when (i) {
                playerIndex   -> paintNodePlayer
                selectedIndex -> paintNodeSelected
                else          -> paintNode
            }
            canvas.drawCircle(x, y, radius, nodePaint)
            canvas.drawCircle(x, y, radius, paintNodeBorder)

            // Emoji icon
            canvas.drawText(loc.icon, x, y + 16f, paintEmoji)

            // Tên địa điểm
            val labelPaint = Paint(paintText).apply {
                color = if (i == selectedIndex) Color.parseColor("#1A1209")
                else Color.parseColor("#F0E6C8")
                textSize = 22f
            }
            canvas.drawText(loc.name, x, y + radius + 20f, labelPaint)

            // Badge "BẠN" tại vị trí player
            if (i == playerIndex) {
                val badgePaint = Paint().apply {
                    color = Color.parseColor("#FF4444")
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }
                canvas.drawCircle(x + radius * 0.7f, y - radius * 0.7f, 14f, badgePaint)
                val bText = Paint(paintText).apply { textSize = 16f; color = Color.WHITE }
                canvas.drawText("★", x + radius * 0.7f, y - radius * 0.7f + 6f, bText)
            }
        }
    }

    // ===== Touch để chọn địa điểm =====
    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        if (event.action == android.view.MotionEvent.ACTION_UP) {
            val w = width.toFloat()
            val h = height.toFloat()
            locations.forEachIndexed { i, loc ->
                val dx = event.x - loc.xRatio * w
                val dy = event.y - loc.yRatio * h
                if (Math.sqrt((dx * dx + dy * dy).toDouble()) < 60.0) {
                    onLocationClick?.invoke(i)
                    return true
                }
            }
        }
        return true
    }
}