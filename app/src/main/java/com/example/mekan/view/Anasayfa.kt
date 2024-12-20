package com.example.mekan.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mekan.R
import com.example.mekan.databinding.FragmentAnasayfaBinding


class Anasayfa : Fragment() {

    private var _binding: FragmentAnasayfaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnasayfaBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button2.setOnClickListener {findNavController().navigate(R.id.action_anasayfa_to_yemekTanitim) }
        binding.button.setOnClickListener {findNavController().navigate(R.id.action_anasayfa_to_recyle_page) }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }



}