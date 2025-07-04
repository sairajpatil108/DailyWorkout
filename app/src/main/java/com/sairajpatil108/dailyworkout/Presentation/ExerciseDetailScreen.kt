package com.sairajpatil108.dailyworkout.Presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sairajpatil108.dailyworkout.ViewModel.*
import com.sairajpatil108.dailyworkout.data.*
import com.sairajpatil108.dailyworkout.Presentation.components.*
import androidx.compose.foundation.isSystemInDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exercise: Exercise,
    workoutViewModel: WorkoutViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val workoutUiState by workoutViewModel.uiState.collectAsState()
    var showCompletionDialog by remember { mutableStateOf(false) }

    // Load recent progress for this exercise
    LaunchedEffect(exercise.exerciseName) {
        workoutViewModel.getRecentProgressForExercise(exercise.exerciseName)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(

            title = { Text("Exercise Guide") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                // Complete exercise button
                TextButton(
                    onClick = { showCompletionDialog = true }
                ) {
                    Text("Complete")
                }
            }
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding( horizontal = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Exercise name and basic info
            ExerciseHeader(exercise = exercise)

            // Target muscles
            InfoCard(
                title = "Target Muscles",
                content = exercise.targetMuscles,
                icon = Icons.Default.FitnessCenter,
                color = MaterialTheme.colorScheme.primaryContainer
            )

            // Sets and reps guidance
            InfoCard(
                title = "Sets & Repetitions",
                content = "${exercise.sets} sets × ${exercise.reps} reps\n\n${exercise.weightGuidance}",
                icon = Icons.Default.Repeat,
                color = MaterialTheme.colorScheme.secondaryContainer
            )

            // Proper posture
            InfoCard(
                title = "Proper Posture & Form",
                content = exercise.properPosture,
                icon = Icons.Default.AccessibilityNew,
                color = MaterialTheme.colorScheme.tertiaryContainer
            )

            // Do's section
            if (exercise.dos.isNotEmpty()) {
                DosCard(dos = exercise.dos)
            }

            // Don'ts section
            if (exercise.donts.isNotEmpty()) {
                DontsCard(donts = exercise.donts)
            }

            // Recent progress
            RecentProgressCard(
                recentProgress = workoutUiState.recentProgress,
                exerciseName = exercise.exerciseName
            )

            // Tutorial video section
            exercise.tutorialVideoUrl?.let { videoUrl ->
                TutorialVideoCard(videoUrl = videoUrl)
            }

            // Complete exercise button
            Button(
                onClick = { showCompletionDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Done, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Complete This Exercise")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Exercise completion dialog
    if (showCompletionDialog) {
        ExerciseCompletionDialog(
            exercise = exercise,
            onDismiss = { showCompletionDialog = false },
            onComplete = { sets, reps, weight, notes ->
                workoutViewModel.completeExercise(exercise.exerciseName, sets, reps, weight, notes)
                showCompletionDialog = false
                onNavigateBack() // Go back after completion
            }
        )
    }
}

@Composable
private fun ExerciseHeader(exercise: Exercise) {
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
                text = exercise.exerciseName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = exercise.targetMuscles,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )
        }
    }
}

@Composable
private fun DosCard(dos: List<String>) {
    val isDarkTheme = isSystemInDarkTheme()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                Color(0xFFE8F5E8)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Do's",
                    modifier = Modifier.size(24.dp),
                    tint = if (isDarkTheme) {
                        Color(0xFF4AE07B)
                    } else {
                        Color(0xFF4CAF50)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Do's ✅",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) {
                        Color(0xFF4AE07B)
                    } else {
                        Color(0xFF2E7D32)
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            dos.forEach { item ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDarkTheme) {
                            Color(0xFF4AE07B)
                        } else {
                            Color(0xFF4CAF50)
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DontsCard(donts: List<String>) {
    val isDarkTheme = isSystemInDarkTheme()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                Color(0xFFFEE8E8)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Don'ts",
                    modifier = Modifier.size(24.dp),
                    tint = if (isDarkTheme) {
                        Color(0xFFFF8A8A)
                    } else {
                        Color(0xFFF44336)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Don'ts ❌",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) {
                        Color(0xFFFF8A8A)
                    } else {
                        Color(0xFFD32F2F)
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            donts.forEach { item ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDarkTheme) {
                            Color(0xFFFF8A8A)
                        } else {
                            Color(0xFFF44336)
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentProgressCard(
    recentProgress: List<ExerciseProgress>,
    exerciseName: String
) {
    val exerciseProgress = recentProgress.filter { it.exerciseName == exerciseName }
    
    if (exerciseProgress.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Progress",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Recent Progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                exerciseProgress.take(3).forEach { progress ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${progress.setsCompleted} sets × ${progress.reps} reps",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (progress.weight > 0) {
                            Text(
                                text = "${progress.weight}kg",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TutorialVideoCard(videoUrl: String) {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDarkTheme) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircleOutline,
                    contentDescription = "Video Tutorial",
                    modifier = Modifier.size(28.dp),
                    tint = if (isDarkTheme) {
                        Color(0xFFFF6B6B)
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Video Tutorial",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Watch a detailed video tutorial for proper form and technique.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Watch video button
            Button(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Handle error (invalid URL, no app to handle video, etc.)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkTheme) {
                        Color(0xFFFF6B6B)
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    },
                    contentColor = if (isDarkTheme) {
                        Color(0xFF000000)
                    } else {
                        MaterialTheme.colorScheme.onTertiary
                    }
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isDarkTheme) 6.dp else 2.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Watch Tutorial Video",
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
} 