package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.AiTutorService
import com.example.data.audio.AudioVoiceManager
import com.example.data.model.GameMode
import com.example.data.model.SupportedLanguages
import com.example.data.repository.LearningRepository
import com.example.ui.components.StatsHeaderBar
import com.example.ui.screens.GamePlayScreen
import com.example.ui.screens.GamesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LessonScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.VocabularyNotebookScreen
import com.example.ui.theme.PolyGreenPrimary
import kotlinx.coroutines.launch

enum class MainDestination(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    LEARN("Learn", Icons.Default.MenuBook),
    GAMES("Games", Icons.Default.SportsEsports),
    PRACTICE("Practice", Icons.Default.RecordVoiceOver),
    PROFILE("Profile", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenLanguageApp(
    repository: LearningRepository,
    audioManager: AudioVoiceManager,
    aiTutorService: AiTutorService
) {
    val userProfile by repository.currentProfile.collectAsState()
    val dailyTasks by repository.dailyTasks.collectAsState()
    val achievements by repository.achievements.collectAsState()
    val savedWords by repository.getSavedWordsFlow(userProfile.targetLanguageId).collectAsState(initial = emptyList())

    var currentDestination by remember { mutableStateOf(MainDestination.HOME) }
    var isOnboardingActive by remember { mutableStateOf(false) }

    // Active sub-screen overlays
    var activeLessonId by remember { mutableStateOf<String?>(null) }
    var activeGameMode by remember { mutableStateOf<GameMode?>(null) }
    var isNotebookOpen by remember { mutableStateOf(false) }
    var isLanguageSheetOpen by remember { mutableStateOf(false) }

    val course = remember(userProfile.targetLanguageId) {
        repository.getCourse(userProfile.targetLanguageId)
    }

    // Handle back button for sub-screens
    BackHandler(enabled = activeLessonId != null || activeGameMode != null || isNotebookOpen || isLanguageSheetOpen || currentDestination != MainDestination.HOME) {
        when {
            activeLessonId != null -> activeLessonId = null
            activeGameMode != null -> activeGameMode = null
            isNotebookOpen -> isNotebookOpen = false
            isLanguageSheetOpen -> isLanguageSheetOpen = false
            currentDestination != MainDestination.HOME -> currentDestination = MainDestination.HOME
        }
    }

    // Onboarding Screen
    if (isOnboardingActive) {
        OnboardingScreen(
            onCompleteOnboarding = { nativeLang, targetLang, skillLevel, goalMin, motivation ->
                repository.setNativeLanguage(nativeLang)
                repository.setTargetLanguage(targetLang)
                repository.updateSettings(
                    speechSpeed = userProfile.speechSpeed,
                    soundEffects = userProfile.soundEffectsEnabled,
                    isDarkMode = userProfile.isDarkMode,
                    dailyGoal = goalMin
                )
                isOnboardingActive = false
            }
        )
        return
    }

    // Active Interactive Lesson Screen
    if (activeLessonId != null) {
        val lesson = repository.getLesson(userProfile.targetLanguageId, activeLessonId!!)
        if (lesson != null) {
            LessonScreen(
                lesson = lesson,
                targetLanguageCode = userProfile.targetLanguageId,
                audioManager = audioManager,
                onFinishLesson = { xpEarned, gemsEarned ->
                    repository.addXpAndGems(xpEarned, gemsEarned)
                    repository.markLessonCompleted(lesson.id)
                    activeLessonId = null
                },
                onExitLesson = { activeLessonId = null }
            )
            return
        }
    }

    // Active Game Sandbox Screen
    if (activeGameMode != null) {
        GamePlayScreen(
            gameMode = activeGameMode!!,
            targetLanguageCode = userProfile.targetLanguageId,
            audioManager = audioManager,
            onFinishGame = { earnedXp, earnedGems ->
                repository.addXpAndGems(earnedXp, earnedGems)
                activeGameMode = null
            },
            onExitGame = { activeGameMode = null }
        )
        return
    }

    // Vocabulary Notebook Screen
    if (isNotebookOpen) {
        VocabularyNotebookScreen(
            userProfile = userProfile,
            savedWords = savedWords,
            audioManager = audioManager,
            onAddWord = { word -> repository.addSavedWord(word) },
            onClose = { isNotebookOpen = false }
        )
        return
    }

    // Main Scaffold with Stats Bar and Bottom Navigation
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            StatsHeaderBar(
                userProfile = userProfile,
                onLanguageClick = { isLanguageSheetOpen = true },
                onRefillHearts = { repository.refillHearts() },
                onPolyClick = { isNotebookOpen = true }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                MainDestination.values().forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination == destination,
                        onClick = { currentDestination = destination },
                        icon = {
                            Icon(imageVector = destination.icon, contentDescription = destination.label)
                        },
                        label = { Text(destination.label, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PolyGreenPrimary,
                            selectedTextColor = PolyGreenPrimary,
                            indicatorColor = PolyGreenPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_${destination.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                MainDestination.HOME -> HomeScreen(
                    userProfile = userProfile,
                    course = course,
                    dailyTasks = dailyTasks,
                    audioManager = audioManager,
                    onStartLesson = { lessonId -> activeLessonId = lessonId },
                    onNavigateToLearn = { currentDestination = MainDestination.LEARN },
                    onNavigateToGames = { currentDestination = MainDestination.GAMES },
                    onNavigateToPractice = { currentDestination = MainDestination.PRACTICE }
                )
                MainDestination.LEARN -> LearnScreen(
                    course = course,
                    userProfile = userProfile,
                    onSelectLesson = { lessonId -> activeLessonId = lessonId }
                )
                MainDestination.GAMES -> GamesScreen(
                    userProfile = userProfile,
                    onSelectGame = { mode -> activeGameMode = mode }
                )
                MainDestination.PRACTICE -> PracticeScreen(
                    userProfile = userProfile,
                    audioManager = audioManager,
                    aiTutorService = aiTutorService
                )
                MainDestination.PROFILE -> ProfileScreen(
                    userProfile = userProfile,
                    achievements = achievements,
                    onUpdateSettings = { speed, sound, dark, goal ->
                        repository.updateSettings(speed, sound, dark, goal)
                        audioManager.setSpeed(speed)
                    },
                    onResetOnboarding = { isOnboardingActive = true }
                )
            }
        }
    }

    // Language Selection Modal Bottom Sheet
    if (isLanguageSheetOpen) {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        ModalBottomSheet(
            onDismissRequest = { isLanguageSheetOpen = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Switch Target Language",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Select any language from our 15+ world catalogue:",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SupportedLanguages.list) { lang ->
                        val isSelected = userProfile.targetLanguageId == lang.id
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) PolyGreenPrimary.copy(alpha = 0.14f) else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    repository.setTargetLanguage(lang.id)
                                    scope.launch {
                                        sheetState.hide()
                                        isLanguageSheetOpen = false
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = lang.flagEmoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${lang.name} (${lang.nativeName})",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = lang.description,
                                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Text(text = "✓", color = PolyGreenPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
