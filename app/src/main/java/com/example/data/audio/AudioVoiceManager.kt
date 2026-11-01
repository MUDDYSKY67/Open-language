package com.example.data.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AudioVoiceManager(context: Context) : TextToSpeech.OnInitListener {

    private val TAG = "AudioVoiceManager"
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private var currentSpeed: Float = 1.0f

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            _isInitialized.value = true
            tts?.setSpeechRate(currentSpeed)
            Log.d(TAG, "TextToSpeech engine initialized successfully")
        } else {
            Log.e(TAG, "Failed to initialize TextToSpeech engine")
            _isInitialized.value = false
        }
    }

    fun setSpeed(speed: Float) {
        currentSpeed = speed
        tts?.setSpeechRate(speed)
    }

    fun speak(text: String, languageCode: String, customSpeed: Float? = null) {
        if (!_isInitialized.value || tts == null) {
            Log.w(TAG, "TTS not ready yet")
            return
        }

        val locale = getLocaleForLanguage(languageCode)
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.w(TAG, "Language $languageCode is not supported by device TTS engine, falling back to US English")
            tts?.setLanguage(Locale.US)
        }

        val speedToUse = customSpeed ?: currentSpeed
        tts?.setSpeechRate(speedToUse)

        _isSpeaking.value = true
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "OPEN_LANG_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    private fun getLocaleForLanguage(code: String): Locale {
        return when (code.lowercase()) {
            "es" -> Locale("es", "ES")
            "fr" -> Locale.FRANCE
            "de" -> Locale.GERMANY
            "ja" -> Locale.JAPAN
            "hi" -> Locale("hi", "IN")
            "ko" -> Locale.KOREA
            "zh" -> Locale.SIMPLIFIED_CHINESE
            "it" -> Locale.ITALY
            "pt" -> Locale("pt", "BR")
            "ru" -> Locale("ru", "RU")
            "ar" -> Locale("ar", "SA")
            else -> Locale.US
        }
    }
}
