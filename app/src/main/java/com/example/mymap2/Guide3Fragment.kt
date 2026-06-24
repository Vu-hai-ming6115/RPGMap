package com.example.mymap2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * Bước 3: Cách đọc chỉ đường — trang cuối
 */
class Guide3Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_guide3, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Quay lại bước 2
        view.findViewById<Button>(R.id.btnBack3).setOnClickListener {
            findNavController().popBackStack()
        }

        // Hoàn thành → đóng GuideActivity, quay về MainActivity
        view.findViewById<Button>(R.id.btnFinish).setOnClickListener {
            requireActivity().finish()
        }
    }
}