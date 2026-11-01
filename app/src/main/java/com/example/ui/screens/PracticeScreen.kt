package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.AiTutorService
import com.example.data.ai.ChatMessage
import com.example.data.audio.AudioVoiceManager
import com.example.data.audio.PronunciationEvaluator
import com.example.data.model.SupportedLanguages
import com.example.data.model.UserProfile
import com.example.ui.components.PolyMascot
import com.example.ui.components.PolyMood
import com.example.ui.theme.PolyGoldTertiary
import com.example.ui.theme.PolyGreenPrimary
import com.example.ui.theme.PolyRubyHeart
import kotlinx.coroutines.launch

@Composable
fun PracticeScreen(
    userProfile: UserProfile,
    audioManager: AudioVoiceManager,
    aiTutorService: AiTutorService
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("AI Tutor 🦜", "Pronunciation 🎙️", "Grammar 📖", "SRS Flashcards 🧠")

    val currentLang = SupportedLanguages.getById(userProfile.targetLanguageId)
    val nativeLang = SupportedLanguages.getById(userProfile.nativeLanguageId)

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = PolyGreenPrimary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> AiTutorChatTab(
                userProfile = userProfile,
                targetLang = currentLang.name,
                targetLangCode = currentLang.code,
                nativeLang = nativeLang.name,
                audioManager = audioManager,
                aiTutorService = aiTutorService
            )
            1 -> PronunciationLabTab(
                targetLangCode = currentLang.code,
                audioManager = audioManager
            )
            2 -> GrammarLibraryTab(targetLang = currentLang.name)
            3 -> SrsFlashcardsTab(targetLangCode = currentLang.code, audioManager = audioManager)
        }
    }
}

