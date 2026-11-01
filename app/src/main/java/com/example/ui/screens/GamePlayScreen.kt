package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.audio.AudioVoiceManager
import com.example.data.audio.PronunciationEvaluator
import com.example.data.model.GameMode
import com.example.ui.components.PolyMascot
import com.example.ui.components.PolyMood
import com.example.ui.theme.PolyGoldTertiary
import com.example.ui.theme.PolyGreenPrimary
import com.example.ui.theme.PolyRubyHeart
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GamePlayScreen(
    gameMode: GameMode,
    targetLanguageCode: String,
    audioManager: AudioVoiceManager,
    onFinishGame: (earnedXp: Int, earnedGems: Int) -> Unit,
    onExitGame: () -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var currentRound by remember { mutableIntStateOf(1) }
    val totalRounds = 5
    var isGameOver by remember { mutableStateOf(false) }

    // Memory cards state
    val memoryCards = remember {
        mutableStateListOf(
            "Hola" to "Hello",
            "Gracias" to "Thank you",
            "Por favor" to "Please",
            "Amigo" to "Friend"
        )
    }
    val matchedMemory = remember { mutableStateListOf<String>() }
    var selectedMemoryCard by remember { mutableStateOf<String?>(null) }

    // Speed Vocab Timer
    var timerSeconds by remember { mutableIntStateOf(30) }
    LaunchedEffect(gameMode, isGameOver) {
        if (gameMode == GameMode.SPEED_VOCABULARY && !isGameOver) {
            while (timerSeconds > 0 && !isGameOver) {
                delay(1000L)
                timerSeconds--
            }
            if (timerSeconds <= 0) {
                isGameOver = true
            }
        }
    }

    if (isGameOver) {
        GameResultScreen(
            gameMode = gameMode,
            score = score,
            onContinue = {
                val earnedXp = gameMode.xpReward + (score * 5)
                val earnedGems = (score / 2).coerceAtLeast(3)
                onFinishGame(earnedXp, earnedGems)
            }
        )
        return
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onExitGame) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Exit Game")
                }

                Text(
                    text = "${gameMode.iconEmoji} ${gameMode.title}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PolyGoldTertiary.copy(alpha = 0.16f)
                ) {
                    Text(
                        text = "Score: $score",
                        fontWeight = FontWeight.Bold,
                        color = PolyGoldTertiary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (gameMode == GameMode.SPEED_VOCABULARY) {
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { timerSeconds / 30f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (timerSeconds <= 5) PolyRubyHeart else PolyGreenPrimary
                )
                Text(
                    text = "⏱️ $timerSeconds seconds remaining",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (timerSeconds <= 5) PolyRubyHeart else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Game Interactive Sandbox
            when (gameMode) {
                GameMode.WORD_MATCHING, GameMode.MEMORY_CARDS -> {
                    Text(
                        text = "Match the Spanish word with its English translation:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val allTiles = listOf("¡Hola!", "Hello", "Gracias", "Thank you", "Por favor", "Please", "Amigo", "Friend")
                        allTiles.forEach { tile ->
                            val isMatched = matchedMemory.contains(tile)
                            val isSelected = selectedMemoryCard == tile

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(100.dp, 60.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        when {
                                            isMatched -> PolyGreenPrimary.copy(alpha = 0.2f)
                                            isSelected -> PolyGoldTertiary.copy(alpha = 0.25f)
                                            else -> MaterialTheme.colorScheme.surface
                                        }
                                    )
                                    .border(
                                        2.dp,
                                        when {
                                            isMatched -> PolyGreenPrimary
                                            isSelected -> PolyGoldTertiary
                                            else -> MaterialTheme.colorScheme.outlineVariant
                                        },
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable(enabled = !isMatched) {
                                        if (selectedMemoryCard == null) {
                                            selectedMemoryCard = tile
                                        } else {
                                            // Check pair
                                            val first = selectedMemoryCard!!
                                            val isPair = (first == "¡Hola!" && tile == "Hello") || (first == "Hello" && tile == "¡Hola!") ||
                                                    (first == "Gracias" && tile == "Thank you") || (first == "Thank you" && tile == "Gracias") ||
                                                    (first == "Por favor" && tile == "Please") || (first == "Please" && tile == "Por favor") ||
                                                    (first == "Amigo" && tile == "Friend") || (first == "Friend" && tile == "Amigo")
                                            if (isPair) {
                                                matchedMemory.add(first)
                                                matchedMemory.add(tile)
                                                score += 10
                                                if (matchedMemory.size >= allTiles.size) {
                                                    isGameOver = true
                                                }
                                            }
                                            selectedMemoryCard = null
                                        }
                                    }
                            ) {
                                Text(
                                    text = tile,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMatched) PolyGreenPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                GameMode.LISTENING_CHALLENGE -> {
                    Text(
                        text = "Listen carefully to Poly's audio and identify the phrase:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    IconButton(
                        onClick = { audioManager.speak("Buenos días, amigo", targetLanguageCode) },
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(PolyGreenPrimary.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Audio Play",
                            tint = PolyGreenPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    val options = listOf("Buenos días, amigo", "Buenas noches, señor", "Hasta luego, adiós", "¿Cómo estás hoy?")
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        options.forEach { opt ->
                            Button(
                                onClick = {
                                    if (opt == "Buenos días, amigo") {
                                        score += 15
                                    }
                                    if (currentRound >= totalRounds) isGameOver = true else currentRound++
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(text = opt, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                GameMode.PRONUNCIATION_CHALLENGE -> {
                    Text(
                        text = "Pronounce this target sentence accurately:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "¡Muchas gracias!",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = PolyGreenPrimary
                    )
                    Text(text = "[moo-chas gra-see-as]", color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(24.dp))

                    IconButton(
                        onClick = {
                            val eval = PronunciationEvaluator.evaluate("Muchas gracias", "Muchas gracias")
                            score += 20
                            if (currentRound >= 3) isGameOver = true else currentRound++
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(PolyRubyHeart)
                    ) {
                        Icon(imageVector = Icons.Default.Mic, contentDescription = "Mic", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Tap to speak and score accent")
                }

                else -> { // PICTURE_GUESS, SPEED_VOCABULARY, TRANSLATION_BATTLE, DAILY_QUIZ, CONVERSATION_CHALLENGE, SENTENCE_BUILDER
                    Text(
                        text = "Round $currentRound of $totalRounds",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "What is the correct translation of:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (currentRound % 2 == 0) "¿De dónde eres?" else "Por favor",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = PolyGreenPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    val answers = if (currentRound % 2 == 0) {
                        listOf("Where are you from?", "What is your name?", "See you tomorrow", "Good evening")
                    } else {
                        listOf("Please", "Thank you", "Goodbye", "Hello")
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        answers.forEach { ans ->
                            Button(
                                onClick = {
                                    val isRight = (currentRound % 2 == 0 && ans == "Where are you from?") || (currentRound % 2 != 0 && ans == "Please")
                                    if (isRight) score += 10
                                    if (currentRound >= totalRounds) isGameOver = true else currentRound++
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(text = ans, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameResultScreen(
    gameMode: GameMode,
    score: Int,
    onContinue: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PolyMascot(
                size = 110.dp,
                mood = PolyMood.CHEERING,
                speechBubbleText = "Awesome match! Poly is so proud of your agility! 🦜🔥"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Challenge Complete!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = PolyGreenPrimary
            )

            Text(
                text = gameMode.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "⭐", fontSize = 26.sp)
                        Text(text = "$score PTS", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(text = "Final Score", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "⚡", fontSize = 26.sp)
                        Text(text = "+${gameMode.xpReward} XP", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PolyGreenPrimary)
                        Text(text = "Bonus Earned", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "💎", fontSize = 26.sp)
                        Text(text = "+${(score / 2).coerceAtLeast(3)} Gems", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PolyGoldTertiary)
                        Text(text = "Rewards", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("game_finish_continue_button"),
                colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Collect Rewards & Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
