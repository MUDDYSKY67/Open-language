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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AchievementBadge
import com.example.data.model.SupportedLanguages
import com.example.data.model.UserProfile
import com.example.ui.components.PolyMascot
import com.example.ui.components.PolyMood
import com.example.ui.theme.PolyGoldTertiary
import com.example.ui.theme.PolyGreenPrimary
import com.example.ui.theme.PolyOrangeFlame
import com.example.ui.theme.PolyRubyHeart

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    achievements: List<AchievementBadge>,
    onUpdateSettings: (speed: Float, soundEffects: Boolean, isDark: Boolean, dailyGoal: Int) -> Unit,
    onResetOnboarding: () -> Unit
) {
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val currentLang = SupportedLanguages.getById(userProfile.targetLanguageId)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // User Identity & Poly Mascot Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PolyMascot(size = 80.dp, mood = PolyMood.CHEERING, showHalo = false)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Text(
                            text = userProfile.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PolyGreenPrimary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Learning ${currentLang.flagEmoji} ${currentLang.name}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = PolyGreenPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Learning Statistics Cards Grid
        item {
            Text(
                text = "📊 Learning Progress & Stats",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMetricCard(
                    icon = "🔥",
                    value = "${userProfile.streakDays} Days",
                    label = "Streak",
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    icon = "⚡",
                    value = "${userProfile.totalXp} XP",
                    label = "Total Points",
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    icon = "💎",
                    value = "${userProfile.gems}",
                    label = "Gems",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMetricCard(
                    icon = "📖",
                    value = "${userProfile.wordsLearnedCount}",
                    label = "Words Learned",
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    icon = "🎯",
                    value = "94%",
                    label = "Accuracy",
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    icon = "⏱️",
                    value = "${userProfile.dailyGoalMinutes}m",
                    label = "Daily Goal",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Achievements & Badges Showcase
        item {
            Text(
                text = "🏆 Achievements & Badges",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                achievements.forEach { badge ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = badge.iconEmoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = badge.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (badge.isUnlocked) PolyGoldTertiary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = badge.tierLabel,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (badge.isUnlocked) PolyGoldTertiary else MaterialTheme.colorScheme.outline,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = badge.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { (badge.currentProgress.toFloat() / badge.targetProgress).coerceIn(0f, 1f) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (badge.isUnlocked) PolyGreenPrimary else PolyGoldTertiary
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${badge.currentProgress}/${badge.targetProgress}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Account & Settings Menu
        item {
            Text(
                text = "⚙️ Preferences & System",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingsActionRow(
                    icon = Icons.Default.Speed,
                    title = "Audio, Voice Speed & Effects",
                    subtitle = "TTS rate: ${userProfile.speechSpeed}x • Sounds: ${if (userProfile.soundEffectsEnabled) "ON" else "OFF"}",
                    onClick = { showSettingsDialog = true }
                )

                SettingsActionRow(
                    icon = Icons.Default.DarkMode,
                    title = "App Theme & Display",
                    subtitle = if (userProfile.isDarkMode) "Dark Theme active" else "Light Theme active",
                    onClick = {
                        onUpdateSettings(
                            userProfile.speechSpeed,
                            userProfile.soundEffectsEnabled,
                            !userProfile.isDarkMode,
                            userProfile.dailyGoalMinutes
                        )
                    }
                )

                SettingsActionRow(
                    icon = Icons.Default.Info,
                    title = "Restart Learning Onboarding",
                    subtitle = "Reconfigure native and target languages or daily goals",
                    onClick = { onResetOnboarding() }
                )

                SettingsActionRow(
                    icon = Icons.Default.Lock,
                    title = "Account & Privacy",
                    subtitle = "Signed in as ${userProfile.email}",
                    onClick = { showLogoutDialog = true }
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Audio & Voice Speed Dialog
    if (showSettingsDialog) {
        var localSpeed by remember { mutableFloatStateOf(userProfile.speechSpeed) }
        var localSounds by remember { mutableStateOf(userProfile.soundEffectsEnabled) }
        var localGoal by remember { mutableStateOf(userProfile.dailyGoalMinutes) }

        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("🔊 Audio & Voice Settings") },
            text = {
                Column {
                    Text("Voice Playback Speed: ${String.format("%.1fx", localSpeed)}", fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = localSpeed,
                        onValueChange = { localSpeed = it },
                        valueRange = 0.6f..1.4f,
                        steps = 7
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Sound Effects & Chimes", fontWeight = FontWeight.SemiBold)
                        Switch(
                            checked = localSounds,
                            onCheckedChange = { localSounds = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Daily Goal: $localGoal min / day", fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(5, 10, 15, 25).forEach { min ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (localGoal == min) PolyGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable { localGoal = min }
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${min}m",
                                    color = if (localGoal == min) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateSettings(localSpeed, localSounds, userProfile.isDarkMode, localGoal)
                        showSettingsDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary)
                ) {
                    Text("Save Settings")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSettingsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Account Management") },
            text = { Text("OPEN LANGUAGE stores your progress locally on this device. Would you like to switch user profiles or log out?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onResetOnboarding()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PolyRubyHeart)
                ) {
                    Text("Switch Profile")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun StatMetricCard(
    icon: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SettingsActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = PolyGreenPrimary)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}
