package com.sairajpatil108.dailyworkout.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sairajpatil108.dailyworkout.ViewModel.*
import com.sairajpatil108.dailyworkout.data.*
import com.sairajpatil108.dailyworkout.Presentation.components.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    workoutViewModel: WorkoutViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToExerciseDetail: (Exercise) -> Unit
) {
    val workoutUiState by workoutViewModel.uiState.collectAsState()
    val currentSession by workoutViewModel.currentSession.collectAsState()
    val exerciseProgress by workoutViewModel.exerciseProgress.collectAsState()
    val currentExerciseIndex by workoutViewModel.currentExerciseIndex.collectAsState()
    val isWorkoutActive by workoutViewModel.isWorkoutActive.collectAsState()
    val workoutTimer by workoutViewModel.workoutTimer.collectAsState()

    var showExerciseDialog by remember { mutableStateOf<Exercise?>(null) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    // Timer for workout duration
    var elapsedTime by remember { mutableStateOf(0L) }
    
    LaunchedEffect(isWorkoutActive, workoutTimer) {
        if (isWorkoutActive && workoutTimer > 0) {
            while (isWorkoutActive) {
                delay(1000L)
                elapsedTime = (System.currentTimeMillis() - workoutTimer) / 1000
            }
        }
    }

    // Show completion dialog when workout is completed
    LaunchedEffect(workoutUiState.isWorkoutCompleted) {
        if (workoutUiState.isWorkoutCompleted) {
            showCompletionDialog = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar with timer and controls
        WorkoutTopBar(
            onNavigateBack = onNavigateBack,
            isWorkoutActive = isWorkoutActive,
            elapsedTime = elapsedTime,
            currentExercise = currentExerciseIndex + 1,
            totalExercises = workoutUiState.todaysExercises.size,
            onPauseResume = {
                if (isWorkoutActive) workoutViewModel.pauseWorkout()
                else workoutViewModel.resumeWorkout()
            }
        )

        if (workoutUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (workoutUiState.isRestDay) {
            RestDayContent(onNavigateBack)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // Progress indicator
                    WorkoutProgressCard(
                        currentSession = currentSession,
                        totalExercises = workoutUiState.todaysExercises.size,
                        completedExercises = exerciseProgress.count { it.isCompleted }
                    )
                }

                itemsIndexed(workoutUiState.todaysExercises) { index, exercise ->
                    val progress = exerciseProgress.find { it.exerciseName == exercise.exerciseName }
                    ExerciseCard(
                        exercise = exercise,
                        progress = progress,
                        isActive = index == currentExerciseIndex,
                        onExerciseClick = { showExerciseDialog = exercise },
                        onCompleteExercise = { sets, reps, weight, notes ->
                            workoutViewModel.completeExercise(exercise.exerciseName, sets, reps, weight, notes)
                        }
                    )
                }

                item {
                    // Complete workout button
                    if (exerciseProgress.count { it.isCompleted } == workoutUiState.todaysExercises.size &&
                        workoutUiState.todaysExercises.isNotEmpty()) {
                        CompleteWorkoutButton {
                            workoutViewModel.completeWorkout()
                        }
                    }
                }
            }
        }
    }

    // Exercise detail dialog
    showExerciseDialog?.let { exercise ->
        ExerciseDetailDialog(
            exercise = exercise,
            onDismiss = { showExerciseDialog = null },
            onViewFullDetail = {
                showExerciseDialog = null
                onNavigateToExerciseDetail(exercise)
            }
        )
    }

    // Workout completion dialog
    if (showCompletionDialog) {
        WorkoutCompletionDialog(
            duration = workoutUiState.workoutDuration,
            exercisesCompleted = workoutUiState.todaysExercises.size,
            onDismiss = {
                showCompletionDialog = false
                onNavigateBack()
            }
        )
    }

    // Start workout automatically if not started
    LaunchedEffect(workoutUiState.todaysExercises) {
        if (workoutUiState.todaysExercises.isNotEmpty() && !isWorkoutActive && workoutTimer == 0L) {
            workoutViewModel.startWorkout()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutTopBar(
    onNavigateBack: () -> Unit,
    isWorkoutActive: Boolean,
    elapsedTime: Long,
    currentExercise: Int,
    totalExercises: Int,
    onPauseResume: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Workout Session",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatTime(elapsedTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            Text(
                text = "$currentExercise/$totalExercises",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = onPauseResume) {
                Icon(
                    imageVector = if (isWorkoutActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isWorkoutActive) "Pause" else "Resume"
                )
            }
        }
    )
}

@Composable
private fun WorkoutProgressCard(
    currentSession: WorkoutSession?,
    totalExercises: Int,
    completedExercises: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$completedExercises/$totalExercises",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = if (totalExercises > 0) completedExercises.toFloat() / totalExercises else 0f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: Exercise,
    progress: ExerciseProgress?,
    isActive: Boolean,
    onExerciseClick: () -> Unit,
    onCompleteExercise: (Int, Int, Float, String) -> Unit
) {
    var showInputDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                progress?.isCompleted == true -> Color(0xFFE8F5E8)
                isActive -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isActive) CardDefaults.outlinedCardBorder() else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.exerciseName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = exercise.targetMuscles,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${exercise.sets} sets × ${exercise.reps} reps",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                if (progress?.isCompleted == true) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onExerciseClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Guide")
                }
                
                if (progress?.isCompleted != true) {
                    Button(
                        onClick = { showInputDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Complete")
                    }
                }
            }
            
            // Show progress details if completed
            progress?.let { prog ->
                if (prog.isCompleted) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Completed: ${prog.setsCompleted} sets, ${prog.reps} reps" +
                                if (prog.weight > 0) ", ${prog.weight}kg" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
    
    // Exercise completion input dialog
    if (showInputDialog) {
        ExerciseCompletionDialog(
            exercise = exercise,
            onDismiss = { showInputDialog = false },
            onComplete = { sets, reps, weight, notes ->
                onCompleteExercise(sets, reps, weight, notes)
                showInputDialog = false
            }
        )
    }
}

@Composable
private fun CompleteWorkoutButton(onComplete: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50)
        )
    ) {
        Button(
            onClick = onComplete,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Complete Workout 🎉",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RestDayContent(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SelfImprovement,
                contentDescription = "Rest Day",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Rest Day 🧘‍♂️",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Take time to recover and prepare for tomorrow!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onNavigateBack) {
                Text("Back to Home")
            }
        }
    }
}

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
} 