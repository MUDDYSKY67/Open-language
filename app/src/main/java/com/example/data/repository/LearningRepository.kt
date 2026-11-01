package com.example.data.repository

import android.content.Context
import androidx.room.Room
import com.example.data.local.OpenLanguageDatabase
import com.example.data.local.SavedWordEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.AchievementBadge
import com.example.data.model.Course
import com.example.data.model.DailyTask
import com.example.data.model.Language
import com.example.data.model.Lesson
import com.example.data.model.SavedWord
import com.example.data.model.SupportedLanguages
import com.example.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LearningRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        OpenLanguageDatabase::class.java,
        "open_language_db"
    ).fallbackToDestructiveMigration().build()

    private val userProfileDao = db.userProfileDao()
    private val savedWordsDao = db.savedWordsDao()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _currentProfile = MutableStateFlow(UserProfile())
    val currentProfile: StateFlow<UserProfile> = _currentProfile.asStateFlow()

    private val _dailyTasks = MutableStateFlow(getDefaultDailyTasks())
    val dailyTasks: StateFlow<List<DailyTask>> = _dailyTasks.asStateFlow()

    private val _achievements = MutableStateFlow(getDefaultAchievements())
    val achievements: StateFlow<List<AchievementBadge>> = _achievements.asStateFlow()

    init {
        coroutineScope.launch {
            val initialEntity = userProfileDao.getProfile()
            if (initialEntity == null) {
                val newProfile = UserProfile()
                userProfileDao.insertOrUpdate(newProfile.toEntity())
                _currentProfile.value = newProfile
            } else {
                _currentProfile.value = initialEntity.toDomain()
            }

            // Also seed default vocabulary for Spanish if empty
            seedInitialVocabulary()

            userProfileDao.getProfileFlow().collect { entity ->
                if (entity != null) {
                    _currentProfile.value = entity.toDomain()
                }
            }
        }
    }

    private suspend fun seedInitialVocabulary() {
        val defaultWords = listOf(
            SavedWordEntity("es_w1", "es", "¡Hola!", "Hello", "o-la", "¡Hola! ¿Cómo estás?", "Hello! How are you?", 3, true),
            SavedWordEntity("es_w2", "es", "Gracias", "Thank you", "gra-see-as", "Muchas gracias por tu ayuda.", "Thank you very much for your help.", 4, true),
            SavedWordEntity("es_w3", "es", "Por favor", "Please", "por fa-vor", "Un café, por favor.", "A coffee, please.", 2, false),
            SavedWordEntity("es_w4", "es", "Buenos días", "Good morning", "bwe-nos dee-as", "Buenos días a todos.", "Good morning everyone.", 2, false),
            SavedWordEntity("es_w5", "es", "Amigo", "Friend", "a-mee-go", "Él es mi mejor amigo.", "He is my best friend.", 1, false)
        )
        for (w in defaultWords) {
            savedWordsDao.insertWord(w)
        }
    }

    fun getCourse(languageId: String): Course {
        return LanguageCatalog.getCourseForLanguage(languageId)
    }

    fun getLesson(languageId: String, lessonId: String): Lesson? {
        val course = getCourse(languageId)
        for (unit in course.units) {
            val found = unit.lessons.find { it.id == lessonId }
            if (found != null) return found
        }
        return null
    }

    fun getSavedWordsFlow(languageId: String): Flow<List<SavedWord>> {
        return savedWordsDao.getWordsForLanguage(languageId).map { list ->
            list.map { it.toDomain() }
        }
    }

    fun addSavedWord(word: SavedWord) {
        coroutineScope.launch {
            savedWordsDao.insertWord(word.toEntity())
        }
    }

    fun updateWordSrs(id: String, newLevel: Int) {
        coroutineScope.launch {
            // update existing
        }
    }

    fun addXpAndGems(earnedXp: Int, earnedGems: Int) {
        coroutineScope.launch {
            val current = _currentProfile.value
            val updated = current.copy(
                totalXp = current.totalXp + earnedXp,
                gems = current.gems + earnedGems
            )
            userProfileDao.insertOrUpdate(updated.toEntity())
            checkTasksAndAchievements(earnedXp = earnedXp)
        }
    }

    fun loseHeart() {
        coroutineScope.launch {
            val current = _currentProfile.value
            if (current.hearts > 0) {
                val updated = current.copy(hearts = current.hearts - 1)
                userProfileDao.insertOrUpdate(updated.toEntity())
            }
        }
    }

    fun refillHearts() {
        coroutineScope.launch {
            val current = _currentProfile.value
            val costGems = 50
            val newGems = if (current.gems >= costGems) current.gems - costGems else current.gems
            val updated = current.copy(hearts = current.maxHearts, gems = newGems)
            userProfileDao.insertOrUpdate(updated.toEntity())
        }
    }

    fun markLessonCompleted(lessonId: String) {
        coroutineScope.launch {
            val current = _currentProfile.value
            val newCompleted = current.completedLessonIds + lessonId
            val updated = current.copy(
                completedLessonIds = newCompleted,
                totalXp = current.totalXp + 25,
                gems = current.gems + 10,
                wordsLearnedCount = current.wordsLearnedCount + 5
            )
            userProfileDao.insertOrUpdate(updated.toEntity())
            checkTasksAndAchievements(lessonsCompleted = 1)
        }
    }

    fun setTargetLanguage(languageId: String) {
        coroutineScope.launch {
            val current = _currentProfile.value
            val updated = current.copy(targetLanguageId = languageId)
            userProfileDao.insertOrUpdate(updated.toEntity())
        }
    }

    fun setNativeLanguage(languageId: String) {
        coroutineScope.launch {
            val current = _currentProfile.value
            val updated = current.copy(nativeLanguageId = languageId)
            userProfileDao.insertOrUpdate(updated.toEntity())
        }
    }

    fun updateSettings(speechSpeed: Float, soundEffects: Boolean, isDarkMode: Boolean, dailyGoal: Int) {
        coroutineScope.launch {
            val current = _currentProfile.value
            val updated = current.copy(
                speechSpeed = speechSpeed,
                soundEffectsEnabled = soundEffects,
                isDarkMode = isDarkMode,
                dailyGoalMinutes = dailyGoal
            )
            userProfileDao.insertOrUpdate(updated.toEntity())
        }
    }

    private fun checkTasksAndAchievements(earnedXp: Int = 0, lessonsCompleted: Int = 0) {
        val currentTasks = _dailyTasks.value.map { task ->
            when (task.id) {
                "task_1" -> { // Complete 2 lessons
                    val newCount = (task.currentCount + lessonsCompleted).coerceAtMost(task.targetCount)
                    task.copy(currentCount = newCount, isCompleted = newCount >= task.targetCount)
                }
                "task_2" -> { // Earn 50 XP
                    val newCount = (task.currentCount + earnedXp).coerceAtMost(task.targetCount)
                    task.copy(currentCount = newCount, isCompleted = newCount >= task.targetCount)
                }
                else -> task
            }
        }
        _dailyTasks.value = currentTasks
    }

    private fun getDefaultDailyTasks(): List<DailyTask> = listOf(
        DailyTask("task_1", "Complete 2 Lessons", "Bite-sized practice in your target language", "📚", 2, 1, 30, 10, false),
        DailyTask("task_2", "Earn 50 XP", "Gain experience through lessons and games", "⚡", 50, 25, 40, 15, false),
        DailyTask("task_3", "Voice Pronunciation", "Practice 1 speaking challenge with Poly", "🎙️", 1, 0, 25, 5, false),
        DailyTask("task_4", "Master 5 Words", "Add and review cards in your Vocabulary Notebook", "📖", 5, 3, 20, 5, false)
    )

    private fun getDefaultAchievements(): List<AchievementBadge> = listOf(
        AchievementBadge("ach_streak_7", "Flame Keeper", "Reach a 7-day learning streak", "🔥", 7, 7, true, "Silver"),
        AchievementBadge("ach_poly_friend", "Poly's Best Buddy", "Complete your first lesson checkpoint", "🦜", 1, 1, true, "Gold"),
        AchievementBadge("ach_polyglot_3", "Aspiring Polyglot", "Study 3 different languages", "🌍", 2, 3, false, "Bronze"),
        AchievementBadge("ach_xp_500", "XP Champion", "Accumulate 500 total learning XP", "💎", 340, 500, false, "Silver"),
        AchievementBadge("ach_voice_master", "Golden Accent", "Score 90%+ on 5 pronunciation duels", "🎙️", 3, 5, false, "Gold")
    )
}

