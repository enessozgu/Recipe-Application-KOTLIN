package com.example.mekan.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.mekan.R
import com.example.mekan.database.AppDatabase
import com.example.mekan.adapter.FoodAdapter
import com.example.mekan.database.Food
import com.example.mekan.databinding.FragmentRecylePageBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Recyle_page : Fragment() {

    private var _binding: FragmentRecylePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private var adapter: FoodAdapter? = null
    private val foodList: ArrayList<Food> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecylePageBinding.inflate(inflater, container, false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "mekan-database2"
        ).build()

        // Initialize the database outside of the coroutine

        adapter = FoodAdapter(foodList){selectedFood->
            val bundle=Bundle().apply {
                putParcelable("foodItem",selectedFood)
            }
            findNavController().navigate(R.id.action_recyle_page_to_gosterim2,bundle)

        }
        binding.recylerr.layoutManager = LinearLayoutManager(requireContext())
        binding.recylerr.adapter = adapter



        // Fetch data from the database
        fetchDataFromDatabase()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchDataFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val fetchedFoodList = db.foodDao().getAll()
                withContext(Dispatchers.Main) {
                    foodList.clear()
                    foodList.addAll(fetchedFoodList)
                    adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }














}