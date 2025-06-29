package com.sairajpatil108.dailyworkout.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sairajpatil108.dailyworkout.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    private val _currentSession = MutableStateFlow<WorkoutSession?>(null)
    val currentSession: StateFlow<WorkoutSession?> = _currentSession.asStateFlow()

    private val _exerciseProgress = MutableStateFlow<List<ExerciseProgress>>(emptyList())
    val exerciseProgress: StateFlow<List<ExerciseProgress>> = _exerciseProgress.asStateFlow()

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

    private val _workoutTimer = MutableStateFlow(0L)
    val workoutTimer: StateFlow<Long> = _workoutTimer.asStateFlow()

    private val _isWorkoutActive = MutableStateFlow(false)
    val isWorkoutActive: StateFlow<Boolean> = _isWorkoutActive.asStateFlow()

    init {
        loadTodaysWorkout()
    }

    private fun loadTodaysWorkout() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val todaysExercises = repository.getTodaysWorkout()
                val session = repository.getTodaysSession() ?: repository.createTodaysSession()
                
                _currentSession.value = session
                _uiState.value = _uiState.value.copy(
                    todaysExercises = todaysExercises,
                    isLoading = false,
                    isRestDay = todaysExercises.isEmpty()
                )

                // Load existing progress if any
                loadExerciseProgress(session.id)
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun loadExerciseProgress(sessionId: String) {
        viewModelScope.launch {
            val progress = repository.getExerciseProgress(sessionId)
            _exerciseProgress.value = progress
        }
    }

    fun startWorkout() {
        viewModelScope.launch {
            _isWorkoutActive.value = true
            _workoutTimer.value = System.currentTimeMillis()
        }
    }

    fun pauseWorkout() {
        _isWorkoutActive.value = false
    }

    fun resumeWorkout() {
        _isWorkoutActive.value = true
    }

    fun completeExercise(exerciseName: String, sets: Int, reps: Int, weight: Float, notes: String = "") {
        viewModelScope.launch {
            val session = _currentSession.value ?: return@launch
            val exercise = _uiState.value.todaysExercises.find { it.exerciseName == exerciseName }
                ?: return@launch

            val progress = ExerciseProgress(
                sessionId = session.id,
                exerciseName = exerciseName,
                setsCompleted = sets,
                totalSets = exercise.sets.toIntOrNull() ?: 3,
                weight = weight,
                reps = reps,
                isCompleted = true,
                notes = notes
            )

            repository.updateExerciseProgress(progress)
            
            // Update local progress
            val currentProgress = _exerciseProgress.value.toMutableList()
            val existingIndex = currentProgress.indexOfFirst { 
                it.exerciseName == exerciseName && it.sessionId == session.id 
            }
            
            if (existingIndex >= 0) {
                currentProgress[existingIndex] = progress
            } else {
                currentProgress.add(progress)
            }
            
            _exerciseProgress.value = currentProgress

            // Update session progress
            val completedCount = currentProgress.count { it.isCompleted }
            val updatedSession = session.copy(completedExercises = completedCount)
            repository.updateSession(updatedSession)
            _currentSession.value = updatedSession

            // Move to next exercise if available
            if (_currentExerciseIndex.value < _uiState.value.todaysExercises.size - 1) {
                _currentExerciseIndex.value += 1
            }
        }
    }

    fun completeWorkout() {
        viewModelScope.launch {
            val session = _currentSession.value ?: return@launch
            val startTime = _workoutTimer.value
            val duration = (System.currentTimeMillis() - startTime) / 1000 / 60 // Convert to minutes

            repository.completeWorkout(session.id, duration)
            
            val completedSession = session.copy(
                isCompleted = true,
                duration = duration,
                completedExercises = _uiState.value.todaysExercises.size
            )
            
            _currentSession.value = completedSession
            _isWorkoutActive.value = false
            
            _uiState.value = _uiState.value.copy(
                isWorkoutCompleted = true,
                workoutDuration = duration
            )
        }
    }

    fun navigateToExercise(index: Int) {
        if (index >= 0 && index < _uiState.value.todaysExercises.size) {
            _currentExerciseIndex.value = index
        }
    }

    fun getExerciseProgress(exerciseName: String): ExerciseProgress? {
        return _exerciseProgress.value.find { it.exerciseName == exerciseName }
    }

    fun getRecentProgressForExercise(exerciseName: String) {
        viewModelScope.launch {
            val recentProgress = repository.getRecentProgressForExercise(exerciseName)
            _uiState.value = _uiState.value.copy(recentProgress = recentProgress)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetWorkout() {
        _currentExerciseIndex.value = 0
        _isWorkoutActive.value = false
        _workoutTimer.value = 0L
        _uiState.value = _uiState.value.copy(
            isWorkoutCompleted = false,
            workoutDuration = 0L
        )
        loadTodaysWorkout()
    }
}

data class WorkoutUiState(
    val isLoading: Boolean = false,
    val todaysExercises: List<Exercise> = emptyList(),
    val isRestDay: Boolean = false,
    val isWorkoutCompleted: Boolean = false,
    val workoutDuration: Long = 0L,
    val recentProgress: List<ExerciseProgress> = emptyList(),
    val error: String? = null
)

class WorkoutViewModelFactory(
    private val repository: WorkoutRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 