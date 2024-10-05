package com.correct.alahmdy.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.correct.alahmdy.data.quran.Read


@Dao
interface ReadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Read)

    @Query("SELECT * FROM read")
    suspend fun getAll(): List<Read>

    @Query("select * from read order by id desc limit 5")
    suspend fun getLastRead(): List<Read>

    @Query("SELECT * FROM read WHERE id = :id")
    suspend fun getById(id: Int): Read

    @Query("select MAX(id) from read")
    suspend fun getNextID(): Int?

    @Delete
    suspend fun delete(entity: Read)

}