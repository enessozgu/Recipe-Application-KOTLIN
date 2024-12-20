package com.example.mekan.view

import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.mekan.R
import com.example.mekan.database.Food
import com.example.mekan.databinding.FragmentGosterimBinding


@Suppress("DEPRECATION")
class Gosterim : Fragment() {
    private var _binding: FragmentGosterimBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGosterimBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myTextView: TextView = binding.textView3
        val myButton: Button = binding.button5

        myButton.setOnClickListener {

            if (myTextView.visibility == View.GONE) { //yapılış gözükmüyorsa
                myTextView.visibility = View.VISIBLE // yapılışı Göster
                binding.textView2.visibility=View.GONE // malzemeleri gizle
                binding.button5.text="Malzemeleri Göster"

            } else {
                myTextView.visibility = View.GONE // yapılış gözüküyorsa yapılışı gizle
                binding.textView2.visibility=View.VISIBLE //malzemeleri göster binding.button5.text="Malzemeler"
                binding.button5.text="Yapılışı Göster"
            }
        }


        arguments?.getParcelable<Food>("foodItem")?.let { food ->
            binding.textView.text = food.yemekAdi
            binding.textView2.text = food.malzeme
            binding.textView3.text = food.yapilis

            // Görseli yükle
            val bitmap = BitmapFactory.decodeByteArray(food.gorsel, 0, food.gorsel.size)
            binding.imageView3.setImageBitmap(bitmap)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
