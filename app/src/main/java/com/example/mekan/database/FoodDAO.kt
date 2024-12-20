package com.example.mekan.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

import androidx.room.Query

    @Dao
    interface FoodDAO {

        @Query("SELECT * FROM Food")
        suspend fun getAll(): List<Food>

        @Insert
        suspend fun insertAll(food: Food)  // Insert a single food object

        @Delete
        suspend fun delete(food: Food)
    }

