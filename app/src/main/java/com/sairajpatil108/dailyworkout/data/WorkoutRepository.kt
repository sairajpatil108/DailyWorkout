package com.sairajpatil108.dailyworkout.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

class WorkoutRepository(
    private val firestoreRepository: FirestoreRepository
) {
    private val workoutPlan = WorkoutData.getWorkoutPlan()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Workout Plan Methods
    fun getExercisesForDay(day: String): List<Exercise> {
        return workoutPlan.weeklySchedule[day] ?: emptyList()
    }

    fun getAllDays(): Set<String> {
        return workoutPlan.weeklySchedule.keys
    }

    fun getExerciseByName(exerciseName: String): Exercise? {
        return workoutPlan.weeklySchedule.values.flatten()
            .find { it.exerciseName == exerciseName }
    }

    fun getTodaysWorkout(): List<Exercise> {
        val today = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        return getExercisesForDay(today)
    }

    // Session Management
    suspend fun getTodaysSession(): WorkoutSession? {
        return firestoreRepository.getTodaysSession()
    }

    suspend fun createTodaysSession(): WorkoutSession {
        return firestoreRepository.createTodaysSession()
    }

    suspend fun updateSession(session: WorkoutSession) {
        firestoreRepository.updateSession(session)
    }

    suspend fun completeWorkout(sessionId: String, duration: Long) {
        firestoreRepository.completeWorkout(sessionId, duration)
    }

    // Exercise Progress Management
    suspend fun getExerciseProgress(sessionId: String): List<ExerciseProgress> {
        return firestoreRepository.getExerciseProgress(sessionId)
    }

    suspend fun updateExerciseProgress(progress: ExerciseProgress) {
        firestoreRepository.updateExerciseProgress(progress)
    }

    suspend fun getRecentProgressForExercise(exerciseName: String): List<ExerciseProgress> {
        return firestoreRepository.getRecentProgressForExercise(exerciseName)
    }

    // Stats and Streaks
    suspend fun getUserStats(): UserStats {
        return firestoreRepository.getUserStats()
    }

    fun getUserStatsFlow(): Flow<UserStats?> = flow {
        firestoreRepository.getUserStatsFlow().collect { stats ->
            emit(stats)
        }
    }

    suspend fun initializeUserStatsIfNeeded() {
        firestoreRepository.initializeUserData()
    }

    // All Sessions
    fun getAllSessions(): Flow<List<WorkoutSession>> = firestoreRepository.getAllSessionsFlow()

    suspend fun getWeeklyProgress(): Map<String, Boolean> {
        return firestoreRepository.getWeeklyProgress()
    }
    
    fun getWeeklyProgressFlow(): Flow<Map<String, Boolean>> = firestoreRepository.getWeeklyProgressFlow()

    suspend fun getDebugStreakInfo(): String {
        val stats = getUserStats()
        return "Current Streak: ${stats.currentStreak}, Longest: ${stats.longestStreak}, Total Workouts: ${stats.totalWorkouts}"
    }

    // Initialize Firestore user data (call when user logs in)
    suspend fun initializeUserData() {
        firestoreRepository.initializeUserData()
    }
} 