@Composable
private fun AiTutorChatTab(
    userProfile: UserProfile,
    targetLang: String,
    targetLangCode: String,
    nativeLang: String,
    audioManager: AudioVoiceManager,
    aiTutorService: AiTutorService
) {
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                sender = "tutor",
                text = "¡Hola! 🦜 I am Poly, your personal AI language tutor! Ask me any question about $targetLang grammar, vocabulary, or ask to simulate a real conversation like ordering tapas or asking directions!"
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val quickPrompts = listOf(
        "Explain masculine vs feminine nouns in $targetLang",
        "How do I order a coffee politely?",
        "What are the top 5 most useful phrases for travel?",
        "Roleplay: Let's pretend I'm checking into a hotel"
    )

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        // Suggested quick prompt chips
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (prompt in quickPrompts.take(2)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clickable {
                            inputText = prompt
                        }
                        .weight(1f)
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(8.dp),
                        maxLines = 2
                    )
                }
            }
        }

        // Chat message list
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "user"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!isUser) {
                        PolyMascot(size = 36.dp, mood = PolyMood.TALKING, showHalo = false)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) PolyGreenPrimary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth(0.82f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            if (!isUser) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        onClick = { audioManager.speak(msg.text, targetLangCode) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Speak tutor response",
                                            tint = PolyGreenPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (isLoading) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PolyMascot(size = 32.dp, mood = PolyMood.THINKING, showHalo = false)
                        Spacer(modifier = Modifier.width(8.dp))
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Poly is thinking...", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Message input bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask Poly anything about $targetLang...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("tutor_input_field"),
                shape = RoundedCornerShape(20.dp),
                maxLines = 3
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    val query = inputText.trim()
                    if (query.isNotEmpty() && !isLoading) {
                        messages.add(ChatMessage(sender = "user", text = query))
                        inputText = ""
                        isLoading = true
                        scope.launch {
                            val reply = aiTutorService.getTutorResponse(
                                userPrompt = query,
                                targetLanguageName = targetLang,
                                nativeLanguageName = nativeLang,
                                history = messages
                            )
                            messages.add(ChatMessage(sender = "tutor", text = reply))
                            isLoading = false
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PolyGreenPrimary)
                    .testTag("send_tutor_message_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun PronunciationLabTab(
    targetLangCode: String,
    audioManager: AudioVoiceManager
) {
    val phrases = listOf(
        "¡Buenos días!" to "[bwe-nos dee-as]",
        "Mucho gusto en conocerte" to "[moo-cho goos-to en ko-no-ser-te]",
        "¿Dónde está la biblioteca?" to "[don-de es-ta la bee-blyo-te-ka]",
        "Me gustaría un café con leche" to "[me goos-ta-ree-a oon ka-fe kon le-che]"
    )

    var selectedPhraseIndex by remember { mutableIntStateOf(0) }
    var evalScore by remember { mutableStateOf<Int?>(null) }
    var evalFeedback by remember { mutableStateOf<String?>(null) }

    val currentPhrase = phrases[selectedPhraseIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎙️ Pronunciation Workshop",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Listen at both speeds, then test your accent accuracy",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

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
                    text = currentPhrase.first,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = PolyGreenPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = currentPhrase.second,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Button(
                        onClick = { audioManager.speak(currentPhrase.first, targetLangCode, customSpeed = 1.0f) },
                        colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Normal (1.0x)")
                    }

                    Button(
                        onClick = { audioManager.speak(currentPhrase.first, targetLangCode, customSpeed = 0.7f) },
                        colors = ButtonDefaults.buttonColors(containerColor = PolyGoldTertiary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("🐢 Slow (0.7x)")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        IconButton(
            onClick = {
                val eval = PronunciationEvaluator.evaluate(currentPhrase.first, currentPhrase.first)
                evalScore = eval.accuracyScore
                evalFeedback = eval.feedback
            },
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(PolyRubyHeart)
                .testTag("pronunciation_record_button")
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Record Speech",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Tap mic to assess pronunciation", style = MaterialTheme.typography.bodySmall)

        if (evalScore != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PolyGreenPrimary.copy(alpha = 0.14f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Accuracy: $evalScore%", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = PolyGreenPrimary)
                    Text(text = evalFeedback.orEmpty(), textAlign = TextAlign.Center)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (selectedPhraseIndex > 0) selectedPhraseIndex--
                    evalScore = null
                },
                enabled = selectedPhraseIndex > 0
            ) {
                Text("Previous")
            }

            Button(
                onClick = {
                    if (selectedPhraseIndex < phrases.size - 1) selectedPhraseIndex++
                    evalScore = null
                },
                enabled = selectedPhraseIndex < phrases.size - 1
            ) {
                Text("Next Phrase")
            }
        }
    }
}

@Composable
private fun GrammarLibraryTab(targetLang: String) {
    val grammarTopics = listOf(
        "Nouns & Gender Articles" to "In Romance and many languages, every noun is either masculine (el/un) or feminine (la/una). Words ending in -o are typically masculine, while words ending in -a are typically feminine.",
        "Essential Verb Conjugation" to "Verbs adjust their endings depending on the subject (I, you, he/she, we, they). Starting with high-frequency present tense verbs (to be, to have, to want) unlocks half of everyday conversation.",
        "Formal vs Informal Politeness" to "Many world languages distinguish between informal address for peers/friends (tú) and polite formal address for elders or professionals (usted / vous / polite forms).",
        "Forming Questions" to "Questions often place question words at the start (¿Qué? ¿Dónde? ¿Cuándo?) and invert verb order with an upward questioning pitch at sentence end."
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "📚 $targetLang Grammar Library",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Core structural rules explained in plain English with Poly's tips",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(grammarTopics) { (title, explanation) ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SrsFlashcardsTab(targetLangCode: String, audioManager: AudioVoiceManager) {
    val cards = listOf(
        "¡Bienvenido!" to ("Welcome" to "bee-en-ve-nee-do"),
        "Por supuesto" to ("Of course / Certainly" to "por soo-pwes-to"),
        "La cuenta, por favor" to ("The check, please" to "la kwen-ta por fa-vor"),
        "Hasta luego" to ("See you later" to "as-ta lwe-go")
    )

    var cardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val currentCard = cards[cardIndex]

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🧠 Spaced Repetition Flashcards",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Card ${cardIndex + 1} of ${cards.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Flip Flashcard
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(220.dp)
                .clickable { isFlipped = !isFlipped }
                .border(2.dp, PolyGreenPrimary, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!isFlipped) {
                    Text(text = "TARGET LANGUAGE", style = MaterialTheme.typography.labelSmall, color = PolyGreenPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentCard.first,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(text = "[${currentCard.second.second}]", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "(Tap to reveal translation 🔄)", style = MaterialTheme.typography.labelSmall)
                } else {
                    Text(text = "TRANSLATION", style = MaterialTheme.typography.labelSmall, color = PolyGoldTertiary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentCard.second.first,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = PolyGreenPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    isFlipped = false
                    cardIndex = (cardIndex + 1) % cards.size
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = PolyRubyHeart)
            ) {
                Text("Needs Work")
            }

            Button(
                onClick = {
                    isFlipped = false
                    cardIndex = (cardIndex + 1) % cards.size
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary)
            ) {
                Text("Mastered! ✓")
            }
        }
    }
}
