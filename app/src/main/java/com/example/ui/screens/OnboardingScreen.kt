package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SupportedLanguages
import com.example.ui.components.PolyMascot
import com.example.ui.components.PolyMood
import com.example.ui.theme.PolyGreenPrimary
import com.example.ui.theme.PolyRubyHeart

@Composable
fun OnboardingScreen(
    onCompleteOnboarding: (nativeLang: String, targetLang: String, skillLevel: String, goalMin: Int, motivation: String) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedNativeLang by remember { mutableStateOf("en") }
    var selectedTargetLang by remember { mutableStateOf("es") }
    var selectedSkillLevel by remember { mutableStateOf("Beginner A1") }
    var selectedGoalMin by remember { mutableIntStateOf(15) }
    var selectedMotivation by remember { mutableStateOf("Travel & Culture") }

    val totalSteps = 8

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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Nav & Step Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (step > 1) {
                    IconButton(
                        onClick = { step-- },
                        modifier = Modifier.testTag("onboarding_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }

                LinearProgressIndicator(
                    progress = { step.toFloat() / totalSteps },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PolyGreenPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Text(
                    text = "$step / $totalSteps",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Content
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier.weight(1f),
                label = "OnboardingStep"
            ) { currentStep ->
                when (currentStep) {
                    1 -> StepWelcome(
                        onNext = { step = 2 }
                    )
                    2 -> StepSelectNativeLanguage(
                        selected = selectedNativeLang,
                        onSelect = { selectedNativeLang = it }
                    )
                    3 -> StepSelectTargetLanguage(
                        selected = selectedTargetLang,
                        onSelect = { selectedTargetLang = it }
                    )
                    4 -> StepSelectSkillLevel(
                        selected = selectedSkillLevel,
                        onSelect = { selectedSkillLevel = it }
                    )
                    5 -> StepSelectDailyGoal(
                        selected = selectedGoalMin,
                        onSelect = { selectedGoalMin = it }
                    )
                    6 -> StepSelectMotivation(
                        selected = selectedMotivation,
                        onSelect = { selectedMotivation = it }
                    )
                    7 -> StepPersonalizedPlan(
                        targetLang = selectedTargetLang,
                        skillLevel = selectedSkillLevel,
                        goalMin = selectedGoalMin,
                        motivation = selectedMotivation,
                        onNext = { step = 8 }
                    )
                    8 -> StepReadyToStart(
                        targetLang = selectedTargetLang,
                        onStart = {
                            onCompleteOnboarding(
                                selectedNativeLang,
                                selectedTargetLang,
                                selectedSkillLevel,
                                selectedGoalMin,
                                selectedMotivation
                            )
                        }
                    )
                }
            }

            // Bottom Continue Button (for selection steps)
            if (step in 2..6) {
                Button(
                    onClick = { step++ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("onboarding_continue_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun StepWelcome(onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.poly_hero),
                contentDescription = "Poly Mascot Hero",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Welcome to\nOPEN LANGUAGE!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Learn Any Language. Anywhere. Anytime.",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        PolyMascot(
            size = 90.dp,
            mood = PolyMood.TALKING,
            speechBubbleText = "Hi! I'm Poly the parrot! I'll guide you step by step to fluency! 🦜"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("start_journey_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Let's Get Started! 🚀", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StepSelectNativeLanguage(selected: String, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "What is your native language?",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Poly will explain grammar and translations in this language.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(SupportedLanguages.list) { lang ->
                LanguageSelectionItem(
                    title = "${lang.flagEmoji} ${lang.name} (${lang.nativeName})",
                    isSelected = selected == lang.id,
                    onClick = { onSelect(lang.id) }
                )
            }
        }
    }
}

@Composable
private fun StepSelectTargetLanguage(selected: String, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Which language do you want to learn?",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "All catalog languages are 100% unlocked from Day 1!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(SupportedLanguages.list) { lang ->
                LanguageSelectionItem(
                    title = "${lang.flagEmoji} ${lang.name}",
                    subtitle = lang.description,
                    isSelected = selected == lang.id,
                    onClick = { onSelect(lang.id) }
                )
            }
        }
    }
}

@Composable
private fun StepSelectSkillLevel(selected: String, onSelect: (String) -> Unit) {
    val levels = listOf(
        "Beginner A1" to "I am brand new to this language. Starting from zero!",
        "Elementary A2" to "I know common words and simple greetings.",
        "Intermediate B1" to "I can handle basic conversations and travel scenarios.",
        "Upper Intermediate B2" to "I can discuss complex topics with confidence.",
        "Advanced C1" to "I want to polish nuance, idioms, and natural fluency."
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "What is your current skill level?",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(levels) { (title, desc) ->
                LanguageSelectionItem(
                    title = title,
                    subtitle = desc,
                    isSelected = selected == title,
                    onClick = { onSelect(title) }
                )
            }
        }
    }
}

@Composable
private fun StepSelectDailyGoal(selected: Int, onSelect: (Int) -> Unit) {
    val goals = listOf(
        5 to ("Casual" to "5 min / day • Steady & relaxed pace ☕"),
        10 to ("Regular" to "10 min / day • Perfect for busy schedules 🏃"),
        15 to ("Serious (Recommended)" to "15 min / day • Great balance of depth and habit ⚡"),
        25 to ("Intense" to "25 min / day • Fast-track to fluency! 🎯")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "What is your daily learning goal?",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(goals) { (minutes, info) ->
                val (tier, desc) = info
                LanguageSelectionItem(
                    title = tier,
                    subtitle = desc,
                    isSelected = selected == minutes,
                    onClick = { onSelect(minutes) }
                )
            }
        }
    }
}

@Composable
private fun StepSelectMotivation(selected: String, onSelect: (String) -> Unit) {
    val motivations = listOf(
        "Culture & Travel" to "✈️ Connect with locals, food, and traditions",
        "Career & Business" to "💼 Unlock international work opportunities",
        "Family & Friends" to "❤️ Talk with loved ones and community",
        "Brain Training" to "🧠 Boost memory, focus, and cognitive agility",
        "School & Exams" to "🎓 Prepare for certifications and tests"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Why are you learning?",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(motivations) { (mot, desc) ->
                LanguageSelectionItem(
                    title = mot,
                    subtitle = desc,
                    isSelected = selected == mot,
                    onClick = { onSelect(mot) }
                )
            }
        }
    }
}

@Composable
private fun StepPersonalizedPlan(
    targetLang: String,
    skillLevel: String,
    goalMin: Int,
    motivation: String,
    onNext: () -> Unit
) {
    val lang = SupportedLanguages.getById(targetLang)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PolyMascot(
            size = 90.dp,
            mood = PolyMood.CHEERING,
            speechBubbleText = "Woohoo! Poly designed your custom roadmap for ${lang.name}! 🦜✨"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "📋 Personalized Learning Plan",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("• Target Language: ${lang.flagEmoji} ${lang.name}")
                Text("• Starting Point: $skillLevel")
                Text("• Daily Pace: $goalMin minutes / day")
                Text("• Motivation: $motivation")
                Text("• First Goal: Complete Unit 1 & Earn your first 7-day streak!")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Review First Lesson", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StepReadyToStart(
    targetLang: String,
    onStart: () -> Unit
) {
    val lang = SupportedLanguages.getById(targetLang)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PolyMascot(
            size = 110.dp,
            mood = PolyMood.HAPPY,
            speechBubbleText = "Your first lesson in ${lang.name} is ready! Let's build your streak! 🔥"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Unit 1: Essential Greetings",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "5 interactive questions • XP + Coins rewards",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("start_first_lesson_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Start Lesson 1 ⚡", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LanguageSelectionItem(
    title: String,
    subtitle: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) PolyGreenPrimary else MaterialTheme.colorScheme.outlineVariant
    val bgColor = if (isSelected) PolyGreenPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (isSelected) {
            Text(text = "✓", color = PolyGreenPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}
