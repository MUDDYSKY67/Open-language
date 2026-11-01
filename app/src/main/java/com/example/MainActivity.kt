package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.data.ai.AiTutorService
import com.example.data.audio.AudioVoiceManager
import com.example.data.repository.LearningRepository
import com.example.ui.navigation.OpenLanguageApp
import com.example.ui.theme.OpenLanguageTheme

class MainActivity : ComponentActivity() {

    private lateinit var audioManager: AudioVoiceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        audioManager = AudioVoiceManager(this)
        val repository = LearningRepository(this)
        val aiTutorService = AiTutorService()

        setContent {
            val userProfile by repository.currentProfile.collectAsState()

            OpenLanguageTheme(darkTheme = userProfile.isDarkMode) {
                OpenLanguageApp(
                    repository = repository,
                    audioManager = audioManager,
                    aiTutorService = aiTutorService
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::audioManager.isInitialized) {
            audioManager.shutdown()
        }
    }
}
