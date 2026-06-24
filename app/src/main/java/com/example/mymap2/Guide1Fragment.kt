package com.example.mymap2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * Bước 1: Giới thiệu bản đồ
 * Dùng findNavController().navigate() để sang bước 2
 */
class Guide1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_guide1, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Nút Tiếp theo → navigate sang Guide2Fragment
        view.findViewById<Button>(R.id.btnNext1).setOnClickListener {
            findNavController().navigate(R.id.action_guide1_to_guide2)
        }

        // Nút Thoát → đóng GuideActivity, quay lại MainActivity
        view.findViewById<Button>(R.id.btnExit1).setOnClickListener {
            requireActivity().finish()
        }
    }
}