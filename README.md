# Daily Workout App 💪

A comprehensive Android workout tracking application built with Jetpack Compose that helps you maintain your fitness routine, track progress, and build consistency streaks.

## Features ✨

### 🏋️‍♂️ **Workout Management**
- **Daily Workout Plans**: Pre-configured workout schedules for each day of the week
- **Exercise Guidance**: Detailed instructions for proper form and technique
- **Progress Tracking**: Track sets, reps, weights, and notes for each exercise
- **Workout Timer**: Built-in timer to track workout duration

### 📊 **Progress & Analytics**
- **Streak Tracking**: Monitor your current and longest workout streaks
- **Weekly Progress**: Visual representation of your weekly workout completion
- **Monthly Statistics**: Comprehensive stats including total workouts and time
- **Exercise History**: Track your progress on individual exercises over time

### 🎯 **Motivation & Consistency**
- **Rest Day Recognition**: Acknowledges rest days (Sundays) in your routine
- **Completion Celebrations**: Motivational messages and achievements
- **Visual Progress Indicators**: Progress bars and completion status
- **Streak Maintenance**: Encourages daily consistency with streak counters

### 📱 **User Experience**
- **Modern UI**: Clean, intuitive interface built with Material Design 3
- **Smooth Navigation**: Easy navigation between workout, progress, and exercise details
- **Responsive Design**: Optimized for different screen sizes
- **Offline Support**: Works completely offline with local database storage

## Technical Architecture 🏗️

### **MVVM Pattern**
- **Model**: Data classes and database entities
- **View**: Jetpack Compose UI components
- **ViewModel**: Business logic and state management

### **Technology Stack**
- **Kotlin**: Modern programming language for Android
- **Jetpack Compose**: Declarative UI toolkit
- **Room Database**: Local data persistence
- **Navigation Compose**: Type-safe navigation
- **Coroutines**: Asynchronous programming
- **Material Design 3**: Modern UI components

### **Database Schema**
- **WorkoutSession**: Tracks daily workout sessions
- **ExerciseProgress**: Records individual exercise completion
- **UserStats**: Maintains user statistics and streaks

## Workout Schedule 📅

The app includes a comprehensive 6-day workout split:

- **Monday**: Upper body focus (Chest, Shoulders, Triceps)
- **Tuesday**: Back and Biceps
- **Wednesday**: Legs and Core
- **Thursday**: Upper body focus (repeat)
- **Friday**: Back and Biceps (repeat)
- **Saturday**: Legs and Core (repeat)
- **Sunday**: Rest day 🧘‍♂️

Each workout includes:
- 5-6 exercises per session
- Detailed muscle targeting information
- Sets and reps recommendations
- Weight guidance for beginners
- Proper form instructions
- Do's and don'ts for safety

## Installation & Setup 🚀

### Prerequisites
- Android Studio (latest version)
- Android SDK (API level 29+)
- Kotlin support

### Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/DailyWorkout.git
   cd DailyWorkout
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory

3. **Build the project**
   - Let Android Studio sync the project
   - Build → Make Project (Ctrl+F9)

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click Run (Shift+F10)

## Usage Guide 📖

### **Getting Started**
1. **Home Screen**: View today's workout plan and your progress statistics
2. **Start Workout**: Tap "Start Workout" to begin your daily session
3. **Complete Exercises**: Follow the guided workout, marking exercises as complete
4. **Track Progress**: View your streaks and statistics in the Progress section

### **During Workouts**
- **Exercise Details**: Tap any exercise to view detailed instructions
- **Progress Tracking**: Use the completion dialog to log sets, reps, and weights
- **Workout Timer**: Monitor your session duration with the built-in timer
- **Navigation**: Easily move between exercises or pause/resume workouts

### **Progress Monitoring**
- **Daily Streaks**: Check your current streak and aim for your personal best
- **Weekly View**: See your completion status for the current week
- **Monthly Stats**: Review your overall progress and workout frequency
- **Exercise History**: Track improvements in individual exercises

## Customization 🎨

### **Adding New Exercises**
Modify the `WorkoutData` class in `data.kt` to add new exercises:

```kotlin
Exercise(
    exerciseName = "Your Exercise Name",
    targetMuscles = "Target Muscle Groups",
    sets = "3",
    reps = "10-12",
    weightGuidance = "Weight recommendations",
    properPosture = "Form instructions",
    dos = listOf("Do this", "Do that"),
    donts = listOf("Don't do this", "Don't do that"),
    tutorialVideos = listOf() // Future feature
)
```

### **Modifying Workout Schedule**
Update the `weeklySchedule` map in `WorkoutData.getWorkoutPlan()` to customize your weekly routine.

## Future Enhancements 🚀

- **Video Tutorials**: Integration with exercise demonstration videos
- **Custom Workouts**: Allow users to create personalized workout plans
- **Social Features**: Share progress with friends and workout buddies
- **Nutrition Tracking**: Complement workouts with nutrition monitoring
- **Wearable Integration**: Sync with fitness wearables for heart rate monitoring
- **Cloud Sync**: Backup and sync data across devices

## Contributing 🤝

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support 💬

If you encounter any issues or have questions:
- Create an issue on GitHub
- Check the documentation
- Review the code comments for implementation details

## Acknowledgments 🙏

- Thanks to the Android development community for excellent libraries and tools
- Inspired by fitness enthusiasts who prioritize consistency and progress tracking
- Built with love for the fitness community 💪

---

**Start your fitness journey today! Download, build, and begin tracking your daily workouts with consistency and motivation.** 🌟 #   D a i l y W o r k o u t  
 