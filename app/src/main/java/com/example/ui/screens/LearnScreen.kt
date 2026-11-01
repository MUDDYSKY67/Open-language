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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Course
import com.example.data.model.CourseUnit
import com.example.data.model.Lesson
import com.example.data.model.SupportedLanguages
import com.example.data.model.UserProfile
import com.example.ui.theme.PolyGoldTertiary
import com.example.ui.theme.PolyGreenPrimary

@Composable
fun LearnScreen(
    course: Course,
    userProfile: UserProfile,
    onSelectLesson: (String) -> Unit
) {
    val currentLang = SupportedLanguages.getById(userProfile.targetLanguageId)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Curriculum Path",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = "${currentLang.flagEmoji} ${currentLang.name} • All Units Unlocked",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "CEFR A1",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Serpentine Units & Lessons
        items(course.units) { unit ->
            UnitSectionCard(
                unit = unit,
                completedLessonIds = userProfile.completedLessonIds,
                onSelectLesson = onSelectLesson
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun UnitSectionCard(
    unit: CourseUnit,
    completedLessonIds: Set<String>,
    onSelectLesson: (String) -> Unit
) {
    val completedInUnit = unit.lessons.count { completedLessonIds.contains(it.id) }
    val progressPercent = if (unit.lessons.isEmpty()) 0f else completedInUnit.toFloat() / unit.lessons.size

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Unit Header Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(unit.colorHex).copy(alpha = 0.2f))
                ) {
                    Text(text = unit.iconEmoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "UNIT ${unit.number} • ${unit.cefrLevel.badge}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(unit.colorHex)
                    )
                    Text(
                        text = unit.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = unit.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progressPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(unit.colorHex),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Serpentine Nodes Layout
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                unit.lessons.forEachIndexed { index, lesson ->
                    val isCompleted = completedLessonIds.contains(lesson.id)
                    val isCurrent = !isCompleted && (index == 0 || completedLessonIds.contains(unit.lessons[index - 1].id))

                    // Serpentine horizontal offset pattern (-30dp, 0dp, +30dp, 0dp)
                    val xOffset = when (index % 4) {
                        0 -> 0.dp
                        1 -> (-36).dp
                        2 -> 0.dp
                        else -> 36.dp
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .offset(x = xOffset)
                            .padding(vertical = 8.dp)
                    ) {
                        LessonNode(
                            lesson = lesson,
                            isCompleted = isCompleted,
                            isCurrent = isCurrent,
                            themeColor = Color(unit.colorHex),
                            onClick = { onSelectLesson(lesson.id) }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonNode(
    lesson: Lesson,
    isCompleted: Boolean,
    isCurrent: Boolean,
    themeColor: Color,
    onClick: () -> Unit
) {
    val nodeSize = if (lesson.isCheckpoint) 72.dp else 64.dp
    val bgColor = when {
        isCompleted -> PolyGreenPrimary
        isCurrent -> themeColor
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(nodeSize)
            .shadow(if (isCurrent) 8.dp else 2.dp, CircleShape)
            .clip(CircleShape)
            .background(bgColor)
            .border(
                width = if (isCurrent) 4.dp else 2.dp,
                color = if (isCurrent) PolyGoldTertiary else Color.White.copy(alpha = 0.4f),
                shape = CircleShape
            )
            .clickable { onClick() }
            .testTag("lesson_node_${lesson.id}")
    ) {
        when {
            lesson.isCheckpoint -> {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Checkpoint",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            isCompleted -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            isCurrent -> {
                Text(
                    text = "${lesson.order}",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Upcoming",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
