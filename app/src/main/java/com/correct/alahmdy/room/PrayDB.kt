package com.correct.alahmdy.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.correct.alahmdy.data.quran.FilterClass
import com.correct.alahmdy.data.quran.QuranClass
import com.correct.alahmdy.data.quran.Read
import com.correct.alahmdy.data.user.PrayTime
import com.correct.alahmdy.data.user.User

@Database(
    entities = [User::class, PrayTime::class, QuranClass::class, FilterClass::class, Read::class],
    version = 1
)
abstract class PrayDB : RoomDatabase() {
    abstract fun userDao(): UsersDao
    abstract fun prayDao(): PrayDao
    abstract fun quranDao(): QuranDao
    abstract fun filterDao(): FilterDao
    abstract fun readDao(): ReadDao
    abstract fun tasbehDao(): TasbehDao

    companion object {
        @Volatile
        private var INSTANCE: PrayDB? = null
        fun getDBInstance(context: Context): PrayDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PrayDB::class.java,
                    "prayDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}