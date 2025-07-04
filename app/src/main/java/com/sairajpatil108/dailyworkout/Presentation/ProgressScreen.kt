package com.sairajpatil108.dailyworkout.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sairajpatil108.dailyworkout.ViewModel.*
import com.sairajpatil108.dailyworkout.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    progressViewModel: ProgressViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit = {}
) {
    val progressUiState by progressViewModel.uiState.collectAsState()
    val userStats by progressViewModel.userStats.collectAsState()
    
    // Use reactive StateFlows for real-time updates
    val weeklyProgress by progressViewModel.weeklyProgress.collectAsState()
    val weeklyCompletionRate by progressViewModel.weeklyCompletionRate.collectAsState()
    val monthlyStats by progressViewModel.monthlyStats.collectAsState()
    val workoutFrequency by progressViewModel.workoutFrequency.collectAsState()
    val streakInfo by progressViewModel.streakInfo.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Progress & Stats") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = onNavigateToDashboard) {
                    Icon(Icons.Default.Dashboard, contentDescription = "Dashboard")
                }
                IconButton(onClick = { progressViewModel.refreshData() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        )
     Spacer(Modifier.height(10.dp))
        if (progressUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Overview stats
                    userStats?.let { stats ->
                        OverviewStatsCard(stats = stats)
                    }
                }

                item {
                    // Streak information
                    StreakCard(streakInfo = streakInfo)
                }

                item {
                    // Weekly progress - now reactive
                    WeeklyProgressCard(
                        weeklyProgress = weeklyProgress,
                        completionRate = weeklyCompletionRate
                    )
                }

                item {
                    // Monthly stats - now reactive
                    MonthlyStatsCard(monthlyStats = monthlyStats)
                }

                item {
                    // Workout frequency - now reactive
                    WorkoutFrequencyCard(frequency = workoutFrequency)
                }

                // Error handling
                progressUiState.error?.let { error ->
                    item {
                        ErrorCard(
                            error = error,
                            onDismiss = { progressViewModel.clearError() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OverviewStatsCard(stats: UserStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Your Fitness Journey",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(
                    value = "${stats.totalWorkouts}",
                    label = "Total\nWorkouts",
                    icon = Icons.Default.FitnessCenter,
                    color = Color(0xFF4CAF50)
                )
                StatItem(
                    value = "${stats.currentStreak}",
                    label = "Current\nStreak",
                    icon = Icons.Default.LocalFireDepartment,
                    color = Color(0xFFFF6B35)
                )
                StatItem(
                    value = "${stats.longestStreak}",
                    label = "Best\nStreak",
                    icon = Icons.Default.EmojiEvents,
                    color = Color(0xFFFFD700)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(32.dp),
            tint = color
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StreakCard(streakInfo: StreakInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (streakInfo.isOnStreak) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Streak",
                modifier = Modifier.size(40.dp),
                tint = if (streakInfo.isOnStreak) Color(0xFFFF6B35) else MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (streakInfo.isOnStreak) "You're on fire! 🔥" else "Start your streak!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (streakInfo.isOnStreak) {
                        "${streakInfo.currentStreak} days strong!"
                    } else {
                        "Complete today's workout to start building your streak."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun WeeklyProgressCard(
    weeklyProgress: Map<String, Boolean>,
    completionRate: Float
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "This Week",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(completionRate * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = completionRate,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday").forEach { day ->
                    val isCompleted = weeklyProgress[day] ?: false
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Completed",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = day.take(1),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = day.take(3),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthlyStatsCard(monthlyStats: MonthlyStats) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "This Month",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MonthlyStatItem(
                    value = "${monthlyStats.totalWorkouts}",
                    label = "Workouts"
                )
                MonthlyStatItem(
                    value = "${monthlyStats.totalWorkoutTime}min",
                    label = "Total Time"
                )
                MonthlyStatItem(
                    value = "${monthlyStats.averageWorkoutTime}min",
                    label = "Avg Time"
                )
            }
        }
    }
}

@Composable
private fun MonthlyStatItem(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WorkoutFrequencyCard(frequency: List<DayFrequency>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Workout Frequency",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            frequency.forEach { dayFreq ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dayFreq.day,
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.width(80.dp)
                    )
                    
                    val maxFreq = frequency.maxOfOrNull { it.frequency } ?: 1
                    val progress = if (maxFreq > 0) dayFreq.frequency.toFloat() / maxFreq else 0f
                    
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "${dayFreq.frequency}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(
    error: String,
    onDismiss: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = error,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
} 