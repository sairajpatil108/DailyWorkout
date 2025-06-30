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

    // Reactive weekly progress that updates automatically
    val weeklyProgress: StateFlow<Map<String, Boolean>> = flow {
        while (true) {
            emit(repository.getWeeklyProgress())
            kotlinx.coroutines.delay(1000) // Check every second for updates
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())

    // Reactive weekly completion rate
    val weeklyCompletionRate: StateFlow<Float> = weeklyProgress.map { progress ->
        val completedDays = progress.values.count { it }
        val totalDays = progress.size
        if (totalDays > 0) completedDays.toFloat() / totalDays else 0f
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0f)

    // Reactive monthly stats
    val monthlyStats: StateFlow<MonthlyStats> = allSessions.map { sessions ->
        calculateMonthlyStats(sessions)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MonthlyStats())

    // Reactive workout frequency
    val workoutFrequency: StateFlow<List<DayFrequency>> = allSessions.map { sessions ->
        calculateWorkoutFrequency(sessions)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Reactive streak info
    val streakInfo: StateFlow<StreakInfo> = userStats.map { stats ->
        stats?.let {
            StreakInfo(
                currentStreak = it.currentStreak,
                longestStreak = it.longestStreak,
                isOnStreak = it.currentStreak > 0
            )
        } ?: StreakInfo()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), StreakInfo())

    init {
        loadProgressData()
        // Refresh data periodically to ensure UI stays updated
        startPeriodicRefresh()
    }

    private fun startPeriodicRefresh() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(30000) // Refresh every 30 seconds
                refreshData()
            }
        }
    }

    private fun loadProgressData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Initial load - reactive flows will handle updates automatically
                val initialWeeklyProgress = repository.getWeeklyProgress()
                
                _uiState.value = _uiState.value.copy(
                    weeklyProgress = initialWeeklyProgress,
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

    private fun calculateMonthlyStats(sessions: List<WorkoutSession>): MonthlyStats {
        val currentMonth = java.time.YearMonth.now()
        
        val monthSessions = sessions.filter { session ->
            try {
                val sessionDate = java.time.LocalDate.parse(session.date)
                sessionDate.year == currentMonth.year && sessionDate.monthValue == currentMonth.monthValue
            } catch (e: Exception) {
                false
            }
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

    private fun calculateWorkoutFrequency(sessions: List<WorkoutSession>): List<DayFrequency> {
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

    // Legacy methods for backward compatibility (will use reactive flows internally)
    fun getWeeklyCompletionRate(): Float = weeklyCompletionRate.value
    fun getMonthlyStats(): MonthlyStats = monthlyStats.value
    fun getWorkoutFrequency(): List<DayFrequency> = workoutFrequency.value
    fun getStreakInfo(): StreakInfo = streakInfo.value

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