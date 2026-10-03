package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.InvestigationSession
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestigationDao {
    @Query("SELECT * FROM investigation_sessions WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getSessionsForUser(userId: String): Flow<List<InvestigationSession>>

    @Query("SELECT * FROM investigation_sessions WHERE id = :id")
    fun getSessionById(id: Long): Flow<InvestigationSession?>

    @Query("SELECT * FROM investigation_sessions WHERE id = :id")
    suspend fun getSessionByIdSync(id: Long): InvestigationSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: InvestigationSession): Long

    @Update
    suspend fun updateSession(session: InvestigationSession)

    @Query("DELETE FROM investigation_sessions WHERE id = :id")
    suspend fun deleteSession(id: Long)
}
