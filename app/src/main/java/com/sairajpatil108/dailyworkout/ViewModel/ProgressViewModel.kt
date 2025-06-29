package com.sairajpatil108.dailyworkout.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sairajpatil108.dailyworkout.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    val userStats: StateFlow<UserStats?> = repository.getUserStatsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val allSessions: StateFlow<List<WorkoutSession>> = repository.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        loadProgressData()
    }

    private fun loadProgressData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val weeklyProgress = repository.getWeeklyProgress()
                val recentSessions = repository.getAllSessions().first().take(7)
                
                _uiState.value = _uiState.value.copy(
                    weeklyProgress = weeklyProgress,
                    recentSessions = recentSessions,
                    isLoading = false
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun getWeeklyCompletionRate(): Float {
        val progress = _uiState.value.weeklyProgress
        val completedDays = progress.values.count { it }
        val totalDays = progress.size
        return if (totalDays > 0) completedDays.toFloat() / totalDays else 0f
    }

    fun getMonthlyStats(): MonthlyStats {
        val sessions = allSessions.value
        val currentMonth = java.time.YearMonth.now()
        
        val monthSessions = sessions.filter { session ->
            val sessionDate = java.time.LocalDate.parse(session.date)
            sessionDate.year == currentMonth.year && sessionDate.monthValue == currentMonth.monthValue
        }
        
        val completedSessions = monthSessions.filter { it.isCompleted }
        val totalWorkoutTime = completedSessions.sumOf { it.duration }
        val averageWorkoutTime = if (completedSessions.isNotEmpty()) {
            totalWorkoutTime / completedSessions.size
        } else 0L
        
        return MonthlyStats(
            totalWorkouts = completedSessions.size,
            totalDays = monthSessions.size,
            totalWorkoutTime = totalWorkoutTime,
            averageWorkoutTime = averageWorkoutTime,
            completionRate = if (monthSessions.isNotEmpty()) {
                completedSessions.size.toFloat() / monthSessions.size
            } else 0f
        )
    }

    fun getStreakInfo(): StreakInfo {
        val stats = userStats.value ?: return StreakInfo()
        return StreakInfo(
            currentStreak = stats.currentStreak,
            longestStreak = stats.longestStreak,
            isOnStreak = stats.currentStreak > 0
        )
    }

    fun getWorkoutFrequency(): List<DayFrequency> {
        val sessions = allSessions.value
        val dayFrequency = mutableMapOf<String, Int>()
        
        sessions.filter { it.isCompleted }.forEach { session ->
            dayFrequency[session.dayOfWeek] = dayFrequency.getOrDefault(session.dayOfWeek, 0) + 1
        }
        
        return listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
            .map { day ->
                DayFrequency(
                    day = day,
                    frequency = dayFrequency.getOrDefault(day, 0)
                )
            }
    }

    fun refreshData() {
        loadProgressData()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ProgressUiState(
    val isLoading: Boolean = false,
    val weeklyProgress: Map<String, Boolean> = emptyMap(),
    val recentSessions: List<WorkoutSession> = emptyList(),
    val error: String? = null
)

data class MonthlyStats(
    val totalWorkouts: Int = 0,
    val totalDays: Int = 0,
    val totalWorkoutTime: Long = 0,
    val averageWorkoutTime: Long = 0,
    val completionRate: Float = 0f
)

data class StreakInfo(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val isOnStreak: Boolean = false
)

data class DayFrequency(
    val day: String,
    val frequency: Int
)

class ProgressViewModelFactory(
    private val repository: WorkoutRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgressViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgressViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 