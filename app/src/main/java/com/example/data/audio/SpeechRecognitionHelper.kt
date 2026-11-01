package com.example.data.audio

import kotlin.math.max

object PronunciationEvaluator {

    /**
     * Compares recognized user speech with target phrase and calculates
     * accuracy percentage (0 - 100%) and personalized feedback.
     */
    fun evaluate(spokenText: String, targetText: String): EvaluationResult {
        val cleanSpoken = clean(spokenText)
        val cleanTarget = clean(targetText)

        if (cleanSpoken.isEmpty()) {
            return EvaluationResult(0, "No speech detected. Try again!", false)
        }

        val distance = levenshteinDistance(cleanSpoken, cleanTarget)
        val maxLength = max(cleanSpoken.length, cleanTarget.length)
        val similarity = if (maxLength == 0) 1.0f else (1.0f - distance.toFloat() / maxLength)
        val scorePercent = (similarity * 100).toInt().coerceIn(0, 100)

        val (feedback, isPassed) = when {
            scorePercent >= 85 -> "Outstanding! Sounds like a native speaker! 🎉" to true
            scorePercent >= 70 -> "Great pronunciation! Very understandable. 👍" to true
            scorePercent >= 50 -> "Good effort! Pay close attention to vowel sounds. 🦜" to false
            else -> "Keep practicing! Listen to Poly's audio and try again. 💪" to false
        }

        return EvaluationResult(scorePercent, feedback, isPassed)
    }

    private fun clean(text: String): String {
        return text.lowercase()
            .replace(Regex("[¡!¿?,.;:\"'()-]"), "")
            .trim()
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // deletion
                    dp[i][j - 1] + 1,      // insertion
                    dp[i - 1][j - 1] + cost // substitution
                )
            }
        }
        return dp[s1.length][s2.length]
    }
}

data class EvaluationResult(
    val accuracyScore: Int,
    val feedback: String,
    val isPassed: Boolean
)
