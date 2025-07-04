package com.sairajpatil108.dailyworkout

import android.app.Application
import com.sairajpatil108.dailyworkout.data.FirestoreRepository
import com.sairajpatil108.dailyworkout.data.WorkoutRepository

class WorkoutApplication : Application() {
    
    // Firestore repository instance - lazy initialization
    private val firestoreRepository by lazy { FirestoreRepository() }
    
    // Repository instance - lazy initialization with Firestore
    val repository by lazy { 
        WorkoutRepository(firestoreRepository = firestoreRepository) 
    }
} 