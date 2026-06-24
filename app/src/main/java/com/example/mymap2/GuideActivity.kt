package com.example.mymap2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

/**
 * Activity MỚI — dùng Navigation Graph để điều hướng giữa các Fragment hướng dẫn.
 * Các Activity cũ (MainActivity, MapActivity, RouteActivity, AboutActivity) KHÔNG thay đổi.
 *
 * Luồng: Guide1Fragment → Guide2Fragment → Guide3Fragment (slide trái/phải)
 */
class GuideActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        // Lấy NavController từ NavHostFragment trong layout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.guideNavHost) as NavHostFragment
        navController = navHostFragment.navController
    }

    // Cho phép nút Back vật lý hoạt động đúng với NavController
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}