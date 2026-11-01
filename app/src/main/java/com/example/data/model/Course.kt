package com.example.data.model

enum class ExerciseType {
    MULTIPLE_CHOICE,
    FILL_BLANKS,
    TRANSLATE,
    MATCH_WORDS,
    SENTENCE_BUILDER,
    LISTEN_SELECT,
    PRONUNCIATION,
    PICTURE_VOCAB
}

enum class CefrLevel(val title: String, val badge: String) {
    A1("Beginner", "A1"),
    A2("Elementary", "A2"),
    B1("Intermediate", "B1"),
    B2("Upper Intermediate", "B2"),
    C1("Advanced", "C1"),
    C2("Proficiency", "C2")
}

data class MatchPair(
    val left: String,
    val right: String
)

data class Exercise(
    val id: String,
    val type: ExerciseType,
    val prompt: String,
    val targetText: String,               // Foreign sentence or word
    val phonetic: String? = null,         // e.g. "Konnichiwa" or "O-la"
    val options: List<String> = emptyList(),
    val correctAnswer: String,            // Primary correct answer or token
    val matchPairs: List<MatchPair> = emptyList(),
    val sentenceTiles: List<String> = emptyList(), // scrambled tokens to arrange
    val explanation: String,              // Educational rationale why answer is right
    val audioText: String? = null,        // String to speak via TTS
    val iconEmoji: String? = null
)

data class Lesson(
    val id: String,
    val title: String,
    val description: String,
    val order: Int,
    val xpReward: Int = 20,
    val gemReward: Int = 5,
    val isCheckpoint: Boolean = false,
    val exercises: List<Exercise>
)

data class CourseUnit(
    val id: String,
    val number: Int,
    val title: String,
    val subtitle: String,
    val cefrLevel: CefrLevel,
    val colorHex: Long = 0xFF10B981,
    val iconEmoji: String = "🌱",
    val lessons: List<Lesson>
)

data class Course(
    val languageId: String,
    val targetLanguageName: String,
    val units: List<CourseUnit>
)
