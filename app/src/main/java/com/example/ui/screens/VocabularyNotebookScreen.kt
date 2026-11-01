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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.audio.AudioVoiceManager
import com.example.data.model.SavedWord
import com.example.data.model.SupportedLanguages
import com.example.data.model.UserProfile
import com.example.ui.components.PolyMascot
import com.example.ui.components.PolyMood
import com.example.ui.theme.PolyGoldTertiary
import com.example.ui.theme.PolyGreenPrimary

@Composable
fun VocabularyNotebookScreen(
    userProfile: UserProfile,
    savedWords: List<SavedWord>,
    audioManager: AudioVoiceManager,
    onAddWord: (SavedWord) -> Unit,
    onClose: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var filterLearnedOnly by remember { mutableStateOf<Boolean?>(null) }
    var showAddWordDialog by remember { mutableStateOf(false) }

    val currentLang = SupportedLanguages.getById(userProfile.targetLanguageId)

    val filteredWords = savedWords.filter { word ->
        val matchesQuery = word.word.contains(searchQuery, ignoreCase = true) ||
                word.translation.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (filterLearnedOnly) {
            true -> word.isLearned
            false -> !word.isLearned
            null -> true
        }
        matchesQuery && matchesFilter
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddWordDialog = true },
                containerColor = PolyGreenPrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_vocab_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Word")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Vocabulary Notebook",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${currentLang.flagEmoji} ${currentLang.name} • ${savedWords.size} Words",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search word or translation...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_vocab_field"),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipPill(
                    title = "All (${savedWords.size})",
                    isSelected = filterLearnedOnly == null,
                    onClick = { filterLearnedOnly = null }
                )
                FilterChipPill(
                    title = "Reviewing (${savedWords.count { !it.isLearned }})",
                    isSelected = filterLearnedOnly == false,
                    onClick = { filterLearnedOnly = false }
                )
                FilterChipPill(
                    title = "Mastered (${savedWords.count { it.isLearned }})",
                    isSelected = filterLearnedOnly == true,
                    onClick = { filterLearnedOnly = true }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Vocabulary List
            if (filteredWords.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    PolyMascot(size = 90.dp, mood = PolyMood.THINKING)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No saved words match your search.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredWords) { word ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = word.word,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 18.sp,
                                            color = PolyGreenPrimary
                                        )
                                        Text(
                                            text = "[${word.phonetic}]",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (word.isLearned) PolyGreenPrimary.copy(alpha = 0.14f) else PolyGoldTertiary.copy(alpha = 0.14f)
                                        ) {
                                            Text(
                                                text = if (word.isLearned) "Mastered" else "SRS Lvl ${word.srsLevel}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = if (word.isLearned) PolyGreenPrimary else PolyGoldTertiary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(6.dp))

                                        IconButton(
                                            onClick = { audioManager.speak(word.word, userProfile.targetLanguageId) },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(PolyGreenPrimary.copy(alpha = 0.12f))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.VolumeUp,
                                                contentDescription = "Pronounce word",
                                                tint = PolyGreenPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = word.translation,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp
                                )

                                if (word.exampleSentence.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "\"${word.exampleSentence}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = word.exampleTranslation,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Word Dialog
    if (showAddWordDialog) {
        var newWord by remember { mutableStateOf("") }
        var newTranslation by remember { mutableStateOf("") }
        var newPhonetic by remember { mutableStateOf("") }
        var newExample by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddWordDialog = false },
            title = { Text("➕ Add Word to Notebook") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newWord,
                        onValueChange = { newWord = it },
                        label = { Text("Word in ${currentLang.name}") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTranslation,
                        onValueChange = { newTranslation = it },
                        label = { Text("Translation") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPhonetic,
                        onValueChange = { newPhonetic = it },
                        label = { Text("Phonetic / Pronunciation (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newExample,
                        onValueChange = { newExample = it },
                        label = { Text("Example Sentence (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newWord.isNotBlank() && newTranslation.isNotBlank()) {
                            onAddWord(
                                SavedWord(
                                    id = "word_${System.currentTimeMillis()}",
                                    languageId = userProfile.targetLanguageId,
                                    word = newWord.trim(),
                                    translation = newTranslation.trim(),
                                    phonetic = newPhonetic.trim(),
                                    exampleSentence = newExample.trim(),
                                    exampleTranslation = "",
                                    srsLevel = 1,
                                    isLearned = false
                                )
                            )
                            showAddWordDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary)
                ) {
                    Text("Save Word")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddWordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun FilterChipPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PolyGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
