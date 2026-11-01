package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "primary_user",
    val name: String = "Poly Learner",
    val email: String = "learner@openlanguage.app",
    val nativeLanguageId: String = "en",
    val targetLanguageId: String = "es",
    val skillLevel: String = "Beginner A1",
    val dailyGoalMinutes: Int = 15,
    val learningMotivation: String = "Culture & Travel",
    val streakDays: Int = 7,
    val totalXp: Int = 340,
    val gems: Int = 125,
    val hearts: Int = 5,
    val maxHearts: Int = 5,
    val speechSpeed: Float = 1.0f,
    val soundEffectsEnabled: Boolean = true,
    val isDarkMode: Boolean = false,
    val wordsLearnedCount: Int = 42,
    val completedLessonsRaw: String = "es_u1_l1,es_u1_l2"
)

@Entity(tableName = "saved_words")
data class SavedWordEntity(
    @PrimaryKey val id: String,
    val languageId: String,
    val word: String,
    val translation: String,
    val phonetic: String,
    val exampleSentence: String,
    val exampleTranslation: String,
    val srsLevel: Int = 1,
    val isLearned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    fun getProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    suspend fun getProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)
}

@Dao
interface SavedWordsDao {
    @Query("SELECT * FROM saved_words WHERE languageId = :langId ORDER BY timestamp DESC")
    fun getWordsForLanguage(langId: String): Flow<List<SavedWordEntity>>

    @Query("SELECT * FROM saved_words ORDER BY timestamp DESC")
    fun getAllWords(): Flow<List<SavedWordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: SavedWordEntity)

    @Update
    suspend fun updateWord(word: SavedWordEntity)

    @Query("DELETE FROM saved_words WHERE id = :id")
    suspend fun deleteWord(id: String)
}

@Database(entities = [UserProfileEntity::class, SavedWordEntity::class], version = 1, exportSchema = false)
abstract class OpenLanguageDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun savedWordsDao(): SavedWordsDao
}
