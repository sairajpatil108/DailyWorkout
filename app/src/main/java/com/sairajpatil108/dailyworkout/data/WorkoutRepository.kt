package com.sairajpatil108.dailyworkout.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

class WorkoutRepository(
    private val workoutSessionDao: WorkoutSessionDao,
    private val exerciseProgressDao: ExerciseProgressDao,
    private val userStatsDao: UserStatsDao
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
        val today = dateFormat.format(Date())
        return workoutSessionDao.getSessionByDate(today)
    }

    suspend fun createTodaysSession(): WorkoutSession {
        val today = Date()
        val dateString = dateFormat.format(today)
        val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(today)
        val exercises = getExercisesForDay(dayOfWeek)
        
        val session = WorkoutSession(
            date = dateString,
            dayOfWeek = dayOfWeek,
            totalExercises = exercises.size
        )
        
        workoutSessionDao.insertSession(session)
        return session
    }

    suspend fun updateSession(session: WorkoutSession) {
        workoutSessionDao.updateSession(session)
        
        // Update stats if workout is completed
        if (session.isCompleted) {
            updateUserStats()
        }
    }

    suspend fun completeWorkout(sessionId: String, duration: Long) {
        val session = workoutSessionDao.getAllSessions().first()
            .find { it.id == sessionId } ?: return
        
        val updatedSession = session.copy(
            isCompleted = true,
            duration = duration,
            completedExercises = session.totalExercises
        )
        
        updateSession(updatedSession)
    }

    // Exercise Progress Management
    suspend fun getExerciseProgress(sessionId: String): List<ExerciseProgress> {
        return exerciseProgressDao.getProgressBySession(sessionId)
    }

    suspend fun updateExerciseProgress(progress: ExerciseProgress) {
        exerciseProgressDao.insertProgress(progress)
    }

    suspend fun getRecentProgressForExercise(exerciseName: String): List<ExerciseProgress> {
        return exerciseProgressDao.getRecentProgressForExercise(exerciseName)
    }

    // Stats and Streaks
    suspend fun getUserStats(): UserStats {
        return userStatsDao.getUserStats() ?: UserStats()
    }

    fun getUserStatsFlow(): Flow<UserStats?> {
        return userStatsDao.getUserStatsFlow()
    }

    private suspend fun updateUserStats() {
        val currentStats = getUserStats()
        val completedWorkouts = workoutSessionDao.getCompletedWorkoutCount()
        val streak = calculateCurrentStreak()
        
        val updatedStats = currentStats.copy(
            currentStreak = streak,
            longestStreak = maxOf(currentStats.longestStreak, streak),
            totalWorkouts = completedWorkouts,
            lastWorkoutDate = dateFormat.format(Date())
        )
        
        userStatsDao.insertOrUpdateStats(updatedStats)
    }

    private suspend fun calculateCurrentStreak(): Int {
        var streak = 0
        val calendar = Calendar.getInstance()
        
        // Start checking from yesterday (since today's workout might just be completed)
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        
        while (true) {
            val dateString = dateFormat.format(calendar.time)
            val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
            
            // Skip Sunday (rest day)
            if (dayOfWeek == "Sunday") {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                continue
            }
            
            val session = workoutSessionDao.getSessionByDate(dateString)
            if (session?.isCompleted == true) {
                streak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        
        return streak
    }

    // All Sessions
    fun getAllSessions(): Flow<List<WorkoutSession>> {
        return workoutSessionDao.getAllSessions()
    }

    suspend fun getWeeklyProgress(): Map<String, Boolean> {
        val calendar = Calendar.getInstance()
        val weekProgress = mutableMapOf<String, Boolean>()
        
        // Get current week (Monday to Saturday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        
        for (i in 0..5) { // Monday to Saturday
            val dateString = dateFormat.format(calendar.time)
            val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
            val session = workoutSessionDao.getSessionByDate(dateString)
            weekProgress[dayName] = session?.isCompleted ?: false
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        
        return weekProgress
    }
} 