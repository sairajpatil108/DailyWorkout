package com.sairajpatil108.dailyworkout.data

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow
import java.util.*

// Database Entities
@Entity(tableName = "workout_sessions")
data class WorkoutSession(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val date: String, // Format: "yyyy-MM-dd"
    val dayOfWeek: String, // Monday, Tuesday, etc.
    val isCompleted: Boolean = false,
    val completedExercises: Int = 0,
    val totalExercises: Int = 0,
    val duration: Long = 0L, // Duration in minutes
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "exercise_progress")
data class ExerciseProgress(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val sessionId: String,
    val exerciseName: String,
    val setsCompleted: Int = 0,
    val totalSets: Int = 0,
    val weight: Float = 0f,
    val reps: Int = 0,
    val isCompleted: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val id: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalWorkouts: Int = 0,
    val totalWorkoutDays: Int = 0,
    val lastWorkoutDate: String = "",
    val weeklyGoal: Int = 6 // Default 6 days per week
)

// DAOs
@Dao
interface WorkoutSessionDao {
    @Query("SELECT * FROM workout_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<WorkoutSession>>

    @Query("SELECT * FROM workout_sessions WHERE date = :date LIMIT 1")
    suspend fun getSessionByDate(date: String): WorkoutSession?

    @Query("SELECT * FROM workout_sessions WHERE date >= :startDate ORDER BY date ASC")
    suspend fun getSessionsFromDate(startDate: String): List<WorkoutSession>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSession)

    @Update
    suspend fun updateSession(session: WorkoutSession)

    @Query("SELECT COUNT(*) FROM workout_sessions WHERE isCompleted = 1")
    suspend fun getCompletedWorkoutCount(): Int

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 1 ORDER BY date DESC LIMIT 7")
    suspend fun getLastSevenCompletedWorkouts(): List<WorkoutSession>
}

@Dao
interface ExerciseProgressDao {
    @Query("SELECT * FROM exercise_progress WHERE sessionId = :sessionId")
    suspend fun getProgressBySession(sessionId: String): List<ExerciseProgress>

    @Query("SELECT * FROM exercise_progress WHERE exerciseName = :exerciseName ORDER BY sessionId DESC LIMIT 5")
    suspend fun getRecentProgressForExercise(exerciseName: String): List<ExerciseProgress>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ExerciseProgress)

    @Update
    suspend fun updateProgress(progress: ExerciseProgress)

    @Query("DELETE FROM exercise_progress WHERE sessionId = :sessionId")
    suspend fun deleteProgressBySession(sessionId: String)
}

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    suspend fun getUserStats(): UserStats?

    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    fun getUserStatsFlow(): Flow<UserStats?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: UserStats)

    @Query("UPDATE user_stats SET currentStreak = :streak WHERE id = 1")
    suspend fun updateCurrentStreak(streak: Int)

    @Query("UPDATE user_stats SET longestStreak = :streak WHERE id = 1")
    suspend fun updateLongestStreak(streak: Int)

    @Query("UPDATE user_stats SET totalWorkouts = totalWorkouts + 1 WHERE id = 1")
    suspend fun incrementTotalWorkouts()
}

// Database
@Database(
    entities = [WorkoutSession::class, ExerciseProgress::class, UserStats::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun exerciseProgressDao(): ExerciseProgressDao
    abstract fun userStatsDao(): UserStatsDao

    companion object {
        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context: android.content.Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_database"
                )
                .fallbackToDestructiveMigration() // Allow destructive migration for development
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Type Converters
class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
} 