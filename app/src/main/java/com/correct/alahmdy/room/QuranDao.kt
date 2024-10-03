package com.correct.alahmdy.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.correct.alahmdy.data.quran.QuranClass


@Dao
interface QuranDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: QuranClass)

    @Query("SELECT * FROM quran")
    suspend fun getAll(): List<QuranClass>

    @Query("SELECT * FROM quran WHERE number = :number")
    suspend fun getByNumber(number: Int): QuranClass

    @Delete
    suspend fun delete(entity: QuranClass)

}