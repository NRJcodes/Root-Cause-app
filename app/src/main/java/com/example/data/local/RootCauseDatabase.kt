package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.InvestigationSession
import com.example.data.model.UserProfile

@Database(
    entities = [InvestigationSession::class, UserProfile::class],
    version = 1,
    exportSchema = false
)
abstract class RootCauseDatabase : RoomDatabase() {
    abstract fun investigationDao(): InvestigationDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: RootCauseDatabase? = null

        fun getDatabase(context: Context): RootCauseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RootCauseDatabase::class.java,
                    "rootcause_enterprise.db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
