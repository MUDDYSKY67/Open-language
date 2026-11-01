package com.example.data.model

data class UserProfile(
    val id: String = "default_user",
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
    val voicePitch: Float = 1.0f,
    val soundEffectsEnabled: Boolean = true,
    val isDarkMode: Boolean = false,
    val wordsLearnedCount: Int = 42,
    val completedLessonIds: Set<String> = setOf("es_u1_l1", "es_u1_l2")
)

data class SavedWord(
    val id: String,
    val languageId: String,
    val word: String,
    val translation: String,
    val phonetic: String,
    val exampleSentence: String,
    val exampleTranslation: String,
    val srsLevel: Int = 1, // 1: Learning, 2: Reviewing, 3: Good, 4: Strong, 5: Mastered
    val isLearned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class DailyTask(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val targetCount: Int,
    val currentCount: Int,
    val xpReward: Int,
    val gemReward: Int,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false
)

data class AchievementBadge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val currentProgress: Int,
    val targetProgress: Int,
    val isUnlocked: Boolean = false,
    val tierLabel: String = "Bronze"
)

enum class GameMode(val title: String, val subtitle: String, val iconEmoji: String, val xpReward: Int) {
    WORD_MATCHING("Word Matching", "Tap matching pairs as fast as you can", "🧩", 25),
    PICTURE_GUESS("Picture Guess", "Identify the correct vocabulary image", "🖼️", 20),
    SENTENCE_BUILDER("Sentence Builder", "Order words into grammatical sentences", "🧱", 30),
    LISTENING_CHALLENGE("Listening Challenge", "Sharpen your ear with native audio", "🎧", 25),
    PRONUNCIATION_CHALLENGE("Pronunciation Duel", "Speak clearly into the microphone", "🎙️", 35),
    SPEED_VOCABULARY("Speed Vocabulary", "Answer before the 30s timer runs out", "⚡", 40),
    TRANSLATION_BATTLE("Translation Battle", "Translate sentences under pressure", "⚔️", 30),
    DAILY_QUIZ("Daily Quiz", "5 fresh mixed questions every morning", "📅", 50),
    MEMORY_CARDS("Memory Flip", "Match hidden cards by sound and meaning", "🃏", 25),
    CONVERSATION_CHALLENGE("Conversation Quest", "Complete dialogue scenarios with Poly", "💬", 45)
}
