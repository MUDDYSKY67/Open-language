package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.data.model.SupportedLanguages
import com.example.data.model.UserProfile
import com.example.ui.theme.PolyGoldTertiary
import com.example.ui.theme.PolyGreenPrimary
import com.example.ui.theme.PolyOrangeFlame
import com.example.ui.theme.PolyRubyHeart

@Composable
fun StatsHeaderBar(
    userProfile: UserProfile,
    onLanguageClick: () -> Unit,
    onRefillHearts: () -> Unit,
    onPolyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showHeartsDialog by remember { mutableStateOf(false) }
    val currentLang = SupportedLanguages.getById(userProfile.targetLanguageId)

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 3.dp,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Language Track Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onLanguageClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("language_selector_button")
            ) {
                Text(text = currentLang.flagEmoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentLang.name,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(text = " ▾", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // Gamification Counters (Streak, Gems, Hearts)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Streak Flame
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PolyOrangeFlame.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(text = "🔥", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile.streakDays}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = PolyOrangeFlame
                    )
                }

                // Gems Counter
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PolyGoldTertiary.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(text = "💎", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile.gems}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = PolyGoldTertiary
                    )
                }

                // Hearts Counter
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PolyRubyHeart.copy(alpha = 0.12f))
                        .clickable { showHeartsDialog = true }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("hearts_refill_button")
                ) {
                    Text(text = "❤️", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile.hearts}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = PolyRubyHeart
                    )
                }

                // Poly Quick Avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(2.dp, PolyGreenPrimary, CircleShape)
                        .clickable { onPolyClick() }
                        .testTag("poly_avatar_button")
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_mascot_poly),
                        contentDescription = "Poly Mascot",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    // Refill Hearts Modal
    if (showHeartsDialog) {
        AlertDialog(
            onDismissRequest = { showHeartsDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("❤️ Hearts Sanctuary")
                }
            },
            text = {
                Text(
                    "You currently have ${userProfile.hearts} of ${userProfile.maxHearts} hearts. " +
                    "Hearts protect you from lesson mistakes. Refill your hearts instantly using 50 gems or earn more by playing practice games!"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRefillHearts()
                        showHeartsDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PolyGreenPrimary),
                    enabled = userProfile.hearts < userProfile.maxHearts && userProfile.gems >= 50
                ) {
                    Text("Refill with 50 💎")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showHeartsDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
