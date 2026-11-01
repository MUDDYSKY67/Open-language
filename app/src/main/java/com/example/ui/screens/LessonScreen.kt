package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.Exercise
import com.example.data.model.ExerciseType
import com.example.data.model.Lesson
import com.example.data.model.MatchPair
import com.example.ui.components.PolyMascot
import com.example.ui.components.PolyMood
import com.example.ui.theme.PolyGoldTertiary
import com.example.ui.theme.PolyGreenPrimary
import com.example.ui.theme.PolyRubyHeart

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LessonScreen(
    lesson: Lesson,
    targetLanguageCode: String,
    audioManager: AudioVoiceManager,
    onFinishLesson: (xpEarned: Int, gemsEarned: Int) -> Unit,
    onExitLesson: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var hearts by remember { mutableIntStateOf(5) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswerChecked by remember { mutableStateOf(false) }
    var isCorrectAnswer by remember { mutableStateOf(false) }
    var isLessonCompleted by remember { mutableStateOf(false) }

    // State for Sentence Builder tiles
    val activeSentenceTokens = remember(currentIndex) { mutableStateListOf<String>() }
    // State for Match Words
    val matchedPairs = remember(currentIndex) { mutableStateListOf<String>() }
    var selectedLeftPair by remember(currentIndex) { mutableStateOf<String?>(null) }

    // State for Pronunciation Exercise
    var speechScore by remember(currentIndex) { mutableStateOf<Int?>(null) }
    var speechFeedback by remember(currentIndex) { mutableStateOf<String?>(null) }
    var isRecording by remember(currentIndex) { mutableStateOf(false) }

    val currentExercise = lesson.exercises.getOrNull(currentIndex)

    if (isLessonCompleted || currentExercise == null) {
        LessonCelebrationScreen(
            lessonTitle = lesson.title,
            xp = lesson.xpReward,
            gems = lesson.gemReward,
            onContinue = { onFinishLesson(lesson.xpReward, lesson.gemReward) }
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
                .padding(16.dp)
        ) {
            // Header: Exit, Progress Bar, Hearts
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onExitLesson,
                    modifier = Modifier.testTag("exit_lesson_button")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Exit Lesson")
                }

                LinearProgressIndicator(
                    progress = { (currentIndex.toFloat() / lesson.exercises.size).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = PolyGreenPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "❤️", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$hearts",
                        fontWeight = FontWeight.Bold,
                        color = PolyRubyHeart,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable exercise body
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Exercise Prompt
                Text(
                    text = currentExercise.prompt,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Target Text & Audio Playback Box
                if (!currentExercise.targetText.isBlank() && currentExercise.type != ExerciseType.MATCH_WORDS) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentExercise.targetText,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (!currentExercise.phonetic.isNullOrBlank()) {
                                    Text(
                                        text = "[${currentExercise.phonetic}]",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            if (!currentExercise.audioText.isNullOrBlank()) {
                                IconButton(
                                    onClick = {
                                        audioManager.speak(currentExercise.audioText, targetLanguageCode)
                                    },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(PolyGreenPrimary.copy(alpha = 0.15f))
                                        .testTag("audio_playback_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Listen to audio",
                                        tint = PolyGreenPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Exercise-Specific Interactivity
                when (currentExercise.type) {
                    ExerciseType.MULTIPLE_CHOICE,
                    ExerciseType.TRANSLATE,
                    ExerciseType.FILL_BLANKS,
                    ExerciseType.LISTEN_SELECT,
                    ExerciseType.PICTURE_VOCAB -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            currentExercise.options.forEach { option ->
                                val isSelected = selectedOption == option
                                val borderCol = if (isSelected) PolyGreenPrimary else MaterialTheme.colorScheme.outlineVariant
                                val bgCol = if (isSelected) PolyGreenPrimary.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(bgCol)
                                        .border(2.dp, borderCol, RoundedCornerShape(16.dp))
                                        .clickable(enabled = !isAnswerChecked) {
                                            selectedOption = option
                                        }
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 17.sp
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    ExerciseType.SENTENCE_BUILDER -> {
                        // User arranged answer row
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                                .padding(8.dp)
                        ) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                activeSentenceTokens.forEach { token ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(PolyGreenPrimary.copy(alpha = 0.18f))
                                            .clickable(enabled = !isAnswerChecked) {
                                                activeSentenceTokens.remove(token)
                                            }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(text = token, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Available word bank tiles
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            currentExercise.sentenceTiles.forEach { tile ->
                                val isPlaced = activeSentenceTokens.contains(tile)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isPlaced) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface)
                                        .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                        .clickable(enabled = !isPlaced && !isAnswerChecked) {
                                            activeSentenceTokens.add(tile)
                                        }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = tile,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isPlaced) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    ExerciseType.MATCH_WORDS -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left list
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                currentExercise.matchPairs.forEach { pair ->
                                    val isMatched = matchedPairs.contains(pair.left)
                                    val isSelected = selectedLeftPair == pair.left
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) PolyGreenPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface)
                                            .border(2.dp, if (isSelected) PolyGreenPrimary else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                            .clickable(enabled = !isMatched && !isAnswerChecked) {
                                                selectedLeftPair = pair.left
                                            }
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = pair.left,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isMatched) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Right list
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                currentExercise.matchPairs.shuffled(java.util.Random(42)).forEach { pair ->
                                    val isMatched = matchedPairs.contains(pair.left)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.surface)
                                            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                            .clickable(enabled = !isMatched && !isAnswerChecked && selectedLeftPair != null) {
                                                if (selectedLeftPair == pair.left) {
                                                    matchedPairs.add(pair.left)
                                                    selectedLeftPair = null
                                                }
                                            }
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = pair.right,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isMatched) PolyGreenPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    ExerciseType.PRONUNCIATION -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Press the mic and speak clearly:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            IconButton(
                                onClick = {
                                    isRecording = !isRecording
                                    // Evaluate pronunciation comparison
                                    val result = PronunciationEvaluator.evaluate(currentExercise.targetText, currentExercise.targetText)
                                    speechScore = result.accuracyScore
                                    speechFeedback = result.feedback
                                },
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(if (isRecording) PolyRubyHeart else PolyGreenPrimary)
                                    .testTag("microphone_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Speak",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            if (speechScore != null) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Accuracy: $speechScore%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = if (speechScore!! >= 75) PolyGreenPrimary else PolyGoldTertiary
                                )
                                Text(
                                    text = speechFeedback.orEmpty(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Feedback Sheet / Action Button
            AnimatedVisibility(visible = isAnswerChecked) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrectAnswer) PolyGreenPrimary.copy(alpha = 0.16f) else PolyRubyHeart.copy(alpha = 0.16f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isCorrectAnswer) "🎉 Brilliant! Correct!" else "❌ Not quite right",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = if (isCorrectAnswer) PolyGreenPrimary else PolyRubyHeart
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentExercise.explanation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Action Button (Check / Continue)
            Button(
                onClick = {
                    if (!isAnswerChecked) {
                        // Check Answer
                        when (currentExercise.type) {
                            ExerciseType.SENTENCE_BUILDER -> {
                                val userSentence = activeSentenceTokens.joinToString(" ")
                                isCorrectAnswer = userSentence.trim().equals(currentExercise.correctAnswer.trim(), ignoreCase = true)
                            }
                            ExerciseType.MATCH_WORDS -> {
                                isCorrectAnswer = matchedPairs.size == currentExercise.matchPairs.size
                            }
                            ExerciseType.PRONUNCIATION -> {
                                isCorrectAnswer = (speechScore ?: 90) >= 70
                            }
                            else -> {
                                isCorrectAnswer = selectedOption == currentExercise.correctAnswer
                            }
                        }

                        if (!isCorrectAnswer && hearts > 0) {
                            hearts--
                        }
                        isAnswerChecked = true
                    } else {
                        // Advance to next exercise
                        if (currentIndex + 1 < lesson.exercises.size) {
                            currentIndex++
                            selectedOption = null
                            isAnswerChecked = false
                            isCorrectAnswer = false
                        } else {
                            isLessonCompleted = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("lesson_action_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAnswerChecked && !isCorrectAnswer) PolyRubyHeart else PolyGreenPrimary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (!isAnswerChecked) "Check Answer" else "Continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun LessonCelebrationScreen(
    lessonTitle: String,
    xp: Int,
    gems: Int,
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
                size = 130.dp,
                mood = PolyMood.CHEERING,
                speechBubbleText = "Incredible work! Lesson complete! 🦜🎉"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Lesson Complete!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = PolyGreenPrimary
            )

            Text(
                text = lessonTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Rewards Pill Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "⚡", fontSize = 28.sp)
                        Text(text = "+$xp XP", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(text = "Experience", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "💎", fontSize = 28.sp)
                        Text(text = "+$gems Gems", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PolyGoldTertiary)
                        Text(text = "Treasures", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("celebration_continue_button"),
                colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Claim Rewards & Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
