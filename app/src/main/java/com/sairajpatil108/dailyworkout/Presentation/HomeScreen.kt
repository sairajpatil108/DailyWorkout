package com.sairajpatil108.dailyworkout.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sairajpatil108.dailyworkout.ViewModel.*
import com.sairajpatil108.dailyworkout.data.*
import com.sairajpatil108.dailyworkout.ui.theme.*
import com.sairajpatil108.dailyworkout.Presentation.components.UserAvatar
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    workoutViewModel: WorkoutViewModel = viewModel(),
    progressViewModel: ProgressViewModel = viewModel(),
    authViewModel: com.sairajpatil108.dailyworkout.ViewModel.AuthViewModel,
    onNavigateToWorkout: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToExerciseDetail: (Exercise) -> Unit,
    onSignOut: () -> Unit
) {
    val workoutUiState by workoutViewModel.uiState.collectAsState()
    val currentSession by workoutViewModel.currentSession.collectAsState()
    val userStats by progressViewModel.userStats.collectAsState()
    val progressUiState by progressViewModel.uiState.collectAsState()
    val weeklyProgress by progressViewModel.weeklyProgress.collectAsState()

    // Memoize expensive calculations
    val currentDate = remember {
        SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault()).format(Date())
    }
    
    // Memoize gradient brush to avoid recreation
    val headerGradient = remember {
        Brush.horizontalGradient(
            colors = listOf(
                PrimaryPurple.copy(alpha = 0.1f),
                LimeGreen.copy(alpha = 0.1f)
            )
        )
    }

    // Set up callback to refresh progress when workout is completed
    LaunchedEffect(Unit) {
        workoutViewModel.onWorkoutCompleted = {
            progressViewModel.forceRefreshWeeklyProgress()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item(key = "header") {
            // Modern Header with gradient background
            ModernHeaderSection(
                user = authViewModel.authState.user,
                currentDate = currentDate,
                headerGradient = headerGradient,
                onSignOut = onSignOut
            )
        }

        item(key = "hero_stats") {
            // Hero Stats Card with glassmorphism effect
            userStats?.let { stats ->
                HeroStatsCard(
                    stats = stats,
                    weeklyProgress = weeklyProgress,
                    onProgressClick = onNavigateToProgress,
                    onDashboardClick = onNavigateToDashboard
                )
            }
        }

        item(key = "workout_card") {
            // Today's workout status with modern design
            ModernWorkoutCard(
                workoutUiState = workoutUiState,
                currentSession = currentSession,
                onStartWorkout = onNavigateToWorkout
            )
        }

        if (!workoutUiState.isRestDay && workoutUiState.todaysExercises.isNotEmpty()) {
            item(key = "exercises_header") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Exercises",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = "${workoutUiState.todaysExercises.size} exercises",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            items(
                items = workoutUiState.todaysExercises,
                key = { exercise -> exercise.exerciseName }
            ) { exercise ->
                ModernExerciseCard(
                    exercise = exercise,
                    onClick = { onNavigateToExerciseDetail(exercise) }
                )
            }
        }

        workoutUiState.error?.let { error ->
            item(key = "error") {
                ModernErrorCard(
                    error = error,
                    onDismiss = { workoutViewModel.clearError() }
                )
            }
        }
    }
}

@Composable
private fun ModernHeaderSection(
    user: com.google.firebase.auth.FirebaseUser?,
    currentDate: String,
    headerGradient: Brush,
    onSignOut: () -> Unit
) {
    var showProfileDialog by remember { mutableStateOf(false) }
    
    // Memoize greeting text to avoid recalculation
    val greetingText = remember(user?.displayName) {
        "Good morning${user?.displayName?.let { ", ${it.split(" ").firstOrNull()}" } ?: ""}! 💪"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    headerGradient,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = greetingText,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentDate,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Clickable User Avatar that opens profile dialog
                UserAvatar(
                    user = user,
                    size = 52.dp,
                    showBorder = true,
                    onClick = { showProfileDialog = true }
                )
            }
        }
    }
    
    // Profile Dialog
    if (showProfileDialog) {
        ProfileDialog(
            user = user,
            onDismiss = { showProfileDialog = false },
            onSignOut = {
                showProfileDialog = false
                onSignOut()
            }
        )
    }
}

@Composable
private fun HeroStatsCard(
    stats: UserStats,
    weeklyProgress: Map<String, Boolean>,
    onProgressClick: () -> Unit,
    onDashboardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProgressClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Header with action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Your Progress",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Tap to explore more",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onDashboardClick,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(12.dp)
                            )
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Dashboard",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    IconButton(
                        onClick = onProgressClick,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                RoundedCornerShape(12.dp)
                            )
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "Progress",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ModernStatItem(
                    icon = Icons.Default.LocalFireDepartment,
                    label = "Current Streak",
                    value = "${stats.currentStreak}",
                    unit = "days",
                    color = Color(0xFFFF6B35),
                    backgroundColor = Color(0xFFFF6B35).copy(alpha = 0.1f)
                )
                ModernStatItem(
                    icon = Icons.Default.EmojiEvents,
                    label = "Best Streak", 
                    value = "${stats.longestStreak}",
                    unit = "days",
                    color = Color(0xFFFFD700),
                    backgroundColor = Color(0xFFFFD700).copy(alpha = 0.1f)
                )
                ModernStatItem(
                    icon = Icons.Default.FitnessCenter,
                    label = "Workouts",
                    value = "${stats.totalWorkouts}",
                    unit = "total",
                    color = PrimaryPurple,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Weekly progress indicator
            OptimizedWeeklyProgress(weeklyProgress)
        }
    }
}