private fun UserProfileEntity.toDomain(): UserProfile {
    val completedSet = if (completedLessonsRaw.isBlank()) emptySet() else completedLessonsRaw.split(",").toSet()
    return UserProfile(
        id = id,
        name = name,
        email = email,
        nativeLanguageId = nativeLanguageId,
        targetLanguageId = targetLanguageId,
        skillLevel = skillLevel,
        dailyGoalMinutes = dailyGoalMinutes,
        learningMotivation = learningMotivation,
        streakDays = streakDays,
        totalXp = totalXp,
        gems = gems,
        hearts = hearts,
        maxHearts = maxHearts,
        speechSpeed = speechSpeed,
        soundEffectsEnabled = soundEffectsEnabled,
        isDarkMode = isDarkMode,
        wordsLearnedCount = wordsLearnedCount,
        completedLessonIds = completedSet
    )
}

private fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = id,
        name = name,
        email = email,
        nativeLanguageId = nativeLanguageId,
        targetLanguageId = targetLanguageId,
        skillLevel = skillLevel,
        dailyGoalMinutes = dailyGoalMinutes,
        learningMotivation = learningMotivation,
        streakDays = streakDays,
        totalXp = totalXp,
        gems = gems,
        hearts = hearts,
        maxHearts = maxHearts,
        speechSpeed = speechSpeed,
        soundEffectsEnabled = soundEffectsEnabled,
        isDarkMode = isDarkMode,
        wordsLearnedCount = wordsLearnedCount,
        completedLessonsRaw = completedLessonIds.joinToString(",")
    )
}

private fun SavedWordEntity.toDomain(): SavedWord = SavedWord(
    id = id,
    languageId = languageId,
    word = word,
    translation = translation,
    phonetic = phonetic,
    exampleSentence = exampleSentence,
    exampleTranslation = exampleTranslation,
    srsLevel = srsLevel,
    isLearned = isLearned,
    timestamp = timestamp
)

private fun SavedWord.toEntity(): SavedWordEntity = SavedWordEntity(
    id = id,
    languageId = languageId,
    word = word,
    translation = translation,
    phonetic = phonetic,
    exampleSentence = exampleSentence,
    exampleTranslation = exampleTranslation,
    srsLevel = srsLevel,
    isLearned = isLearned,
    timestamp = timestamp
)
