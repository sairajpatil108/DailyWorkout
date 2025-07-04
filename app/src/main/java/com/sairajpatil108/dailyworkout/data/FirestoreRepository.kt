package com.sairajpatil108.dailyworkout.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    }
    
    private fun getUserDocument() = db.collection("users").document(getCurrentUserId())
    private fun getWorkoutSessionsCollection() = getUserDocument().collection("workoutSessions")
    private fun getExerciseProgressCollection() = getUserDocument().collection("exerciseProgress")
    
    // User Stats Methods
    suspend fun getUserStats(): UserStats {
        return try {
            val document = getUserDocument().get().await()
            val stats = document.data?.let { data ->
                UserStats(
                    currentStreak = (data["currentStreak"] as? Long)?.toInt() ?: 0,
                    longestStreak = (data["longestStreak"] as? Long)?.toInt() ?: 0,
                    totalWorkouts = (data["totalWorkouts"] as? Long)?.toInt() ?: 0,
                    lastWorkoutDate = data["lastWorkoutDate"] as? String ?: ""
                )
            } ?: UserStats()
            
            println("DEBUG Firestore: Retrieved user stats: $stats")
            stats
        } catch (e: Exception) {
            println("DEBUG Firestore: Error getting user stats: ${e.message}")
            UserStats()
        }
    }
    
    suspend fun updateUserStats(stats: UserStats) {
        try {
            val data = mapOf(
                "currentStreak" to stats.currentStreak,
                "longestStreak" to stats.longestStreak,
                "totalWorkouts" to stats.totalWorkouts,
                "lastWorkoutDate" to stats.lastWorkoutDate,
                "updatedAt" to System.currentTimeMillis()
            )
            
            getUserDocument().set(data).await()
            println("DEBUG Firestore: Updated user stats: $stats")
        } catch (e: Exception) {
            println("DEBUG Firestore: Error updating user stats: ${e.message}")
            throw e
        }
    }
    
    // Initialize user data (call this when user first logs in)
    suspend fun initializeUserData() {
        try {
            val document = getUserDocument().get().await()
            if (!document.exists()) {
                val initialStats = UserStats()
                updateUserStats(initialStats)
                println("DEBUG Firestore: Initialized user data")
            }
        } catch (e: Exception) {
            println("DEBUG Firestore: Error initializing user data: ${e.message}")
        }
    }
    
    // Real-time Flow methods for reactive UI
    fun getUserStatsFlow(): Flow<UserStats> = flow {
        try {
            getUserDocument().addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("DEBUG Firestore: Error listening to user stats: ${error.message}")
                    return@addSnapshotListener
                }
                
                val stats = snapshot?.data?.let { data ->
                    UserStats(
                        currentStreak = (data["currentStreak"] as? Long)?.toInt() ?: 0,
                        longestStreak = (data["longestStreak"] as? Long)?.toInt() ?: 0,
                        totalWorkouts = (data["totalWorkouts"] as? Long)?.toInt() ?: 0,
                        lastWorkoutDate = data["lastWorkoutDate"] as? String ?: ""
                    )
                } ?: UserStats()
                
                // In a real implementation, this would be handled by a proper Flow
                // For now, we'll emit the current value
            }
            
            // Emit initial value
            emit(getUserStats())
        } catch (e: Exception) {
            println("DEBUG Firestore: Error in getUserStatsFlow: ${e.message}")
            emit(UserStats())
        }
    }
    
    fun getAllSessionsFlow(): Flow<List<WorkoutSession>> = flow {
        try {
            getWorkoutSessionsCollection()
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        println("DEBUG Firestore: Error listening to sessions: ${error.message}")
                        return@addSnapshotListener
                    }
                    
                    // In a real implementation, this would emit to the Flow
                }
            
            // Emit initial value
            emit(getAllSessions())
        } catch (e: Exception) {
            println("DEBUG Firestore: Error in getAllSessionsFlow: ${e.message}")
            emit(emptyList())
        }
    }
    
    fun getWeeklyProgressFlow(): Flow<Map<String, Boolean>> = flow {
        try {
            // Listen to workout sessions changes and recalculate weekly progress
            getWorkoutSessionsCollection()
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        println("DEBUG Firestore: Error listening to weekly progress: ${error.message}")
                        return@addSnapshotListener
                    }
                    
                    // In a real implementation, this would emit the updated weekly progress
                }
            
            // Emit initial value
            emit(getWeeklyProgress())
        } catch (e: Exception) {
            println("DEBUG Firestore: Error in getWeeklyProgressFlow: ${e.message}")
            emit(emptyMap())
        }
    }
    
    // Workout Session Methods
    suspend fun getTodaysSession(): WorkoutSession? {
        return try {
            val today = dateFormat.format(Date())
            val query = getWorkoutSessionsCollection()
                .whereEqualTo("date", today)
                .limit(1)
                .get()
                .await()
            
            val document = query.documents.firstOrNull()
            document?.let { doc ->
                WorkoutSession(
                    id = doc.id,
                    date = doc.getString("date") ?: "",
                    dayOfWeek = doc.getString("dayOfWeek") ?: "",
                    isCompleted = doc.getBoolean("isCompleted") ?: false,
                    completedExercises = (doc.getLong("completedExercises") ?: 0).toInt(),
                    totalExercises = (doc.getLong("totalExercises") ?: 0).toInt(),
                    duration = doc.getLong("duration") ?: 0L
                )
            }
        } catch (e: Exception) {
            println("DEBUG Firestore: Error getting today's session: ${e.message}")
            null
        }
    }
    
    suspend fun createTodaysSession(): WorkoutSession {
        return try {
            val today = Date()
            val dateString = dateFormat.format(today)
            val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(today)
            val workoutPlan = WorkoutData.getWorkoutPlan()
            val exercises = workoutPlan.weeklySchedule[dayOfWeek] ?: emptyList()
            
            val sessionData = mapOf(
                "date" to dateString,
                "dayOfWeek" to dayOfWeek,
                "isCompleted" to false,
                "completedExercises" to 0,
                "totalExercises" to exercises.size,
                "duration" to 0L,
                "createdAt" to System.currentTimeMillis()
            )
            
            val documentRef = getWorkoutSessionsCollection().add(sessionData).await()
            
            WorkoutSession(
                id = documentRef.id,
                date = dateString,
                dayOfWeek = dayOfWeek,
                totalExercises = exercises.size
            )
        } catch (e: Exception) {
            println("DEBUG Firestore: Error creating today's session: ${e.message}")
            throw e
        }
    }
    
    suspend fun updateSession(session: WorkoutSession) {
        try {
            val data = mapOf(
                "date" to session.date,
                "dayOfWeek" to session.dayOfWeek,
                "isCompleted" to session.isCompleted,
                "completedExercises" to session.completedExercises,
                "totalExercises" to session.totalExercises,
                "duration" to session.duration,
                "lastUpdated" to System.currentTimeMillis()
            )
            
            getWorkoutSessionsCollection().document(session.id).set(data).await()

        } catch (e: Exception) {
            throw e
        }
    }
    

    
    suspend fun completeWorkout(sessionId: String, duration: Long) {
        try {
            val sessionData = mapOf(
                "isCompleted" to true,
                "duration" to duration,
                "completedAt" to System.currentTimeMillis(),
                "lastUpdated" to System.currentTimeMillis()
            )
            
            // Update the session
            getWorkoutSessionsCollection().document(sessionId).update(sessionData).await()
            
            // Update user stats after workout completion
            updateUserStatsAfterWorkout()
        } catch (e: Exception) {
            throw e
        }
    }
    
    // Exercise Progress Methods
    suspend fun getExerciseProgress(sessionId: String): List<ExerciseProgress> {
        return try {
            val query = getExerciseProgressCollection()
                .whereEqualTo("sessionId", sessionId)
                .get()
                .await()
            
            query.documents.mapNotNull { doc ->
                ExerciseProgress(
                    sessionId = doc.getString("sessionId") ?: "",
                    exerciseName = doc.getString("exerciseName") ?: "",
                    setsCompleted = (doc.getLong("setsCompleted") ?: 0).toInt(),
                    totalSets = (doc.getLong("totalSets") ?: 0).toInt(),
                    weight = (doc.getDouble("weight") ?: 0.0).toFloat(),
                    reps = (doc.getLong("reps") ?: 0).toInt(),
                    isCompleted = doc.getBoolean("isCompleted") ?: false,
                    notes = doc.getString("notes") ?: ""
                )
            }
        } catch (e: Exception) {
            println("DEBUG Firestore: Error getting exercise progress: ${e.message}")
            emptyList()
        }
    }
    
    suspend fun updateExerciseProgress(progress: ExerciseProgress) {
        try {
            val data = mapOf(
                "sessionId" to progress.sessionId,
                "exerciseName" to progress.exerciseName,
                "setsCompleted" to progress.setsCompleted,
                "totalSets" to progress.totalSets,
                "weight" to progress.weight,
                "reps" to progress.reps,
                "isCompleted" to progress.isCompleted,
                "notes" to progress.notes,
                "updatedAt" to System.currentTimeMillis()
            )
            
            // Use a unique document ID based on sessionId and exerciseName
            val documentId = "${progress.sessionId}_${progress.exerciseName.replace(" ", "_")}"
            getExerciseProgressCollection().document(documentId).set(data).await()
            

        } catch (e: Exception) {
            throw e
        }
    }
    
    suspend fun getRecentProgressForExercise(exerciseName: String): List<ExerciseProgress> {
        return try {
            val query = getExerciseProgressCollection()
                .whereEqualTo("exerciseName", exerciseName)
                .whereEqualTo("isCompleted", true)
                .limit(10)
                .get()
                .await()
            
            query.documents.mapNotNull { doc ->
                ExerciseProgress(
                    sessionId = doc.getString("sessionId") ?: "",
                    exerciseName = doc.getString("exerciseName") ?: "",
                    setsCompleted = (doc.getLong("setsCompleted") ?: 0).toInt(),
                    totalSets = (doc.getLong("totalSets") ?: 0).toInt(),
                    weight = (doc.getDouble("weight") ?: 0.0).toFloat(),
                    reps = (doc.getLong("reps") ?: 0).toInt(),
                    isCompleted = doc.getBoolean("isCompleted") ?: false,
                    notes = doc.getString("notes") ?: ""
                )
            }.sortedByDescending { it.sessionId }
        } catch (e: Exception) {
            println("DEBUG Firestore: Error getting recent progress: ${e.message}")
            emptyList()
        }
    }
    
    // Weekly Progress and Statistics
    suspend fun getWeeklyProgress(): Map<String, Boolean> {
        return try {
            val calendar = Calendar.getInstance()
            val today = Calendar.getInstance()
            val currentDayOfWeek = today.get(Calendar.DAY_OF_WEEK)
            
            // Calculate days to subtract to get to Monday
            val daysFromMonday = when (currentDayOfWeek) {
                Calendar.SUNDAY -> 6
                Calendar.MONDAY -> 0
                Calendar.TUESDAY -> 1
                Calendar.WEDNESDAY -> 2
                Calendar.THURSDAY -> 3
                Calendar.FRIDAY -> 4
                Calendar.SATURDAY -> 5
                else -> 0
            }
            
            calendar.add(Calendar.DAY_OF_YEAR, -daysFromMonday)
            
            val weekProgress = mutableMapOf<String, Boolean>()
            
            for (i in 0..5) { // Monday to Saturday
                val dateString = dateFormat.format(calendar.time)
                val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
                
                val query = getWorkoutSessionsCollection()
                    .whereEqualTo("date", dateString)
                    .whereEqualTo("isCompleted", true)
                    .limit(1)
                    .get()
                    .await()
                
                val isCompleted = query.documents.isNotEmpty()
                weekProgress[dayName] = isCompleted
                
                println("DEBUG Firestore Weekly Progress: $dayName ($dateString) - Completed: $isCompleted")
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            
            println("DEBUG Firestore Weekly Progress Result: $weekProgress")
            weekProgress
        } catch (e: Exception) {
            println("DEBUG Firestore: Error getting weekly progress: ${e.message}")
            emptyMap()
        }
    }
    
    suspend fun getAllSessions(): List<WorkoutSession> {
        return try {
            val query = getWorkoutSessionsCollection()
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()
            
            query.documents.mapNotNull { doc ->
                WorkoutSession(
                    id = doc.id,
                    date = doc.getString("date") ?: "",
                    dayOfWeek = doc.getString("dayOfWeek") ?: "",
                    isCompleted = doc.getBoolean("isCompleted") ?: false,
                    completedExercises = (doc.getLong("completedExercises") ?: 0).toInt(),
                    totalExercises = (doc.getLong("totalExercises") ?: 0).toInt(),
                    duration = doc.getLong("duration") ?: 0L
                )
            }
        } catch (e: Exception) {
            println("DEBUG Firestore: Error getting all sessions: ${e.message}")
            emptyList()
        }
    }
    
    // Private helper methods
    suspend fun updateUserStatsAfterWorkout() {
        try {
            println("DEBUG Firestore: Updating user stats after workout...")
            
            val currentStats = getUserStats()
            val completedWorkouts = getCompletedWorkoutCount()
            val streak = calculateCurrentStreak()
            
            println("DEBUG Firestore: Current stats - Current Streak: ${currentStats.currentStreak}, Longest: ${currentStats.longestStreak}, Total: ${currentStats.totalWorkouts}")
            println("DEBUG Firestore: New calculated values - Streak: $streak, Completed Workouts: $completedWorkouts")
            
            val updatedStats = currentStats.copy(
                currentStreak = streak,
                longestStreak = maxOf(currentStats.longestStreak, streak),
                totalWorkouts = completedWorkouts,
                lastWorkoutDate = dateFormat.format(Date())
            )
            
            updateUserStats(updatedStats)

        } catch (e: Exception) {
            // Handle error silently for production
        }
    }
    
    private suspend fun getCompletedWorkoutCount(): Int {
        return try {
            val query = getWorkoutSessionsCollection()
                .whereEqualTo("isCompleted", true)
                .get()
                .await()
            
            query.documents.size
        } catch (e: Exception) {
            println("DEBUG Firestore: Error getting completed workout count: ${e.message}")
            0
        }
    }
    
    private suspend fun calculateCurrentStreak(): Int {
        return try {
            var streak = 0
            val calendar = Calendar.getInstance()
            
            println("DEBUG Firestore: Starting streak calculation...")
            
            while (true) {
                val dateString = dateFormat.format(calendar.time)
                val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
                
                println("DEBUG Firestore: Checking date: $dateString ($dayOfWeek)")
                
                // Skip Sunday (rest day)
                if (dayOfWeek == "Sunday") {
                    println("DEBUG Firestore: Skipping Sunday (rest day)")
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    continue
                }
                
                val query = getWorkoutSessionsCollection()
                    .whereEqualTo("date", dateString)
                    .whereEqualTo("isCompleted", true)
                    .limit(1)
                    .get()
                    .await()
                
                if (query.documents.isNotEmpty()) {
                    streak++
                    println("DEBUG Firestore: Found completed workout for $dateString, streak: $streak")
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                } else {
                    // Check if it's today and no session exists yet
                    val today = dateFormat.format(Date())
                    if (dateString == today) {
                        println("DEBUG Firestore: Today's date, checking if it's a workout day")
                        // Today's session might not exist yet, check if it's a workout day
                        val workoutPlan = WorkoutData.getWorkoutPlan()
                        val exercises = workoutPlan.weeklySchedule[dayOfWeek] ?: emptyList()
                        if (exercises.isNotEmpty()) {
                            // It's a workout day but no session created yet, streak should break
                            println("DEBUG Firestore: Today is a workout day but no completed session found, breaking streak")
                            break
                        } else {
                            // It's not a workout day, continue checking previous days
                            println("DEBUG Firestore: Today is not a workout day, continuing")
                            calendar.add(Calendar.DAY_OF_YEAR, -1)
                            continue
                        }
                    } else {
                        // Found an incomplete workout day, streak is broken
                        println("DEBUG Firestore: Found incomplete workout day $dateString, breaking streak")
                        break
                    }
                }
            }
            
            println("DEBUG Firestore: Final streak calculated: $streak")
            streak
        } catch (e: Exception) {
            println("DEBUG Firestore: Error calculating current streak: ${e.message}")
            0
        }
    }


} 