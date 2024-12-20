package com.example.mekan.adapter



import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mekan.database.Food

import com.example.mekan.databinding.RecyleEowBinding

import java.util.ArrayList

class FoodAdapter(private val foodList: ArrayList<Food>, private val onItemClick: (Food) -> Unit): RecyclerView.Adapter<FoodAdapter.PostHolder>() {



    class PostHolder(val binding:RecyleEowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding=RecyleEowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val foodItem = foodList[position]

        // Set the food name
        holder.binding.isim.text = foodItem.yemekAdi

        // Set the food image
        val imageBytes = foodItem.gorsel
        if (imageBytes != null) {
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.binding.imageView.setImageBitmap(bitmap)
        } else {
            // Optional: Set a placeholder image or clear the ImageView if there's no image
            holder.binding.imageView.setImageResource(android.R.color.transparent)
        }

        holder.itemView.setOnClickListener{

            onItemClick(foodItem)
        }





    }


}
