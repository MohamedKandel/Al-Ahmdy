package com.correct.alahmdy.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.correct.alahmdy.data.quran.FilterClass


@Dao
interface FilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: FilterClass)

    @Query("SELECT * FROM filter")
    suspend fun getAll(): List<FilterClass>

    @Query("SELECT * FROM filter WHERE id = :id")
    suspend fun getById(id: Int): FilterClass

    @Delete
    suspend fun delete(entity: FilterClass)

}