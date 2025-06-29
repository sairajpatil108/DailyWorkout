package com.sairajpatil108.dailyworkout

import android.app.Application
import com.sairajpatil108.dailyworkout.data.WorkoutDatabase
import com.sairajpatil108.dailyworkout.data.WorkoutRepository

class WorkoutApplication : Application() {
    
    // Database instance - lazy initialization
    val database by lazy { WorkoutDatabase.getDatabase(this) }
    
    // Repository instance - lazy initialization with database DAOs
    val repository by lazy { 
        WorkoutRepository(
            workoutSessionDao = database.workoutSessionDao(),
            exerciseProgressDao = database.exerciseProgressDao(),
            userStatsDao = database.userStatsDao()
        ) 
    }
} 