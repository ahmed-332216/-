package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "daftari_entries")
data class DaftariEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "PROFILE", "DIARY", "MEMORIES", "DREAMS", "REVIEWS", "TODO", "PLANNER", "BUDGET", "SLEEP", "WORKOUT", "GOALS", "TRAVEL", "GAMES", "BOOKS", "COURSES", "QUOTES", "LINKS", "RECIPES", "EXAM_QUESTIONS", "STUDY_SCHEDULE", "LESSON_SUMMARIES", "PROGRESS", "QUIZ", "GLOSSARY", "LANGUAGE", "SKILLS", "PROGRAMMING", "PHOTOS", "HEALTH_LOG"
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val rating: Float = 0f,
    val extraInfo: String? = null
)

@Dao
interface DaftariDao {
    @Query("SELECT * FROM daftari_entries ORDER BY id DESC")
    fun getAllEntriesFlow(): Flow<List<DaftariEntry>>

    @Query("SELECT * FROM daftari_entries WHERE category = :category ORDER BY id DESC")
    fun getEntriesByCategoryFlow(category: String): Flow<List<DaftariEntry>>

    @Query("SELECT * FROM daftari_entries WHERE category = :category ORDER BY id DESC")
    suspend fun getEntriesByCategory(category: String): List<DaftariEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: DaftariEntry)

    @Update
    suspend fun updateEntry(entry: DaftariEntry)

    @Delete
    suspend fun deleteEntry(entry: DaftariEntry)

    @Query("DELETE FROM daftari_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Int)
}

@Database(entities = [DaftariEntry::class], version = 1, exportSchema = false)
abstract class DaftariDatabase : RoomDatabase() {
    abstract val dao: DaftariDao

    companion object {
        @Volatile
        private var INSTANCE: DaftariDatabase? = null

        fun getDatabase(context: Context): DaftariDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DaftariDatabase::class.java,
                    "daftari_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
