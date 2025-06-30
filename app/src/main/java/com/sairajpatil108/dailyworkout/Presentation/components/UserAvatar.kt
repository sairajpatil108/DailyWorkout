package com.sairajpatil108.dailyworkout.Presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser

@Composable
fun UserAvatar(
    user: FirebaseUser?,
    size: Dp = 48.dp,
    modifier: Modifier = Modifier,
    showBorder: Boolean = true
) {
    val cornerRadius = size / 2
    val borderModifier = if (showBorder) {
        Modifier.border(
            width = 2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                )
            ),
            shape = RoundedCornerShape(cornerRadius)
        )
    } else {
        Modifier
    }
    
    Box(
        modifier = modifier
            .size(size)
            .then(borderModifier)
    ) {
        if (user?.photoUrl != null) {
            AsyncImage(
                model = user.photoUrl,
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (showBorder) 2.dp else 0.dp)
                    .clip(RoundedCornerShape(cornerRadius)),
                contentScale = ContentScale.Crop
            )
        } else {
            // Fallback to initials if no photo
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (showBorder) 2.dp else 0.dp),
                shape = RoundedCornerShape(cornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user?.displayName?.firstOrNull()?.toString()?.uppercase() ?: "U",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = (size.value * 0.4).sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
} 