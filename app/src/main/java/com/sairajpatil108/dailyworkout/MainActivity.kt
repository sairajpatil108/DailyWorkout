package com.sairajpatil108.dailyworkout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sairajpatil108.dailyworkout.data.*
import com.sairajpatil108.dailyworkout.ui.theme.DailyWorkoutTheme
import com.sairajpatil108.dailyworkout.ViewModel.*
import com.sairajpatil108.dailyworkout.Presentation.*

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		// Get repository from application
		val repository = (application as WorkoutApplication).repository
		
		enableEdgeToEdge()
		setContent {
			DailyWorkoutTheme {
				WorkoutApp(repository = repository)
			}
		}
	}
}

@Composable
fun WorkoutApp(repository: WorkoutRepository) {
	val navController = rememberNavController()
	val authViewModel: AuthViewModel = viewModel(
		factory = AuthViewModelFactory(repository)
	)
	
	// Create ViewModels with repository
	val workoutViewModel: WorkoutViewModel = viewModel(
		factory = WorkoutViewModelFactory(repository)
	)
	val progressViewModel: ProgressViewModel = viewModel(
		factory = ProgressViewModelFactory(repository)
	)
	
	Scaffold(
		modifier = Modifier.fillMaxSize()
	) { innerPadding ->
		WorkoutNavHost(
			navController = navController,
			authViewModel = authViewModel,
			workoutViewModel = workoutViewModel,
			progressViewModel = progressViewModel,
			modifier = Modifier.padding(innerPadding)
		)
	}
}

@Composable
fun WorkoutNavHost(
	navController: NavHostController,
	authViewModel: AuthViewModel,
	workoutViewModel: WorkoutViewModel,
	progressViewModel: ProgressViewModel,
	modifier: Modifier = Modifier
) {
	var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
	val authState = authViewModel.authState

	// Determine start destination based on authentication state
	val startDestination = if (authState.isSignedIn) "home" else "login"

	NavHost(
		navController = navController,
		startDestination = startDestination,
		modifier = modifier
	) {
		composable("login") {
			LoginScreen(
				authViewModel = authViewModel,
				onSignInSuccess = {
					navController.navigate("home") {
						popUpTo("login") { inclusive = true }
					}
				}
			)
		}
		
		composable("home") {
			HomeScreen(
				workoutViewModel = workoutViewModel,
				progressViewModel = progressViewModel,
				authViewModel = authViewModel,
				onNavigateToWorkout = {
					navController.navigate("workout")
				},
				onNavigateToProgress = {
					navController.navigate("progress")
				},
				onNavigateToDashboard = {
					navController.navigate("dashboard")
				},
				onNavigateToExerciseDetail = { exercise ->
					selectedExercise = exercise
					navController.navigate("exercise_detail")
				},
				onSignOut = {
					authViewModel.signOut()
					navController.navigate("login") {
						popUpTo("home") { inclusive = true }
					}
				}
			)
		}
		
		composable("workout") {
			WorkoutScreen(
				workoutViewModel = workoutViewModel,
				onNavigateBack = {
					navController.popBackStack()
				},
				onNavigateToExerciseDetail = { exercise ->
					selectedExercise = exercise
					navController.navigate("exercise_detail")
				}
			)
		}
		
		composable("progress") {
			ProgressScreen(
				progressViewModel = progressViewModel,
				onNavigateBack = {
					navController.popBackStack()
				},
				onNavigateToDashboard = {
					navController.navigate("dashboard")
				}
			)
		}
		
		composable("dashboard") {
			DashboardScreen(
				progressViewModel = progressViewModel,
				onNavigateBack = {
					navController.popBackStack()
				}
			)
		}
		
		composable("exercise_detail") {
			selectedExercise?.let { exercise ->
				ExerciseDetailScreen(
					exercise = exercise,
					workoutViewModel = workoutViewModel,
					onNavigateBack = {
						navController.popBackStack()
					}
				)
			}
		}
	}
}

