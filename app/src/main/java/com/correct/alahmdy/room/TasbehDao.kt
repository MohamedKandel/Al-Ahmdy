package com.correct.alahmdy.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.correct.alahmdy.data.user.Tasbeh

@Dao
interface TasbehDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Tasbeh)

    @Query("SELECT * FROM tasbeh")
    suspend fun getAll(): List<Tasbeh>

    @Query("SELECT * FROM tasbeh WHERE id = :id")
    suspend fun getById(id: Int): Tasbeh?

    @Delete
    suspend fun delete(entity: Tasbeh)
}