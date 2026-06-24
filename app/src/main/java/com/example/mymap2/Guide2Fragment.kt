package com.example.mymap2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * Bước 2: Cách chọn điểm đến trên bản đồ
 */
class Guide2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_guide2, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tiếp theo → Guide3Fragment
        view.findViewById<Button>(R.id.btnNext2).setOnClickListener {
            findNavController().navigate(R.id.action_guide2_to_guide3)
        }

        // Quay lại → Guide1Fragment (popBackStack lên bước 1)
        view.findViewById<Button>(R.id.btnBack2).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}