@Composable
private fun ModernStatItem(
    icon: ImageVector,
    label: String,
    value: String,
    unit: String,
    color: Color,
    backgroundColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(backgroundColor, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = unit,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun OptimizedWeeklyProgress(weeklyProgress: Map<String, Boolean>) {
    // Memoize expensive calculations to avoid recomputation
    val completedCount = remember(weeklyProgress) {
        weeklyProgress.values.count { it }
    }
    
    val dayMappings = remember {
        listOf(
            "Mon" to "Monday",
            "Tue" to "Tuesday", 
            "Wed" to "Wednesday",
            "Thu" to "Thursday",
            "Fri" to "Friday",
            "Sat" to "Saturday"
        )
    }
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "This Week's Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "$completedCount/6",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Optimized grid layout with fixed item size
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dayMappings.forEach { (shortDay, fullDay) ->
                val isCompleted = weeklyProgress[fullDay] ?: false
                
                OptimizedDayItem(
                    shortDay = shortDay,
                    isCompleted = isCompleted
                )
            }
        }
    }
}

@Composable
private fun OptimizedDayItem(
    shortDay: String,
    isCompleted: Boolean
) {
    // Fixed size to avoid layout calculations
    val itemSize = 44.dp
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(itemSize),
            colors = CardDefaults.cardColors(
                containerColor = if (isCompleted) SuccessGreen else MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isCompleted) 4.dp else 1.dp
            ),
            shape = RoundedCornerShape(12.dp)
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
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = shortDay.take(1),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = shortDay,
            style = MaterialTheme.typography.labelSmall,
            color = if (isCompleted) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ModernWorkoutCard(
    workoutUiState: WorkoutUiState,
    currentSession: WorkoutSession?,
    onStartWorkout: () -> Unit
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
            Text(
                text = "Today's Workout",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when {
                workoutUiState.isRestDay -> {
                    ModernRestDayContent()
                }
                currentSession?.isCompleted == true -> {
                    ModernCompletedContent(currentSession)
                }
                workoutUiState.todaysExercises.isNotEmpty() -> {
                    ModernActiveWorkoutContent(
                        exerciseCount = workoutUiState.todaysExercises.size,
                        completedCount = currentSession?.completedExercises ?: 0,
                        onStartWorkout = onStartWorkout
                    )
                }
                else -> {
                    LoadingContent()
                }
            }
        }
    }
}

@Composable
private fun ModernRestDayContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SelfImprovement,
                contentDescription = "Rest Day",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Rest Day 🧘‍♂️",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Take time to recover and prepare for tomorrow!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ModernCompletedContent(session: WorkoutSession) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                SuccessGreen.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completed",
                modifier = Modifier.size(64.dp),
                tint = SuccessGreen
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Workout Completed! 🎉",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SuccessGreen
            )
            Text(
                text = "Duration: ${session.duration} minutes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ModernActiveWorkoutContent(
    exerciseCount: Int,
    completedCount: Int,
    onStartWorkout: () -> Unit
) {
    // Memoize text calculations
    val exerciseText = remember(exerciseCount) { "$exerciseCount exercises planned" }
    val completedText = remember(completedCount, exerciseCount) { "$completedCount/$exerciseCount completed" }
    val progressValue = remember(completedCount, exerciseCount) { 
        if (exerciseCount > 0) completedCount.toFloat() / exerciseCount else 0f 
    }
    val buttonText = remember(completedCount) { 
        if (completedCount > 0) "Continue Workout" else "Start Workout" 
    }
    val buttonIcon = remember(completedCount) { 
        if (completedCount > 0) Icons.Default.PlayArrow else Icons.Default.FitnessCenter 
    }
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = exerciseText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                if (completedCount > 0) {
                    Text(
                        text = completedText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            // Simplified time badge
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Time",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "45-60 min",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        if (completedCount > 0) {
            Spacer(modifier = Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = progressValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Button(
            onClick = onStartWorkout,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = buttonIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Loading today's workout...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ModernExerciseCard(
    exercise: Exercise,
    onClick: () -> Unit
) {
    // Memoize the sets and reps text to avoid string concatenation on every recomposition
    val setsText = remember(exercise.sets) { "${exercise.sets} sets" }
    val repsText = remember(exercise.reps) { "${exercise.reps} reps" }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exercise icon/indicator with fixed background
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = "Exercise",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.exerciseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = exercise.targetMuscles,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                
                // Simplified tag layout without nested Surfaces
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExerciseTag(
                        text = setsText,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        textColor = MaterialTheme.colorScheme.primary
                    )
                    
                    ExerciseTag(
                        text = repsText,
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                        textColor = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "View Exercise",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ExerciseTag(
    text: String,
    backgroundColor: Color,
    textColor: Color
) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ModernErrorCard(
    error: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = error,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium
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

@Composable
private fun ProfileDialog(
    user: com.google.firebase.auth.FirebaseUser?,
    onDismiss: () -> Unit,
    onSignOut: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Profile Photo/Avatar
                UserAvatar(
                    user = user,
                    size = 80.dp,
                    showBorder = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // User Info
                user?.displayName?.let { name ->
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                user?.email?.let { email ->
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Profile Actions
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Sign Out Option
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSignOut() },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Sign Out",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Sign Out",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Close Button
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
} 