package com.example.data.ai

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "user", "tutor", or "system"
    val text: String,
    val translation: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

class AiTutorService {

    private val TAG = "AiTutorService"

    // Lazily evaluate Firebase GenerativeModel to avoid startup crashes if Firebase is uninitialized
    private val generativeModel by lazy {
        try {
            Firebase.ai.generativeModel(
                modelName = "gemini-3.8-flash"
            )
        } catch (e: Exception) {
            Log.e(TAG, "Firebase AI not yet configured or available: ${e.message}")
            null
        }
    }

    suspend fun getTutorResponse(
        userPrompt: String,
        targetLanguageName: String,
        nativeLanguageName: String,
        history: List<ChatMessage>
    ): String = withContext(Dispatchers.IO) {
        val model = generativeModel
        if (model == null) {
            return@withContext getOfflineTutorFallback(userPrompt, targetLanguageName, nativeLanguageName)
        }

        try {
            val systemContext = "You are Poly the colorful parrot, a friendly, enthusiastic, world-class language tutor. " +
                    "The user is learning $targetLanguageName and their native language is $nativeLanguageName. " +
                    "Answer their question or continue the roleplay dialogue. Always include natural examples in $targetLanguageName " +
                    "with clear explanations in $nativeLanguageName. Keep it encouraging, concise, and fun with a touch of parrot charm (like 🦜)! "

            val fullPrompt = "$systemContext\n\nUser asked: $userPrompt"

            withTimeout(35_000L) {
                val response = model.generateContent(fullPrompt)
                response.text ?: getOfflineTutorFallback(userPrompt, targetLanguageName, nativeLanguageName)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Gemini API call failed, using smart educational fallback", e)
            getOfflineTutorFallback(userPrompt, targetLanguageName, nativeLanguageName)
        }
    }

    private fun getOfflineTutorFallback(
        prompt: String,
        targetLang: String,
        nativeLang: String
    ): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("hello") || lower.contains("hi") || lower.contains("greeting") -> {
                "¡Hola! (or greeting in $targetLang)! 🦜 I'm Poly, your language guide. In $targetLang, greetings often vary by time of day. Practice saying it aloud, and notice how tone sets a warm, polite impression!"
            }
            lower.contains("grammar") || lower.contains("verb") -> {
                "Great grammar question! 🦜 In $targetLang, verbs often conjugate based on the subject and tense. Remember: starting with the present tense of the top 5 most common verbs gives you 50% of everyday conversations!"
            }
            lower.contains("pronounce") || lower.contains("sound") -> {
                "Pronunciation tip from Poly: 🦜 Listen closely to the vowel sounds in $targetLang. Unlike some languages where vowels change sound depending on surrounding consonants, vowels in many languages remain pure and distinct!"
            }
            else -> {
                "Poly here! 🦜 That's a great concept in $targetLang. When expressing this, focus on vocabulary in context rather than word-for-word translation. Keep practicing daily, and you'll build natural conversational flow!"
            }
        }
    }
}
