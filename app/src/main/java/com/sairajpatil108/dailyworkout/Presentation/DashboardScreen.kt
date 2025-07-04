package com.sairajpatil108.dailyworkout.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sairajpatil108.dailyworkout.ViewModel.*
import com.sairajpatil108.dailyworkout.data.*
import com.sairajpatil108.dailyworkout.ui.theme.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    progressViewModel: ProgressViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val progressUiState by progressViewModel.uiState.collectAsState()
    val userStats by progressViewModel.userStats.collectAsState()
    val allSessions by progressViewModel.allSessions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Modern Top App Bar
        TopAppBar(
            title = { 
                Text(
                    "Analytics Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                ) 
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.Default.ArrowBack, 
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                IconButton(onClick = { progressViewModel.refreshData() }) {
                    Icon(
                        Icons.Default.Refresh, 
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        if (progressUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading analytics...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    // Hero Dashboard Card
                    userStats?.let { stats ->
                        ModernHeroStatsCard(
                            stats = stats,
                            weeklyProgress = progressUiState.weeklyProgress
                        )
                    }
                }

                item {
                    // Weekly Consistency Grid
                    ModernWeeklyConsistencyCard(
                        weeklyProgress = progressUiState.weeklyProgress,
                        completionRate = progressViewModel.getWeeklyCompletionRate()
                    )
                }

                item {
                    // Monthly Overview
                    val monthlyStats = progressViewModel.getMonthlyStats()
                    ModernMonthlyOverviewCard(monthlyStats = monthlyStats)
                }

                item {
                    // Streak Progress
                    userStats?.let { stats ->
                        ModernStreakProgressCard(stats = stats)
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernHeroStatsCard(
    stats: UserStats,
    weeklyProgress: Map<String, Boolean>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryPurple.copy(alpha = 0.1f),
                            LimeGreen.copy(alpha = 0.1f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(28.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Your Fitness Journey",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Analytics & Progress Overview",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Current streak with enhanced styling
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (stats.currentStreak > 0) 
                            Color(0xFFFF6B35).copy(alpha = 0.1f) 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = if (stats.currentStreak > 0) Color(0xFFFF6B35) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${stats.currentStreak}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (stats.currentStreak > 0) Color(0xFFFF6B35) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Enhanced stats grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ModernDashboardStatItem(
                        value = "${stats.totalWorkouts}",
                        label = "Total\nWorkouts",
                        icon = Icons.Default.FitnessCenter,
                        color = SuccessGreen,
                        backgroundColor = SuccessGreen.copy(alpha = 0.1f)
                    )
                    ModernDashboardStatItem(
                        value = "${stats.longestStreak}",
                        label = "Best\nStreak",
                        icon = Icons.Default.EmojiEvents,
                        color = Color(0xFFFFD700),
                        backgroundColor = Color(0xFFFFD700).copy(alpha = 0.1f)
                    )
                    ModernDashboardStatItem(
                        value = "${weeklyProgress.values.count { it }}/${weeklyProgress.size}",
                        label = "This\nWeek",
                        icon = Icons.Default.DateRange,
                        color = PrimaryPurple,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernDashboardStatItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    backgroundColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(backgroundColor, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(32.dp),
                tint = color
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ModernWeeklyConsistencyCard(
    weeklyProgress: Map<String, Boolean>,
    completionRate: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Header section with better spacing
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Weekly Consistency",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Track your workout consistency",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Percentage badge with better positioning
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = when {
                            completionRate >= 0.8f -> SuccessGreen.copy(alpha = 0.15f)
                            completionRate >= 0.6f -> WarningOrange.copy(alpha = 0.15f)
                            else -> ErrorRed.copy(alpha = 0.15f)
                        }
                    ) {
                        Text(
                            text = "${(completionRate * 100).toInt()}%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                completionRate >= 0.8f -> SuccessGreen
                                completionRate >= 0.6f -> WarningOrange
                                else -> ErrorRed
                            },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Progress bar with better styling
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${weeklyProgress.values.count { it }}/6 days",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LinearProgressIndicator(
                        progress = completionRate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = when {
                            completionRate >= 0.8f -> SuccessGreen
                            completionRate >= 0.6f -> WarningOrange
                            else -> ErrorRed
                        },
                        trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // Weekly grid with responsive layout
            Column {
                Text(
                    text = "This Week",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Responsive grid layout
                BoxWithConstraints {
                    val itemWidth = (maxWidth - 50.dp) / 6 // Account for spacing
                    val itemSize = minOf(itemWidth, 56.dp)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday").forEach { day ->
                            val isCompleted = weeklyProgress[day] ?: false
                            ResponsiveWeeklyDayCard(
                                day = day.take(3),
                                isCompleted = isCompleted,
                                size = itemSize
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernWeeklyDayCard(
    day: String,
    isCompleted: Boolean
) {
    Card(
        modifier = Modifier.size(52.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) SuccessGreen else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCompleted) 4.dp else 2.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = PureWhite,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ResponsiveWeeklyDayCard(
    day: String,
    isCompleted: Boolean,
    size: Dp
) {
    Card(
        modifier = Modifier.size(size),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) SuccessGreen else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCompleted) 6.dp else 2.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = PureWhite,
                    modifier = Modifier.size((size * 0.4f).coerceAtLeast(16.dp))
                )
            } else {
                Text(
                    text = day,
                    style = when {
                        size >= 52.dp -> MaterialTheme.typography.labelLarge
                        size >= 40.dp -> MaterialTheme.typography.labelMedium
                        else -> MaterialTheme.typography.labelSmall
                    },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ModernMonthlyOverviewCard(monthlyStats: MonthlyStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Monthly Overview",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Your performance this month",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ModernMonthlyStatCard(
                    value = "${monthlyStats.totalWorkouts}",
                    label = "Workouts",
                    icon = Icons.Default.FitnessCenter,
                    color = SuccessGreen
                )
                ModernMonthlyStatCard(
                    value = "${monthlyStats.totalWorkoutTime}m",
                    label = "Total Time",
                    icon = Icons.Default.Timer,
                    color = Color(0xFF2196F3)
                )
                ModernMonthlyStatCard(
                    value = "${monthlyStats.averageWorkoutTime}m",
                    label = "Avg Time",
                    icon = Icons.Default.Schedule,
                    color = Color(0xFF9C27B0)
                )
                ModernMonthlyStatCard(
                    value = "${(monthlyStats.completionRate * 100).toInt()}%",
                    label = "Success",
                    icon = Icons.Default.TrendingUp,
                    color = WarningOrange
                )
            }
        }
    }
}

@Composable
private fun ModernMonthlyStatCard(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(28.dp),
            tint = color
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun ModernStreakProgressCard(stats: UserStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = Color(0xFFFF6B35),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Streak Progress",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Keep the momentum going!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Streak milestone
            val nextMilestone = when {
                stats.currentStreak < 7 -> 7
                stats.currentStreak < 14 -> 14
                stats.currentStreak < 30 -> 30
                stats.currentStreak < 50 -> 50
                stats.currentStreak < 100 -> 100
                else -> ((stats.currentStreak / 50) + 1) * 50
            }
            
            val progress = if (nextMilestone > 0) stats.currentStreak.toFloat() / nextMilestone else 0f
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFF6B35).copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current: ${stats.currentStreak} days",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Next milestone: $nextMilestone days",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Surface(
                            color = Color(0xFFFF6B35).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "${nextMilestone - stats.currentStreak} to go!",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFFFF6B35),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFFFF6B35),
                        trackColor = Color(0xFFFF6B35).copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}

 